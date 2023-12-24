package org.scoredroid.infra.test.fixtures.dataaccess.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.datasource.local.InMemoryMatchDataSource
import org.scoredroid.infra.dataaccess.datasource.local.TransientMatchDataSource
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.utils.mappers.toMatchResponse


class MatchRepositoryFixture(
    val repository: MatchRepository,
    private val transientMatchDataSource: TransientMatchDataSource,
    private val persistentMatchDataSource: FakePersistentMatchDataSource,
) {
    suspend fun createMatch(request: CreateMatchRepositoryRequest): Match {
        return repository.createMatch(request)
    }

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

    suspend fun renameMatch(matchId: Long, name: String) {
        repository.updateMatch(getMatch(matchId).rename(name))
    }

    suspend fun removeMatch(matchId: Long) {
        repository.removeMatch(matchId)
    }

    suspend fun addTeamsToExistingMatch(matchId: Long, vararg teamNames: String) {
        teamNames.forEach { name ->
            repository.updateMatch(getMatch(matchId).addTeam(name))
        }
    }

    suspend fun makeFirstTeam(matchId: Long, teamAt: Int) {
        repository.updateMatch(getMatch(matchId).moveTeam(teamAt, 0))
    }

    suspend fun removeTeam(matchId: Long, teamAt: Int) {
        repository.updateMatch(getMatch(matchId).removeTeam(teamAt))
    }

    suspend fun renameTeam(matchId: Long, teamAt: Int, name: String) {
        repository.renameTeam(matchId, teamAt, name)
    }

    suspend fun bumpScore(matchId: Long, teamAt: Int) {
        repository.updateScore(matchId, teamAt) { currentScore ->
            currentScore + 1
        }
    }

    suspend fun getMatchFlow(matchId: Long): Flow<MatchResponse?> {
        return repository.getMatchFlow(matchId)
            .map { it?.toMatchResponse() }
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

    fun persistenceFailsWith(exception: Throwable) {
        persistentMatchDataSource.failWith(exception)
    }

    fun coldStart(): MatchRepositoryFixture {
        val inMemoryDataSource = InMemoryMatchDataSource.newInstance()
        val repository = MatchRepository(inMemoryDataSource, this.persistentMatchDataSource)
        return MatchRepositoryFixture(
            repository,
            inMemoryDataSource,
            this.persistentMatchDataSource,
        )
    }

    suspend fun getMatch(matchId: Long): Match {
        return repository.getMatch(matchId).getOrThrow()
    }

    suspend fun hasMatch(matchId: Long): Boolean {
        return repository.getMatch(matchId).isSuccess
    }
}
