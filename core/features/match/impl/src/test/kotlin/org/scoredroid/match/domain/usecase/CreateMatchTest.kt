package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.domain.entities.Match
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import org.scoredroid.match.domain.request.CreateMatchRequestOptions

@ExperimentalCoroutinesApi
class CreateMatchTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val createMatch = CreateMatch(fixture.repository)

    @Nested
    inner class DefaultParam {

        @Test
        fun `id provided by local data source`() = runTest {
            repeat(2) {
                fixture.createEmptyMatch()
            }

            val match = createMatch()

            assertThat(match.id).isEqualTo(2)
            assertThat(getPersistedMatch(match.id)).isNotNull()
        }

        @Test
        fun `empty match name`() = runTest {
            val match = createMatch()

            assertThat(match.name).isEmpty()
            assertThat(getPersistedMatch(match.id)!!.name).isEmpty()
        }

        @Test
        fun `no teams are created`() = runTest {
            val match = createMatch()

            assertThat(match.teams).isEmpty()
            assertThat(getPersistedMatch(match.id)!!.teams).isEmpty()
        }
    }

    @Nested
    inner class CustomParam {

        @Test
        fun `custom match name`() = runTest {
            val match = createMatch(CreateMatchRequestOptions(matchName = "match name"))

            assertThat(match.name).isEqualTo("match name")
            assertThat(getPersistedMatch(match.id)!!.name).isEqualTo("match name")
        }

        @Test
        fun `initial teams`() = runTest {
            val match = createMatch(
                CreateMatchRequestOptions(
                    teams = listOf(
                        CreateMatchRequestOptions.InitialTeamRequest(name = "team 1"),
                        CreateMatchRequestOptions.InitialTeamRequest(name = "team 2"),
                    )
                )
            )

            assertThat(match.teams).hasSize(2)
            assertThat(match.teams.first().name).isEqualTo("team 1")
            assertThat(match.teams.last().name).isEqualTo("team 2")

            val persistedMatch = getPersistedMatch(match.id)!!
            assertThat(persistedMatch.teams).hasSize(2)
            assertThat(persistedMatch.teams.first().name).isEqualTo("team 1")
            assertThat(persistedMatch.teams.last().name).isEqualTo("team 2")
        }
    }

    private suspend fun getPersistedMatch(matchId: Long): Match? {
        return fixture.repository.getMatch(matchId)
    }
}
