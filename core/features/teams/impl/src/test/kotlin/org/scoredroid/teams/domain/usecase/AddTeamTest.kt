package org.scoredroid.teams.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.test.assertions.dataaccess.repository.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import org.scoredroid.teams.domain.request.AddTeamRequest

@ExperimentalCoroutinesApi
class AddTeamTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val addTeam = AddTeam(fixture.repository)

    @Nested
    inner class MatchNotFound {

        @Test
        fun `return error`() = runTest {
            val matchId = 1L

            val result = addTeam(matchId, AddTeamRequest(name = "irrelevant"))

            assertTrue(result.isFailure)
        }
    }

    @Nested
    inner class MatchExists {
        private val matchId = 7L

        @BeforeEach
        internal fun setUp() = runTest {
            repeat(matchId.inc().toInt()) {
                fixture.createEmptyMatch()
            }
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `team is added`(rebootApplication: Boolean) = runTest {
            if (rebootApplication) fixture.rebootApplication()
            val addTeamRequest = AddTeamRequest(name = "team name")

            val result = addTeam(matchId, addTeamRequest)

            assertTeamWasAdded(result, addTeamRequest)
        }

        @Test
        fun `flow is updated`() = runTest {
            fixture.getMatchFlow(matchId).test {
                addTeam(matchId, AddTeamRequest(name = "team name"))

                val oldMatch = awaitItem()!!
                val newMatch = awaitItem()!!

                assertThat(newMatch.teams.size).isEqualTo(oldMatch.teams.size.inc())
                assertThat(newMatch.teams.last().name).isEqualTo("team name")
            }
        }

        private suspend fun assertTeamWasAdded(
            result: Result<MatchResponse>,
            addTeamRequest: AddTeamRequest,
        ) {
            assertThat(result.isSuccess).isTrue()
            assertMatchResponse(fixture, result) { match ->
                assertThat(match.teams.last().name).isEqualTo(addTeamRequest.name)
            }
        }
    }
}
