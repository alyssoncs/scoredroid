package org.scoredroid.infra.test.fixtures.dataaccess.repository

import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakeMatchLocalDataSource

object MatchRepositoryTestFactory {
    fun create(): MatchRepository {
        val localDataSource = FakeMatchLocalDataSource()
        return MatchRepository(localDataSource)
    }
}
