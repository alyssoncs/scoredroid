package org.scoredroid.teams.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.dataaccess.requestmodel.AddTeamRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryTestFactory
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
class MoveTeamTest {

    private val fixture = MatchRepositoryTestFactory.create()
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
            matchId = fixture.repository.createMatch(CreateMatchRepositoryRequest()).id
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
            private lateinit var match: Match
            @BeforeEach
            fun setUp() = runTest {
                fixture.repository.addTeam(matchId, AddTeamRepositoryRequest("t0"))
                fixture.repository.addTeam(matchId, AddTeamRepositoryRequest("t1"))
                match = fixture.repository.addTeam(matchId, AddTeamRepositoryRequest("t2")).getOrThrow()
            }

            @ParameterizedTest
            @ValueSource(ints = [-2, -1, 4, 5])
            fun `teamAt out of bounds, return team not found error`(teamAt: Int) = runTest {
                val result = moveTeam(matchId = matchId, teamAt = teamAt, moveTo = 0)

                assertThrows<UpdateTeamError.TeamNotFound> { result.getOrThrow() }
            }

            @Test
            fun `moveTo equals to teamAt, keep the teams in the same order`() = runTest {
                val teams = moveTeam(matchId = matchId, teamAt = 1, moveTo = 1).getOrThrow().teams

                assertTeamOrder(teams, "t0, t1, t2")
            }

            @Test
            fun `moveTo within bounds, move the team to the correct position`() = runTest {
                val teams = moveTeam(matchId = matchId, teamAt = 0, moveTo = 1).getOrThrow().teams

                assertTeamOrder(teams, "t1, t0, t2")
            }

            @Test
            fun `moveTo underflows, move the team to the initial position`() = runTest {
                val teams = moveTeam(matchId = matchId, teamAt = 2, moveTo = -2).getOrThrow().teams

                assertTeamOrder(teams, "t2, t0, t1")
            }

            @Test
            fun `moveTo overflows, move the team to the last position`() = runTest {
                val teams = moveTeam(matchId = matchId, teamAt = 1, moveTo = 6).getOrThrow().teams

                assertTeamOrder(teams, "t0, t2, t1")
            }

            private fun assertTeamOrder(teams: List<TeamResponse>, order: String) {
                assertThat(teams.joinToString { it.name }).isEqualTo(order)
            }
        }
    }
}
