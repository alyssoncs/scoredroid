package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
import org.scoredroid.infra.dataaccess.dao.DeleteTeamDaoRequestModel
import org.scoredroid.infra.dataaccess.dao.InsertMatchDaoRequestModel
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.entities.MatchEntity
import org.scoredroid.infra.dataaccess.entities.TeamEntity
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

class MatchDaoToPersistentMatchDataSourceAdapter(
    private val matchDao: MatchDao,
) : PersistentMatchDataSource {

    override suspend fun createMatch(matchRequest: CreateMatchRepositoryRequest): Match {
        val matchId = matchDao.insertMatch(InsertMatchDaoRequestModel(matchRequest.name))
        val teams = matchRequest.teams.mapIndexed { index, team ->
            TeamEntity(name = team.name, score = 0, matchId = matchId, position = index)
        }
        matchDao.insertTeams(teams)

        return matchDao.getMatchById(matchId).toDomain()
    }

    override suspend fun getMatch(matchId: Long): Match? {
        val matches = matchDao.getMatchById(matchId)

        if (matches.isEmpty())
            return null

        return matches.toDomain()
    }

    override suspend fun save(match: Match): Result<Unit> {
        val currentMatch = getMatch(match.id)
            ?: return Result.failure(Throwable("Match with id = ${match.id} not found"))

        updateMatch(match)
        updateTeams(match, currentMatch)

        return Result.success(Unit)
    }

    override suspend fun removeMatch(matchId: Long): Result<Unit> {
        val matches = matchDao.getMatchById(matchId)

        if (matches.isEmpty())
            return Result.failure(Exception("Match with id $matchId was not found"))

        matchDao.deleteMatch(matches.keys.first())
        return Result.success(Unit)
    }


    private suspend fun updateMatch(match: Match) {
        matchDao.updateMatch(match.toEntity())
    }

    private suspend fun updateTeams(
        match: Match,
        currentMatch: Match
    ) {
        val teamEntities = match.toEntities()
        matchDao.deleteTeams(getMissingTeams(currentMatch, teamEntities))
        matchDao.updateTeams(getTeamsToUpdate(currentMatch, teamEntities))
        matchDao.insertTeams(getAdditionalTeams(currentMatch, teamEntities))
    }

    private fun getMissingTeams(
        currentMatch: Match,
        teamsToUpdate: List<TeamEntity>
    ): List<DeleteTeamDaoRequestModel> {
        return List(
            currentMatch.teams
                .safeSubList(teamsToUpdate.size, currentMatch.teams.size)
                .size
        ) { idx ->
            DeleteTeamDaoRequestModel(
                matchId = currentMatch.id,
                position = idx + teamsToUpdate.size.toLong()
            )
        }
    }

    private fun getTeamsToUpdate(
        currentMatch: Match,
        teamsToUpdate: List<TeamEntity>
    ): List<TeamEntity> {
        val indexOfLastSavedTeam = teamsToUpdate.indexOfLastPresentOn(currentMatch)
        return teamsToUpdate.subList(0, indexOfLastSavedTeam.inc())
    }

    private fun getAdditionalTeams(
        currentMatch: Match,
        teamsToUpdate: List<TeamEntity>
    ): List<TeamEntity> {
        val indexOfLastSavedTeam = teamsToUpdate.indexOfLastPresentOn(currentMatch)
        return teamsToUpdate.subList(indexOfLastSavedTeam.inc(), teamsToUpdate.size)
    }

    private fun List<TeamEntity>.indexOfLastPresentOn(
        currentMatch: Match
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

    private fun Map<MatchEntity, List<TeamEntity>>.toDomain(): Match {
        val matchEntity = keys.first()
        val teamsEntities = this[matchEntity].orEmpty()
        return Match(
            id = matchEntity.id,
            name = matchEntity.name,
            teams = teamsEntities.toDomain(),
        )
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
}
