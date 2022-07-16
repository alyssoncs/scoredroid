package org.scoredroid.teams.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.FakeMatchLocalDataSource
import org.scoredroid.teams.domain.request.AddTeamRequest

@ExperimentalCoroutinesApi
class AddTeamTest {

    private val fakeLocalDataSource = FakeMatchLocalDataSource()
    private val repository = MatchRepository(fakeLocalDataSource)
    private val addTeam = AddTeam(repository)

    @Nested
    inner class MatchNotFound {

        @Test
        fun `return error`() = runTest {
            val matchId = 1L

            val result = addTeam(matchId, AddTeamRequest(name = "irrelevant"))

            assertError(result)
        }

        private fun assertError(result: Result<MatchResponse>) {
            assertTrue(result.isFailure)
        }
    }

    @Nested
    inner class MatchExists {
        private val matchId = 7L

        @BeforeEach
        internal fun setUp() = runTest {
            repeat(matchId.inc().toInt()) {
                repository.createMatch(CreateMatchRepositoryRequest(emptyList()))
            }
        }

        @Test
        fun `team is added`() = runTest {
            val addTeamRequest = AddTeamRequest(name = "team name")
            val result = addTeam(matchId, addTeamRequest)


            assertContainsTeamCorrespondingWith(result, addTeamRequest)
        }

        private fun assertContainsTeamCorrespondingWith(
            result: Result<MatchResponse>,
            addTeamRequest: AddTeamRequest,
        ) {
            val team = result.getOrThrow().teams.last()

            assertThat(team.name).isEqualTo(addTeamRequest.name)
        }
    }
}
