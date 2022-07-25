package org.scoredroid.score.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
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

            assertThat(result.getOrThrow().teams).isEmpty()
        }
    }

    @Nested
    inner class ExistingTeam {
        private lateinit var match: Match

        @BeforeEach
        fun setUp() = runTest {
            match = fixture.createEmptyMatch()
            fixture.repository.addTeam(matchId = match.id, AddTeamRepositoryRequest("team 1"))
            fixture.repository.addTeam(matchId = match.id, AddTeamRepositoryRequest("team 2"))
            fixture.repository.addTeam(matchId = match.id, AddTeamRepositoryRequest("team 3"))
            incrementScore(matchId = match.id, teamAt = 0, increment = 5)
            incrementScore(matchId = match.id, teamAt = 1, increment = 4)
            incrementScore(matchId = match.id, teamAt = 2, increment = 7)
        }

        @Test
        fun `all team scores are reseted`() = runTest {
            val result = resetScore(match.id)

            assertEmptyScore(result = result)
        }

        private fun assertEmptyScore(result: Result<MatchResponse>) {
            result.getOrThrow().teams.forEach {
                assertThat(it.score).isEqualTo(0)
            }
        }
    }
}
