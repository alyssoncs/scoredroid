package org.scoredroid.match.domain.usecase

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.FakeMatchLocalDataSource

@ExperimentalCoroutinesApi
class CreateMatchTest {
    private val localDataSource = FakeMatchLocalDataSource()
    private val repository = MatchRepository(localDataSource)
    private val createMatch = CreateMatch(repository)

    @Nested
    inner class DefaultParam {

        @Test
        fun `id provided by local data source`() = runTest {
            repeat(2) {
                repository.createMatch(CreateMatchRepositoryRequest(emptyList()))
            }

            val match = createMatch()

            assertThat(match.id).isEqualTo(2)
        }

        @Test
        fun `no teams are created`() = runTest {
            val match = createMatch()

            assertThat(match.teams).isEmpty()
        }
    }
}