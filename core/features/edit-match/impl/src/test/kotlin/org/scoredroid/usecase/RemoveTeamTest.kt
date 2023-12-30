package org.scoredroid.usecase

import app.cash.turbine.test
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.test.assertions.dataaccess.repository.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

@ExperimentalCoroutinesApi
class RemoveTeamTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val removeTeam = RemoveTeam(fixture.repository)
    private lateinit var match: Match

    @BeforeEach
    fun setUp() = runTest {
        match = fixture.createMatchWithTeams("team 1", "team 2")
    }

    @Nested
    inner class ValidIndex {
        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `remove first team`(cacheMiss: Boolean) = runTest {
            if (cacheMiss) fixture.allDataInPersistence()

            val matchResult = removeTeam(match.id, 0)

            assertMatchResponse(fixture, matchResult) { match ->
                match.teams shouldHaveSize 1
                match.teams.first().name shouldBe "team 2"
            }
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `remove last team`(cacheMiss: Boolean) = runTest {
            if (cacheMiss) fixture.allDataInPersistence()

            val matchResult = removeTeam(match.id, 1)

            assertMatchResponse(fixture, matchResult) { match ->
                match.teams shouldHaveSize 1
                match.teams.first().name shouldBe "team 1"
            }
        }

        @ParameterizedTest
        @ValueSource(booleans = [true, false])
        fun `remove all teams`(cacheMiss: Boolean) = runTest {
            if (cacheMiss) fixture.allDataInPersistence()

            removeTeam(match.id, 0).getOrThrow()

            val matchResult = removeTeam(match.id, 0)

            assertMatchResponse(fixture, matchResult) { match ->
                match.teams.shouldBeEmpty()
            }
        }

        @Test
        fun `flow is updated`() = runTest {
            fixture.getMatchFlow(match.id).test {
                removeTeam(match.id, 0)

                val oldMatch = awaitItem()!!
                val newMatch = awaitItem()!!

                newMatch.teams.size shouldBe oldMatch.teams.size.dec()
            }
        }
    }

    @Nested
    inner class InvalidIndex {
        @ParameterizedTest
        @ValueSource(ints = [-1, 2, 3])
        fun `do nothing`(index: Int) = runTest {
            val matchResult = removeTeam(match.id, index)

            assertMatchResponse(fixture, matchResult) { match ->
                match.teams shouldHaveSize 2
            }
        }
    }
}
