package org.scoredroid.match.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.infra.test.assertions.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates


@ExperimentalCoroutinesApi
class RenameMatchTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val renameMatch = RenameMatch(fixture.repository)

    @Nested
    inner class MatchNotFound {

        @Test
        fun `return error`() = runTest {
            val nonExistingMatchId = 0L

            val result = renameMatch(nonExistingMatchId, "irrelevant")

            assertThat(result.isFailure).isTrue()
        }
    }

    @Nested
    inner class MatchFound {

        private var matchId by Delegates.notNull<Long>()

        private val oldName = "old name"
        private val newName = "new name"

        @BeforeEach
        fun setUp() = runTest {
            matchId = fixture.createNamedMatch(matchName = oldName).id
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `return renamed match`(rebootApplication: Boolean) = runTest {
            if (rebootApplication) fixture.rebootApplication()

            val result = renameMatch(matchId, newName)

            assertMatchResponse(fixture, result) { match ->
                assertThat(match.name).isEqualTo(newName)
            }
        }

        @Test
        fun `flow is updated`() = runTest {
            fixture.getMatchFlow(matchId).test {
                renameMatch(matchId, newName)

                assertThat(awaitItem()!!.name).isEqualTo(oldName)
                assertThat(awaitItem()!!.name).isEqualTo(newName)
            }
        }
    }
}
