package org.scoredroid.match.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates

class RemoveMatchTest {
    private val fixture = MatchRepositoryFixtureFactory.create()
    private val removeMatch = RemoveMatch(fixture.repository)

    @Nested
    inner class NoMatch {
        @Test
        fun `return failure`() = runTest {
            val result = removeMatch(0L)

            assertThat(result.isFailure).isTrue()
        }
    }

    @Nested
    inner class MatchExists {
        private var matchId by Delegates.notNull<Long>()

        @BeforeEach
        internal fun setUp() = runTest {
            matchId = fixture.createEmptyMatch().id
        }

        @Test
        fun `return success`() = runTest {
            val result = removeMatch(matchId)

            assertThat(result.isSuccess).isTrue()
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `removes the match`(rebootApplication: Boolean) = runTest {
            if (rebootApplication) fixture.rebootApplication()

            removeMatch(matchId)

            assertThat(fixture.repository.getMatch(matchId)).isNull()
        }

        @Test
        fun `flow is updated`() = runTest {
            fixture.getMatchFlow(matchId).test {
                removeMatch(matchId)

                assertThat(awaitItem()).isNotNull()
                assertThat(awaitItem()).isNull()
            }
        }
    }
}
