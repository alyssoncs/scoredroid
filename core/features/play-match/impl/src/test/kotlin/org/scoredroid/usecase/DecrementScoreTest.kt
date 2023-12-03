package org.scoredroid.usecase

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.test.assertions.dataaccess.repository.assertMatchResponse

@ExperimentalCoroutinesApi
class DecrementScoreTest : UpdateScoreTest() {
    override fun updateStrategy(currentScore: Int, updateAmount: Int) = currentScore - updateAmount

    override fun createUpdateScoreUseCase(repository: MatchRepository): UpdateScore {
        return DecrementScore(repository)::invoke
    }

    @Nested
    inner class DecrementMoreThanCurrentScore {

        private lateinit var match: Match

        @BeforeEach
        fun setUp() = runTest {
            match = fixture.createMatchWithTeams("team name")
            incrementScore(matchId = match.id, teamAt = 0, increment = 5)
        }

        @Test
        fun `score is decremented down to zero`() = runTest {
            val result = updateScore(match.id, 0, 10)

            assertMatchResponse(fixture, result) { match ->
                match.teams[0].score shouldBe 0
            }
        }
    }
}
