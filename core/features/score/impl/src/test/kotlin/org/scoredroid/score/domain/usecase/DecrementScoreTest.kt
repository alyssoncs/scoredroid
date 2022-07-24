package org.scoredroid.score.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest

@ExperimentalCoroutinesApi
class DecrementScoreTest : UpdateScoreTest() {
    override fun updateStrategy(currentScore: Int, updateAmount: Int) = currentScore - updateAmount

    override fun createUpdateScoreUseCase(repository: MatchRepository): UpdateScore {
        return DecrementScore(ScoreUpdater(repository))::invoke
    }

    @Nested
    inner class DecrementMoreThanCurrentScore {

        private lateinit var match: Match

        @BeforeEach
        fun setUp() = runTest {
            match = repository.createMatch(CreateMatchRepositoryRequest())
            repository.addTeam(matchId = match.id, AddTeamRepositoryRequest("team name"))
            incrementScore(matchId = match.id, teamAt = 0, increment = 5)
        }

        @Test
        fun `score is decremented down to zero`() = runTest {
            val result = updateScore(match.id, 0, 10)

            assertThat(result.getOrThrow().teams[0].score).isEqualTo(0)
        }
    }
}
