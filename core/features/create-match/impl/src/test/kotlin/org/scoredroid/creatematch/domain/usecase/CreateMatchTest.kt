package org.scoredroid.creatematch.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.scoredroid.creatematch.data.repository.MatchRepository
import org.scoredroid.creatematch.testdouble.data.datasource.local.MatchLocalDataSourceStub

@ExperimentalCoroutinesApi
class CreateMatchTest {
    private val matchLocalDataSourceStub = MatchLocalDataSourceStub()
    private val matchRepository = MatchRepository(matchLocalDataSourceStub)
    private val createMatch = CreateMatch(matchRepository)

    @Test
    fun `id provided by local data source`() = runTest {
        matchLocalDataSourceStub.matchId = 2

        val match = createMatch()

        assertThat(match.id).isEqualTo(2)
    }

    @Test
    fun `no teams`() = runTest {
        val match = createMatch()

        assertThat(match.teams).isEmpty()
    }
}