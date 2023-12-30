package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.TeamRequest
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

class GetMatchesTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val getMatches = GetMatchesFlow(fixture.repository)

    @Nested
    inner class NoMatchExists {

        @Test
        fun `should return flow with empty list`() = runTest {
            val matchesFlow = getMatches()

            matchesFlow.test {
                awaitItem().isEmpty()
            }
        }
    }

    @Nested
    inner class MatchExists {

        private val firstMatchRequest = CreateMatchRepositoryRequest(
            name = "first match",
            teams = listOf(
                TeamRequest("team a"),
                TeamRequest("team b"),
            ),
        )

        private val secondMatchRequest = CreateMatchRepositoryRequest(
            name = "second match",
            teams = listOf(
                TeamRequest("team 1"),
            ),
        )

        private val expectedSecondMatchResponse = MatchResponse(
            id = 1L,
            name = "second match",
            teams = listOf(
                TeamResponse("team 1", 0),
            ),
        )

        private val expectedFirstMatchResponse = MatchResponse(
            id = 0L,
            name = "first match",
            teams = listOf(
                TeamResponse("team a", 0),
                TeamResponse("team b", 0),
            ),
        )

        @Nested
        inner class AllMatchesInTransientStorage {

            @BeforeEach
            fun setUp() = runTest {
                fixture.createMatch(firstMatchRequest)
                fixture.createMatch(secondMatchRequest)
            }

            @Test
            fun `should return flow with matches in reverse order`() = runTest {
                val matchesFow = getMatches()

                matchesFow.test {
                    val matches = awaitItem()
                    matches shouldHaveSize 2
                    matches[0] shouldBe expectedSecondMatchResponse
                    matches[1] shouldBe expectedFirstMatchResponse
                }
            }
        }

        @Nested
        inner class AllMatchesInPersistentStorage {

            @BeforeEach
            fun setUp() = runTest {
                fixture.createMatch(firstMatchRequest)
                fixture.createMatch(secondMatchRequest)
                fixture.allDataInPersistence()
            }

            @Test
            fun `should return flow with matches in reverse order`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    val matches = awaitItem()
                    matches shouldHaveSize 2
                    matches[0] shouldBe expectedSecondMatchResponse
                    matches[1] shouldBe expectedFirstMatchResponse
                }
            }
        }

        @Nested
        inner class SomeMatchesInPersistentStorageAndOthersInTransientStorage {

            @BeforeEach
            fun setUp() = runTest {
                fixture.createMatch(firstMatchRequest)
                fixture.allDataInPersistence()
                fixture.createMatch(secondMatchRequest)
            }

            @Test
            fun `should return flow with matches in reverse order`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    val matches = awaitItem()
                    matches shouldHaveSize 2
                    matches[0] shouldBe expectedSecondMatchResponse
                    matches[1] shouldBe expectedFirstMatchResponse
                }
            }
        }

        @Nested
        inner class ColdStart {

            private lateinit var getMatchesWithColdStart: GetMatchesFlow

            @BeforeEach
            fun setUp() = runTest {
                fixture.createMatch(firstMatchRequest)
                fixture.createMatch(secondMatchRequest)
                getMatchesWithColdStart = GetMatchesFlow(fixture.coldStart().repository)
            }

            @Test
            fun `should return flow with matches in reverse order`() = runTest {
                val matchesFlow = getMatchesWithColdStart()

                matchesFlow.test {
                    val matches = awaitItem()
                    matches shouldHaveSize 2
                    matches[0] shouldBe expectedSecondMatchResponse
                    matches[1] shouldBe expectedFirstMatchResponse
                }
            }
        }

        @Nested
        inner class Updates {

            @BeforeEach
            fun setUp() = runTest {
                fixture.createMatch(firstMatchRequest)
                fixture.createMatch(secondMatchRequest)
            }

            @Test
            fun `new match updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.createNamedMatch("third match")

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 3
                    newMatches[0].name shouldBe "third match"
                }
            }

            @Test
            fun `new team updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.addTeamsToExistingMatch(1L, "team 2")

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 2
                    newMatches.first().teams shouldHaveSize 2
                    newMatches.first().teams.last().name shouldBe "team 2"
                }
            }

            @Test
            fun `team removal updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.removeTeam(1L, 0)

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 2
                    newMatches.first().teams.shouldBeEmpty()
                }
            }

            @Test
            fun `score update updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.bumpScore(1L, 0)

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 2
                    newMatches.first().teams.first().score shouldBe 1
                }
            }

            @Test
            fun `match rename updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.renameMatch(1L, "new name")

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 2
                    newMatches.first().name shouldBe "new name"
                }
            }

            @Test
            fun `team movement updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.makeFirstTeam(0L, 1)

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 2
                    newMatches.last().teams.first().name shouldBe "team b"
                }
            }

            @Test
            fun `team rename updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.renameTeam(1L, 0, "new name")

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 2
                    newMatches.first().teams.first().name shouldBe "new name"
                }
            }

            @Test
            fun `match removal updates the flow`() = runTest {
                val matchesFlow = getMatches()

                matchesFlow.test {
                    awaitItem() shouldHaveSize 2

                    fixture.removeMatch(1L)

                    val newMatches = awaitItem()
                    newMatches shouldHaveSize 1
                    newMatches.first().name shouldBe "first match"
                }
            }
        }
    }
}
