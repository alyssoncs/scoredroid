package org.scoredroid.score.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.test.assertions.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

typealias UpdateScore = suspend (matchId: Long, teamAt: Int, updateAmount: Int) -> Result<MatchResponse>

@ExperimentalCoroutinesApi
abstract class UpdateScoreTest {

    abstract fun updateStrategy(currentScore: Int, updateAmount: Int): Int
    abstract fun createUpdateScoreUseCase(repository: MatchRepository): UpdateScore

    lateinit var updateScore: UpdateScore
    protected val fixture = MatchRepositoryFixtureFactory.create()
    protected val incrementScore = IncrementScore(ScoreUpdater(fixture.repository))


    @BeforeEach
    fun setUp() {
        updateScore = createUpdateScoreUseCase(fixture.repository)
    }


    @Nested
    inner class NoMatchesCreated {
        @Test
        fun `return match not found error`() = runTest {
            val result = updateScore(0L, 0, 1)

            assertThrows<UpdateScoreError.MatchNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class NonExistingMatch {
        @BeforeEach
        fun setUp() = runTest {
            fixture.createEmptyMatch()
        }

        @Test
        fun `return match not found error`() = runTest {
            val result = updateScore(3L, 0, 1)

            assertThrows<UpdateScoreError.MatchNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class NoTeamCreated {
        @BeforeEach
        fun setUp() = runTest {
            fixture.createEmptyMatch()
        }

        @Test
        fun `return team not found error`() = runTest {
            val result = updateScore(0L, 0, 1)

            assertThrows<UpdateScoreError.TeamNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class NonExistingTeam {
        private lateinit var match: Match

        @BeforeEach
        fun setUp() = runTest {
            match = fixture.createMatchWithTeams("team name")
        }

        @Test
        fun `index overflow, return team not found error`() = runTest {
            val result = updateScore(match.id, 2, 1)

            assertThrows<UpdateScoreError.TeamNotFound> { result.getOrThrow() }
        }

        @Test
        fun `index underflow, return team not found error`() = runTest {
            val result = updateScore(match.id, -1, 1)

            assertThrows<UpdateScoreError.TeamNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class ExistingTeam {
        private lateinit var match: Match

        @BeforeEach
        fun setUp() = runTest {
            match = fixture.createMatchWithTeams("team name")
            incrementScore(matchId = match.id, teamAt = 0, increment = 5)
        }

        @Nested
        inner class UpdateCurrentScore {

            @ParameterizedTest
            @ValueSource(ints = [1, 2, 3])
            fun `score is updated by specific amount`(updateAmount: Int) = runTest {
                val result = updateScore(match.id, 0, updateAmount)

                assertScore(result, updateAmount)
            }

            private suspend fun assertScore(result: Result<MatchResponse>, updateAmount: Int) {
                assertMatchResponse(fixture, result) { match ->
                    assertThat(match.teams[0].score)
                        .isEqualTo(updateStrategy(currentScore = 5, updateAmount = updateAmount))
                }
            }
        }
    }
}
