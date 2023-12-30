package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
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
        fun `team is added`(cacheMiss: Boolean) = runTest {
            if (cacheMiss) fixture.allDataInPersistence()
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

                newMatch.teams.size shouldBe oldMatch.teams.size.inc()
                newMatch.teams.last().name shouldBe "team name"
            }
        }

        private suspend fun assertTeamWasAdded(
            result: Result<MatchResponse>,
            addTeamRequest: AddTeamRequest,
        ) {
            result.isSuccess.shouldBeTrue()
            assertMatchResponse(fixture, result) { match ->
                match.teams.last().name shouldBe addTeamRequest.name
            }
        }
    }
}
