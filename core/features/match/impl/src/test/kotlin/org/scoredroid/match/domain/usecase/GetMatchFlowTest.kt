package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

@OptIn(ExperimentalCoroutinesApi::class)
class GetMatchFlowTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val getMatchFlow = GetMatchFlow(fixture.repository)

    @Nested
    inner class MatchDoesNotExists {

        @Test
        fun `should return null`() = runTest {
            val flow = getMatchFlow(matchId = 0L)

            assertThat(flow).isNull()
        }
    }

    @Nested
    inner class MatchExists {

        @BeforeEach
        fun setUp() = runTest {
            fixture.createNamedMatch("match name")
        }

        @Test
        fun `should return a flow`() = runTest {
            val flow = getMatchFlow(matchId = 0L)

            assertThat(flow).isNotNull()
        }

        @Test
        fun `should have the correct match`() = runTest {
            val flow = getMatchFlow(matchId = 0L)!!

            assertThat(flow.first().name).isEqualTo("match name")
        }
    }
}