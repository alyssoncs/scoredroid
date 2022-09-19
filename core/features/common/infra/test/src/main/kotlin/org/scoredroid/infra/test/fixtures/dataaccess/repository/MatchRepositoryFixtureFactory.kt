package org.scoredroid.infra.test.fixtures.dataaccess.repository

import org.scoredroid.infra.dataaccess.datasource.local.FakeTransientMatchDataSource
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource

object MatchRepositoryFixtureFactory {
    private val autoIncrement: (Long) -> Long = { currentId -> currentId.inc() }

    fun create(
        initialMatchId: Long = 0L,
        matchIdStrategy: (currentId: Long) -> Long = autoIncrement,
    ): MatchRepositoryFixture {
        val inMemoryDataSource = FakeTransientMatchDataSource()
        val persistentDataSource = FakePersistentMatchDataSource(initialMatchId, matchIdStrategy)
        val repository = MatchRepository(inMemoryDataSource, persistentDataSource)
        return MatchRepositoryFixture(repository, inMemoryDataSource)
    }
}
