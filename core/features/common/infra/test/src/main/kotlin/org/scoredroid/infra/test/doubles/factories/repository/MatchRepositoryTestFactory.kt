package org.scoredroid.infra.test.doubles.factories.repository

import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.test.doubles.FakeMatchLocalDataSource

object MatchRepositoryTestFactory {
    fun create(): MatchRepository {
        val localDataSource = FakeMatchLocalDataSource()
        return MatchRepository(localDataSource)
    }
}