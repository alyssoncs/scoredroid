package org.scoredroid.infra.dataaccess.datasource.local

import org.scoredroid.domain.entities.Match
import org.scoredroid.domain.entities.Score.Companion.toScore
import org.scoredroid.domain.entities.Team
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
        return if (getMatch(match.id) == null) {
            Result.failure(Throwable("Match with id = ${match.id} not found"))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun removeMatch(matchId: Long): Result<Unit> {
        val matches = matchDao.getMatchById(matchId)

        if (matches.isEmpty())
            return Result.failure(Exception("match with id $matchId was not found"))

        matchDao.deleteMatch(matches.keys.first())
        return Result.success(Unit)
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
}
