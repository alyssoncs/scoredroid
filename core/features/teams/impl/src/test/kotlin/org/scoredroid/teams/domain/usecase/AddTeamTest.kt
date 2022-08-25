package org.scoredroid.teams.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.data.response.MatchResponse
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

        @Test
        fun `team is added`() = runTest {
            val addTeamRequest = AddTeamRequest(name = "team name")

            val result = addTeam(matchId, addTeamRequest)

            assertTeamWasAdded(matchId, result, addTeamRequest)
        }

        private suspend fun assertTeamWasAdded(
            matchId: Long,
            result: Result<MatchResponse>,
            addTeamRequest: AddTeamRequest,
        ) {
            assertThat(result.isSuccess).isTrue()

            val resultTeam = result.getOrThrow().teams.last()
            val persistedMatch = fixture.repository.getMatch(matchId)

            assertThat(persistedMatch).isNotNull()
            assertThat(resultTeam.name).isEqualTo(addTeamRequest.name)
            assertThat(resultTeam.name).isEqualTo(persistedMatch!!.teams.last().name)
        }
    }
}
