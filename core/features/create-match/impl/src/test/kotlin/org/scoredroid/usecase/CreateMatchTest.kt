package org.scoredroid.usecase

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.assertions.dataaccess.repository.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

@ExperimentalCoroutinesApi
class CreateMatchTest {

    private val oddIds: (currentId: Long) -> Long = { currentId -> currentId + 2 }
    private val fixture = MatchRepositoryFixtureFactory.create(
        initialMatchId = 1L,
        matchIdStrategy = oddIds,
    )
    private val createMatch = CreateMatch(fixture.repository)

    @Nested
    inner class DefaultParam {

        @Test
        fun `id provided by persistent local data source`() = runTest {
            repeat(2) {
                fixture.createEmptyMatch()
            }

            val matchResponse = createMatch()

            assertMatchResponse(fixture, matchResponse) { match ->
                match.id shouldBe 5
            }
        }

        @Test
        fun `empty match name`() = runTest {
            val matchResponse = createMatch()

            assertMatchResponse(fixture, matchResponse) { match ->
                match.name.shouldBeEmpty()
            }
        }

        @Test
        fun `no teams are created`() = runTest {
            val matchResponse = createMatch()

            assertMatchResponse(fixture, matchResponse) { match ->
                match.teams.shouldBeEmpty()
            }
        }

        @Test
        fun `can get a flow`() = runTest {
            val matchResponse = createMatch()

            fixture.getMatchFlow(matchResponse.id).first().shouldNotBeNull()
        }
    }

    @Nested
    inner class CustomParam {

        @Test
        fun `custom match name`() = runTest {
            val matchResponse = createMatch(CreateMatchRequestOptions(matchName = "match name"))

            assertMatchResponse(fixture, matchResponse) { match ->
                match.name shouldBe "match name"
            }
        }

        @Test
        fun `initial teams`() = runTest {
            val matchResponse = createMatch(
                CreateMatchRequestOptions(
                    teams = listOf(
                        CreateMatchRequestOptions.InitialTeamRequest(name = "team 1"),
                        CreateMatchRequestOptions.InitialTeamRequest(name = "team 2"),
                    ),
                ),
            )

            assertMatchResponse(fixture, matchResponse) { match ->
                match.teams shouldHaveSize 2
                match.teams.first().name shouldBe "team 1"
                match.teams.last().name shouldBe "team 2"
            }
        }
    }
}
