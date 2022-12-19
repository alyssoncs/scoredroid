package org.scoredroid.match.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates

@OptIn(ExperimentalCoroutinesApi::class)
class ClearTransientMatchDataTest {
    private val fixture = MatchRepositoryFixtureFactory.create()
    private val clearTransientData = ClearTransientMatchData(fixture.repository)

    @Nested
    inner class NoMatch {
        @Test
        fun `return failure`() = runTest {
            val result = clearTransientData(0L)

            assertThat(result.isFailure).isTrue()
        }
    }

    @Nested
    inner class MatchExists {

        private var matchId by Delegates.notNull<Long>()

        @BeforeEach
        fun setUp() = runTest {
            matchId = fixture.createNamedMatch("old name").id
        }

        @Test
        fun `return success`() = runTest {
            val result = clearTransientData(matchId)

            assertThat(result.isSuccess).isTrue()
        }

        @Test
        fun `clean the transient data`() = runTest {
            fixture.rebootApplication()
            fixture.repository.renameMatch(matchId, "new name")
            assertThat(fixture.repository.getMatch(matchId)!!.name).isEqualTo("new name")

            clearTransientData(matchId)

            assertThat(fixture.repository.getMatch(matchId)!!.name).isEqualTo("old name")
        }

        @Test
        fun `flow is updated to persistent value`() = runTest(
            context = UnconfinedTestDispatcher() // TODO: this can probably be removed after turbine bump
        ) {
            fixture.getMatchFlow(matchId).test {
                fixture.rebootApplication()
                fixture.repository.renameMatch(matchId, "new name")

                clearTransientData(matchId)

                assertThat(awaitItem()!!.name).isEqualTo("old name")
                assertThat(awaitItem()!!.name).isEqualTo("new name")
                assertThat(awaitItem()!!.name).isEqualTo("old name")
            }
        }
    }
}