package org.scoredroid.infra.test.fixtures.dataaccess.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.datasource.local.TransientMatchDataSource
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.utils.mappers.toMatchResponse

class MatchRepositoryFixture(
    val repository: MatchRepository,
    private val transientMatchDataSource: TransientMatchDataSource,
) {
    suspend fun createEmptyMatch(): Match {
        return repository.createMatch(CreateMatchRepositoryRequest())
    }

    suspend fun createNamedMatch(matchName: String): Match {
        return repository.createMatch(CreateMatchRepositoryRequest(name = matchName))
    }

    suspend fun createMatchWithTeams(vararg teamNames: String): Match {
        return createEmptyMatch().also { match ->
            addTeamsToExistingMatch(match.id, *teamNames)
        }
    }

    suspend fun addTeamsToExistingMatch(matchId: Long, vararg teamNames: String) {
        teamNames.forEach { name ->
            repository.addTeam(matchId = matchId, AddTeamRepositoryRequest(name))
        }
    }

    suspend fun hasMatchFlow(matchId: Long): Boolean {
        return repository.getMatchFlow(matchId) != null
    }

    suspend fun getMatchFlow(matchId: Long): Flow<MatchResponse> {
        return repository.getMatchFlow(matchId)
            ?.map { it.toMatchResponse() }!!
    }

    suspend fun rebootApplication() {
        transientMatchDataSource.getAllMatches()
            .map { match -> match.id }
            .forEach { matchId -> repository.persist(matchId) }

        clearInMemoryData()
    }

    suspend fun clearInMemoryData() {
        transientMatchDataSource.clear()
    }
}
