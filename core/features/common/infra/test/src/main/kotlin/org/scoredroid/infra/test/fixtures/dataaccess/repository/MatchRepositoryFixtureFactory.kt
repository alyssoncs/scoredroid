package org.scoredroid.infra.test.fixtures.dataaccess.repository

import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakeMatchLocalDataSource
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource

object MatchRepositoryFixtureFactory {
    private val autoIncrement: (Long) -> Long = { currentId -> currentId.inc() }

    fun create(
        initialMatchId: Long = 0L,
        matchIdStrategy: (currentId: Long) -> Long = autoIncrement,
    ): MatchRepositoryFixture {
        val localDataSource = FakeMatchLocalDataSource()
        val persistentDataSource = FakePersistentMatchDataSource(initialMatchId, matchIdStrategy)
        val repository = MatchRepository(localDataSource, persistentDataSource)
        return MatchRepositoryFixture(repository)
    }
}
