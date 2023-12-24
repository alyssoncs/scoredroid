package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
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

            result.isFailure.shouldBeTrue()
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

            result.isSuccess.shouldBeTrue()
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `removes the match`(rebootApplication: Boolean) = runTest {
            if (rebootApplication) fixture.rebootApplication()

            removeMatch(matchId)

            fixture.hasMatch(matchId).shouldBeFalse()
        }

        @Test
        fun `flow is updated`() = runTest {
            fixture.getMatchFlow(matchId).test {
                removeMatch(matchId)

                awaitItem().shouldNotBeNull()
                awaitItem().shouldBeNull()
            }
        }
    }
}
