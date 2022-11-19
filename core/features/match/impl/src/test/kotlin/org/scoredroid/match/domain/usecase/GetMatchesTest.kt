package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.data.response.TeamResponse
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.dataaccess.requestmodel.TeamRequest
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory

@OptIn(ExperimentalCoroutinesApi::class)
class GetMatchesTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val getMatches = GetMatches(fixture.repository)

    @Nested
    inner class NoMatchExists {

        @Test
        fun `should return empty list`() = runTest {
            val matches = getMatches()

            assertThat(matches).isEmpty()
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
            fun `should return matches in reverse order`() = runTest {
                val matches = getMatches()

                assertThat(matches).hasSize(2)
                assertThat(matches[0]).isEqualTo(expectedSecondMatchResponse)
                assertThat(matches[1]).isEqualTo(expectedFirstMatchResponse)
            }
        }

        @Nested
        inner class AllMatchesInPersistentStorage {

            @BeforeEach
            fun setUp() = runTest {
                fixture.createMatch(firstMatchRequest)
                fixture.createMatch(secondMatchRequest)
                fixture.rebootApplication()
            }

            @Test
            fun `should return matches in reverse order`() = runTest {
                val matches = getMatches()

                assertThat(matches).hasSize(2)
                assertThat(matches[0]).isEqualTo(expectedSecondMatchResponse)
                assertThat(matches[1]).isEqualTo(expectedFirstMatchResponse)
            }
        }

        @Nested
        inner class SomeMatchesInPersistentStorageAndOthersInTransientStorage {

            @BeforeEach
            fun setUp() = runTest {
                fixture.createMatch(firstMatchRequest)
                fixture.rebootApplication()
                fixture.createMatch(secondMatchRequest)
            }

            @Test
            fun `should return matches in reverse order`() = runTest {
                val matches = getMatches()

                assertThat(matches).hasSize(2)
                assertThat(matches[0]).isEqualTo(expectedSecondMatchResponse)
                assertThat(matches[1]).isEqualTo(expectedFirstMatchResponse)
            }
        }
    }
}
