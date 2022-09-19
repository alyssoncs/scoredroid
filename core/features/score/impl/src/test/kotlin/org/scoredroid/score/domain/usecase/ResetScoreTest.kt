package org.scoredroid.score.domain.usecase

import app.cash.turbine.test
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
import org.scoredroid.infra.test.assertions.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory


@ExperimentalCoroutinesApi
class ResetScoreTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val scoreUpdater = ScoreUpdater(fixture.repository)
    private val incrementScore = IncrementScore(scoreUpdater)
    private val resetScore = ResetScore(scoreUpdater)

    @Nested
    inner class NoMatchesCreated {
        @Test
        fun `return match not found error`() = runTest {
            val result = resetScore(0L)

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
            val result = resetScore(3L)

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
        fun `return empty response`() = runTest {
            val result = resetScore(0L)

            assertMatchResponse(fixture, result) { match ->
                assertThat(match.teams).isEmpty()
            }
        }
    }

    @Nested
    inner class ExistingTeam {
        private lateinit var match: Match

        @BeforeEach
        fun setUp() = runTest {
            match = fixture.createMatchWithTeams("team 1", "team 2", "team 3")
            incrementScore(matchId = match.id, teamAt = 0, increment = 5)
            incrementScore(matchId = match.id, teamAt = 1, increment = 4)
            incrementScore(matchId = match.id, teamAt = 2, increment = 7)
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `all teams scores are reseted`(rebootApplication: Boolean) = runTest {
            if (rebootApplication) fixture.rebootApplication()

            val result = resetScore(match.id)

            assertEmptyScore(result = result)
        }

        @Test
        fun `flow is updated`() = runTest {
            fixture.getMatchFlow(match.id).test {
                resetScore(match.id)

                assertThat(scoreSumOf(awaitItem())).isNotEqualTo(0)
                assertThat(scoreSumOf(awaitItem())).isEqualTo(0)
            }
        }

        private suspend fun assertEmptyScore(result: Result<MatchResponse>) {
            assertMatchResponse(fixture, result) { match ->
                assertThat(match.teams).hasSize(3)
                match.teams.forEach {
                    assertThat(it.score).isEqualTo(0)
                }
            }
        }

        private fun scoreSumOf(match: MatchResponse): Int {
            return match.teams.sumOf { it.score }
        }
    }
}
