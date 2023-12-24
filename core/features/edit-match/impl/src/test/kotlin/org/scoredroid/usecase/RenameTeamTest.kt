package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.test.assertions.dataaccess.repository.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

@ExperimentalCoroutinesApi
class RenameTeamTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val renameTeam = RenameTeam(fixture.repository)

    @Nested
    inner class MatchNotFound {

        @Test
        fun `return error`() = runTest {
            val matchId = 1L

            val result = renameTeam(matchId, 0, "irrelevant")

            assertTrue(result.isFailure)
        }
    }

    @Nested
    inner class MatchExists {
        private val matchId = 7L
        private val numberOfMatches get() = matchId.inc().toInt()
        private val numberOfTeams = 4

        @BeforeEach
        internal fun setUp() = runTest {
            repeat(numberOfMatches - 1) {
                fixture.createEmptyMatch()
            }

            val teamNames = Array(numberOfTeams) { "generic name" }
            fixture.createMatchWithTeams(*teamNames)
        }

        @Nested
        inner class TeamExists {

            @TestFactory
            fun `team is renamed`(): List<DynamicTest> {
                val renameWithoutRebooting = List(numberOfTeams) { it to false }
                val renameRebooting = List(numberOfTeams) { it to true }

                return (renameWithoutRebooting + renameRebooting).map {
                    dynamicTest("renaming team #${it.first} ${if (!it.second) "without" else ""} rebooting") {
                        runTest {
                            if (it.second) fixture.rebootApplication()
                            val expectedName =
                                if (it.second) "rebooting name" else "non rebooting name"

                            val result = renameTeam(matchId, it.first, expectedName)

                            assertTeamWasRenamed(result, it.first, expectedName)
                        }
                    }
                }
            }

            @Test
            fun `flow is updated`() = runTest {
                fixture.getMatchFlow(matchId).test {
                    renameTeam(matchId, 1, "team name")

                    val oldMatch = awaitItem()!!
                    val newMatch = awaitItem()!!

                    newMatch.teams shouldBeSameSizeAs oldMatch.teams
                    newMatch.teams[1].name shouldBe "team name"
                }
            }

            private suspend fun assertTeamWasRenamed(
                result: Result<MatchResponse>,
                teamAt: Int,
                expectedName: String,
            ) {
                result.isSuccess.shouldBeTrue()
                assertMatchResponse(fixture, result) { match ->
                    match.teams[teamAt].name shouldBe expectedName
                }
            }
        }

        @Nested
        inner class TeamDoesNotExists {

            @Test
            fun `do nothing`() = runTest {
                val result = renameTeam(matchId, numberOfTeams, "irrelevant")

                assertTeamWasNotRenamed(result)
            }

            @Test
            fun `flow is not updated`() = runTest {
                fixture.getMatchFlow(matchId).test {
                    renameTeam(matchId, numberOfTeams, "irrelevant")

                    awaitItem()
                    ensureAllEventsConsumed()
                }
            }

            private suspend fun assertTeamWasNotRenamed(
                result: Result<MatchResponse>,
            ) {
                result.isSuccess.shouldBeTrue()

                assertMatchResponse(fixture, result) { match ->
                    match.teams.all { it.name == "generic name" }.shouldBeTrue()
                }
            }
        }
    }
}
