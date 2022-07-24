package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.factories.repository.MatchRepositoryTestFactory
import kotlin.properties.Delegates


@ExperimentalCoroutinesApi
class RenameMatchTest {

    private val repository = MatchRepositoryTestFactory.create()
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

        private var matchId by Delegates.notNull<Long>()

        @BeforeEach
        fun setUp() = runTest {
            matchId = repository.createMatch(CreateMatchRepositoryRequest(name = "generic name")).id
        }

        @Test
        fun `return renamed match`() = runTest {
            val result = renameMatch(matchId, "specific name")

            assertThat(result.getOrThrow().name).isEqualTo("specific name")
        }
    }
}
