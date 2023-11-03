package org.scoredroid.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

class GetMatchFlowTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val getMatchFlow = GetMatchFlow(fixture.repository)

    @Nested
    inner class MatchDoesNotExists {

        @Test
        fun `should return flow with null match`() = runTest {
            val flow = getMatchFlow(matchId = 0L)

            assertThat(flow.first()).isNull()
        }
    }

    @Nested
    inner class MatchExists {

        @BeforeEach
        fun setUp() = runTest {
            fixture.createNamedMatch("match name")
        }

        @Test
        fun `should have the correct match`() = runTest {
            val flow = getMatchFlow(matchId = 0L)

            assertThat(flow.first()!!.name).isEqualTo("match name")
        }

        @Test
        fun `updates on the match updates the flow`() = runTest {
            getMatchFlow(matchId = 0L).test {
                skipItems(1)

                fixture.renameMatch(0L, "new name")

                assertThat(awaitItem()!!.name).isEqualTo("new name")
            }
        }

        @Test
        fun `match deletion makes the flow emits null`() = runTest {
            getMatchFlow(matchId = 0L).test {
                skipItems(1)

                fixture.removeMatch(0L)

                assertThat(awaitItem()).isNull()
            }
        }

        @Test
        fun `should get correct flow on cold start`() = runTest {
            val coldStartGetMatchFlow = GetMatchFlow(fixture.coldStart().repository)
            val flow = coldStartGetMatchFlow(matchId = 0L)

            assertThat(flow.first()!!.name).isEqualTo("match name")
        }
    }
}
