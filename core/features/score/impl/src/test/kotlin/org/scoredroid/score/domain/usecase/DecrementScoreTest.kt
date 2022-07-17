package org.scoredroid.score.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.FakeMatchLocalDataSource

@ExperimentalCoroutinesApi
class DecrementScoreTest {

    private val localDataSource = FakeMatchLocalDataSource()
    private val repository = MatchRepository(localDataSource)
    private val decrementScore = DecrementScore(repository)
    private val incrementScore = IncrementScore(repository)

    @Nested
    inner class NoMatchesCreated {
        @Test
        fun `return match not found error`() = runTest {
            val result = decrementScore(matchId = 0L, teamAt = 0)

            assertThrows<UpdateScoreError.MatchNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class NonExistingMatch {
        @BeforeEach
        fun setUp() = runTest {
            repository.createMatch(CreateMatchRepositoryRequest(emptyList()))
        }

        @Test
        fun `return match not found error`() = runTest {
            val result = decrementScore(matchId = 3L, teamAt = 0)

            assertThrows<UpdateScoreError.MatchNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class NoTeamCreated {
        @BeforeEach
        fun setUp() = runTest {
            repository.createMatch(CreateMatchRepositoryRequest(emptyList()))
        }

        @Test
        fun `return team not found error`() = runTest {
            val result = decrementScore(matchId = 0L, teamAt = 0)

            assertThrows<UpdateScoreError.TeamNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class NonExistingTeam {
        private lateinit var match: MatchResponse

        @BeforeEach
        fun setUp() = runTest {
            match = repository.createMatch(CreateMatchRepositoryRequest(emptyList()))
            repository.addTeam(matchId = match.id, AddTeamRepositoryRequest("team name"))
        }

        @Test
        fun `index overflow, return team not found error`() = runTest {
            val result = decrementScore(matchId = match.id, teamAt = 2)

            assertThrows<UpdateScoreError.TeamNotFound> { result.getOrThrow() }
        }

        @Test
        fun `index underflow, return team not found error`() = runTest {
            val result = decrementScore(matchId = match.id, teamAt = -1)

            assertThrows<UpdateScoreError.TeamNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class ExistingTeam {
        private lateinit var match: MatchResponse

        @BeforeEach
        fun setUp() = runTest {
            match = repository.createMatch(CreateMatchRepositoryRequest(emptyList()))
            repository.addTeam(matchId = match.id, AddTeamRepositoryRequest("team name"))
            incrementScore(matchId = match.id, teamAt = 0, increment = 5)
        }

        @Nested
        inner class DecrementUpToCurrentScore {

            @Test
            fun `score is decremented`() = runTest {
                val result = decrementScore(matchId = match.id, teamAt = 0)

                assertThat(result.getOrThrow().teams[0].score).isEqualTo(4)
            }

            @Test
            fun `score is decremented by specific value`() = runTest {
                val result = decrementScore(matchId = match.id, teamAt = 0, decrement = 5)

                assertThat(result.getOrThrow().teams[0].score).isEqualTo(0)
            }
        }

        @Nested
        inner class DecrementMoreThanCurrentScore {

            @Test
            fun `score is decremented down to zero`() = runTest {
                val result = decrementScore(matchId = match.id, teamAt = 0, decrement = 10)

                assertThat(result.getOrThrow().teams[0].score).isEqualTo(0)
            }
        }
    }
}