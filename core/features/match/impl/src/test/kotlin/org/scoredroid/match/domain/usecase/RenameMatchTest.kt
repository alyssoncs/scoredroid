package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.FakeMatchLocalDataSource


@ExperimentalCoroutinesApi
class RenameMatchTest {

    private val localDataSource = FakeMatchLocalDataSource()
    private val repository = MatchRepository(localDataSource)
    private val renameMatch = RenameMatch(repository)

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

        @BeforeEach
        fun setUp() = runTest {
            repository.createMatch(CreateMatchRepositoryRequest(name = "generic name"))
        }

        @Test
        fun `return renamed match`() = runTest {
            val nonExistingMatchId = 0L

            val result = renameMatch(nonExistingMatchId, "specific name")

            assertThat(result.getOrThrow().name).isEqualTo("specific name")
        }
    }
}