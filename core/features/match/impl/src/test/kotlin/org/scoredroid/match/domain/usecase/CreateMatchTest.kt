package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.assertions.assertMatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import org.scoredroid.match.domain.request.CreateMatchRequestOptions

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
                assertThat(match.id).isEqualTo(5)
            }
        }

        @Test
        fun `empty match name`() = runTest {
            val matchResponse = createMatch()

            assertMatchResponse(fixture, matchResponse) { match ->
                assertThat(match.name).isEmpty()
            }
        }

        @Test
        fun `no teams are created`() = runTest {
            val matchResponse = createMatch()

            assertMatchResponse(fixture, matchResponse) { match ->
                assertThat(match.teams).isEmpty()
            }
        }

        @Test
        fun `can get a flow`() = runTest {
            val matchResponse = createMatch()

            assertThat(fixture.getMatchFlow(matchResponse.id).first()).isNotNull()
        }
    }

    @Nested
    inner class CustomParam {

        @Test
        fun `custom match name`() = runTest {
            val matchResponse = createMatch(CreateMatchRequestOptions(matchName = "match name"))

            assertMatchResponse(fixture, matchResponse) { match ->
                assertThat(match.name).isEqualTo("match name")
            }
        }

        @Test
        fun `initial teams`() = runTest {
            val matchResponse = createMatch(
                CreateMatchRequestOptions(
                    teams = listOf(
                        CreateMatchRequestOptions.InitialTeamRequest(name = "team 1"),
                        CreateMatchRequestOptions.InitialTeamRequest(name = "team 2"),
                    )
                )
            )

            assertMatchResponse(fixture, matchResponse) { match ->
                assertThat(match.teams).hasSize(2)
                assertThat(match.teams.first().name).isEqualTo("team 1")
                assertThat(match.teams.last().name).isEqualTo("team 2")
            }
        }
    }
}
