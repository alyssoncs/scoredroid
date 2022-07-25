package org.scoredroid.infra.test.fixtures.dataaccess.repository

import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakeMatchLocalDataSource

object MatchRepositoryTestFactory {
    fun create(): MatchRepositoryFixture {
        val localDataSource = FakeMatchLocalDataSource()
        val repository = MatchRepository(localDataSource)
        return MatchRepositoryFixture(repository)
    }
}
