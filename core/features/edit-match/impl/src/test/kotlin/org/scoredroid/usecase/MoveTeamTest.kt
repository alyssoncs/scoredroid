package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.test.assertions.dataaccess.repository.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
class MoveTeamTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val moveTeam = MoveTeam(fixture.repository)

    @Nested
    inner class NoMatch {

        @Test
        fun `return match not found error`() = runTest {
            val nonExistingMatchId = 0L

            val result = moveTeam(matchId = nonExistingMatchId, teamAt = 0, moveTo = 0)

            assertThrows<UpdateTeamError.MatchNotFound> { result.getOrThrow() }
        }
    }

    @Nested
    inner class HasMatch {

        private var matchId by Delegates.notNull<Long>()

        @BeforeEach
        fun setUp() = runTest {
            matchId = fixture.createEmptyMatch().id
        }

        @Nested
        inner class NoTeam {
            @Test
            fun `return team not found error`() = runTest {
                val nonExistingTeamIndex = 0

                val result = moveTeam(matchId = matchId, teamAt = nonExistingTeamIndex, moveTo = 0)

                assertThrows<UpdateTeamError.TeamNotFound> { result.getOrThrow() }
            }
        }

        @Nested
        inner class TeamExist {
            @BeforeEach
            fun setUp() = runTest {
                fixture.addTeamsToExistingMatch(matchId, "t0", "t1", "t2")
            }

            @ParameterizedTest
            @ValueSource(ints = [-2, -1, 4, 5])
            fun `teamAt out of bounds, return team not found error`(teamAt: Int) = runTest {
                val result = moveTeam(matchId = matchId, teamAt = teamAt, moveTo = 0)

                assertThrows<UpdateTeamError.TeamNotFound> { result.getOrThrow() }
            }

            @ParameterizedTest
            @ValueSource(booleans = [true, false])
            fun `moveTo equals to teamAt, keep the teams in the same order`(rebootApplication: Boolean) = runTest {
                if (rebootApplication) fixture.rebootApplication()

                val matchResult = moveTeam(matchId = matchId, teamAt = 1, moveTo = 1)

                assertTeamOrder(matchResult, "t0, t1, t2")
            }

            @ParameterizedTest
            @ValueSource(booleans = [true, false])
            fun `moveTo within bounds, move the team to the correct position`(rebootApplication: Boolean) = runTest {
                if (rebootApplication) fixture.rebootApplication()

                val matchResult = moveTeam(matchId = matchId, teamAt = 0, moveTo = 1)

                assertTeamOrder(matchResult, "t1, t0, t2")
            }

            @ParameterizedTest
            @ValueSource(booleans = [true, false])
            fun `moveTo underflows, move the team to the initial position`(rebootApplication: Boolean) = runTest {
                if (rebootApplication) fixture.rebootApplication()

                val matchResult = moveTeam(matchId = matchId, teamAt = 2, moveTo = -2)

                assertTeamOrder(matchResult, "t2, t0, t1")
            }

            @ParameterizedTest
            @ValueSource(booleans = [true, false])
            fun `moveTo overflows, move the team to the last position`(rebootApplication: Boolean) = runTest {
                if (rebootApplication) fixture.rebootApplication()

                val matchResult = moveTeam(matchId = matchId, teamAt = 1, moveTo = 6)

                assertTeamOrder(matchResult, "t0, t2, t1")
            }

            @Test
            fun `flow is updated`() = runTest {
                fixture.getMatchFlow(matchId).test {
                    moveTeam(matchId = matchId, teamAt = 1, moveTo = 0)

                    val oldMatch = awaitItem()!!
                    val newMatch = awaitItem()!!

                    assertTeamOrder(oldMatch, "t0, t1, t2")
                    assertTeamOrder(newMatch, "t1, t0, t2")
                }
            }

            private suspend fun assertTeamOrder(matchResult: Result<MatchResponse>, expectedOrder: String) {
                assertMatchResponse(fixture, matchResult) { match ->
                    assertTeamOrder(match, expectedOrder)
                }
            }

            private fun assertTeamOrder(match: MatchResponse, expectedOrder: String) {
                match.teams.joinToString { it.name } shouldBe expectedOrder
            }
        }
    }
}
