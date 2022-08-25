package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.data.response.MatchResponse
import org.scoredroid.infra.test.fixtures.dataaccess.repository.MatchRepositoryFixtureFactory
import kotlin.properties.Delegates


@ExperimentalCoroutinesApi
class RenameMatchTest {

    private val fixture = MatchRepositoryFixtureFactory.create()
    private val renameMatch = RenameMatch(fixture.repository)

    @Nested
    inner class MatchNotFound {

        @Test
        fun `return error`() = runTest {
            val nonExistingMatchId = 0L

            val result = renameMatch(nonExistingMatchId, "irrelevant")

            assertThat(result.isFailure).isTrue()
        }
    }

    @Nested
    inner class MatchFound {

        private var matchId by Delegates.notNull<Long>()

        @BeforeEach
        fun setUp() = runTest {
            matchId = fixture.createNamedMatch(matchName = "generic name").id
        }

        @Test
        fun `return renamed match`() = runTest {
            val result = renameMatch(matchId, "specific name")

            assertMatchWasRenamed(result, "specific name")
        }

        private suspend fun assertMatchWasRenamed(
            result: Result<MatchResponse>,
            matchName: String
        ) {
            assertThat(result.getOrThrow().name).isEqualTo(matchName)
            assertThat(fixture.repository.getMatch(matchId)!!.name).isEqualTo(matchName)
        }
    }
}
