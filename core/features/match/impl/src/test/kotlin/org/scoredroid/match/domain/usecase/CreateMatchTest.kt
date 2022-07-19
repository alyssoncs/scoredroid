package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.factories.repository.MatchRepositoryTestFactory
import org.scoredroid.match.domain.request.CreateMatchRequestOptions

@ExperimentalCoroutinesApi
class CreateMatchTest {

    private val repository = MatchRepositoryTestFactory.create()
    private val createMatch = CreateMatch(repository)

    @Nested
    inner class DefaultParam {

        @Test
        fun `id provided by local data source`() = runTest {
            repeat(2) {
                repository.createMatch(CreateMatchRepositoryRequest("", emptyList()))
            }

            val match = createMatch()

            assertThat(match.id).isEqualTo(2)
        }

        @Test
        fun `empty match name`() = runTest {
            val match = createMatch()

            assertThat(match.name).isEmpty()
        }

        @Test
        fun `no teams are created`() = runTest {
            val match = createMatch()

            assertThat(match.teams).isEmpty()
        }
    }

    @Nested
    inner class CustomParam {

        @Test
        fun `custom match name`() = runTest {
            val match = createMatch(CreateMatchRequestOptions(matchName = "match name"))

            assertThat(match.name).isEqualTo("match name")
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
        }
    }
}