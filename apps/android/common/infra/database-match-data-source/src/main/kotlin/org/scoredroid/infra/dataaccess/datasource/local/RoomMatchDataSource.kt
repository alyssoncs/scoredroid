package org.scoredroid.infra.dataaccess.datasource.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.dao.DeleteTeamDaoRequestModel
import org.scoredroid.infra.dataaccess.dao.InsertMatchDaoRequestModel
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.entities.MatchEntity
import org.scoredroid.infra.dataaccess.entities.TeamEntity
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

internal class RoomMatchDataSource(
    private val matchDao: MatchDao,
) : PersistentMatchDataSource {

    override suspend fun createMatch(matchRequest: CreateMatchRepositoryRequest): Match =
        withContext(Dispatchers.IO) {
            val matchId = matchDao.insertMatch(InsertMatchDaoRequestModel(matchRequest.name))
            val teams = matchRequest.teams.mapIndexed { index, team ->
                TeamEntity(name = team.name, score = 0, matchId = matchId, position = index)
            }
            matchDao.insertTeams(teams)

            return@withContext matchDao.getMatchById(matchId).toDomain().first()
        }

    override suspend fun getMatch(matchId: Long): Match? = withContext(Dispatchers.IO) {
        val matches = matchDao.getMatchById(matchId).toDomain()

        return@withContext if (matches.isEmpty())
            null
        else
            matches.first()
    }

    override suspend fun getAllMatches(): List<Match> = withContext(Dispatchers.IO) {
        val map: Map<MatchEntity, List<TeamEntity>> = matchDao.getAllMatches()
        return@withContext map.toDomain()
    }

    override suspend fun save(match: Match): Result<Unit> = withContext(Dispatchers.IO) {
        val currentMatch = getMatch(match.id)
            ?: return@withContext Result.failure(matchNotFound(match.id))

        updateMatch(match)
        updateTeams(match, currentMatch)

        return@withContext Result.success(Unit)
    }

    override suspend fun removeMatch(matchId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        val matches = matchDao.getMatchById(matchId)

        if (matches.isEmpty())
            return@withContext Result.failure(matchNotFound(matchId))

        matchDao.deleteMatch(matches.keys.first())
        return@withContext Result.success(Unit)
    }

    private suspend fun updateMatch(match: Match) {
        matchDao.updateMatch(match.toEntity())
    }

    private suspend fun updateTeams(
        match: Match,
        currentMatch: Match,
    ) {
        val teamEntities = match.toEntities()
        matchDao.deleteTeams(getMissingTeams(currentMatch, teamEntities))
        matchDao.updateTeams(getTeamsToUpdate(currentMatch, teamEntities))
        matchDao.insertTeams(getAdditionalTeams(currentMatch, teamEntities))
    }

    private fun getMissingTeams(
        currentMatch: Match,
        teamsToUpdate: List<TeamEntity>,
    ): List<DeleteTeamDaoRequestModel> {
        return List(
            currentMatch.teams
                .safeSubList(teamsToUpdate.size, currentMatch.teams.size)
                .size,
        ) { idx ->
            DeleteTeamDaoRequestModel(
                matchId = currentMatch.id,
                position = idx + teamsToUpdate.size.toLong(),
            )
        }
    }

    private fun getTeamsToUpdate(
        currentMatch: Match,
        teamsToUpdate: List<TeamEntity>,
    ): List<TeamEntity> {
        val indexOfLastSavedTeam = teamsToUpdate.indexOfLastPresentOn(currentMatch)
        return teamsToUpdate.subList(0, indexOfLastSavedTeam.inc())
    }

    private fun getAdditionalTeams(
        currentMatch: Match,
        teamsToUpdate: List<TeamEntity>,
    ): List<TeamEntity> {
        val indexOfLastSavedTeam = teamsToUpdate.indexOfLastPresentOn(currentMatch)
        return teamsToUpdate.subList(indexOfLastSavedTeam.inc(), teamsToUpdate.size)
    }

    private fun List<TeamEntity>.indexOfLastPresentOn(
        currentMatch: Match,
    ): Int {
        return if (currentMatch.teams.size < size)
            currentMatch.teams.lastIndex
        else
            this.lastIndex
    }

    private fun Match.toEntity(): MatchEntity {
        return MatchEntity(id = id, name = name)
    }

    private fun Match.toEntities(): List<TeamEntity> {
        return teams.mapIndexed { idx, team ->
            TeamEntity(
                name = team.name,
                score = team.score.intValue,
                matchId = id,
                position = idx,
            )
        }
    }

    private fun Map<MatchEntity, List<TeamEntity>>.toDomain(): List<Match> {
        return map { mapEntry ->
            Match(
                id = mapEntry.key.id,
                name = mapEntry.key.name,
                teams = mapEntry.value.toDomain(),
            )
        }
    }

    private fun List<TeamEntity>.toDomain(): List<Team> {
        return sortedBy { it.position }
            .map { teamEntity ->
                Team(
                    name = teamEntity.name,
                    score = teamEntity.score.toScore(),
                )
            }
    }

    private fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> {
        return if (fromIndex in this.indices && toIndex in fromIndex..this.size)
            this.subList(fromIndex, toIndex)
        else
            emptyList()
    }

    private fun matchNotFound(matchId: Long): Throwable {
        return Throwable("Match with id = $matchId was not found")
    }
}
