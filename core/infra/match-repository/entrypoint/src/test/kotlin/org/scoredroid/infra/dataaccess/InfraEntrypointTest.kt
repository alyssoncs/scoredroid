package org.scoredroid.infra.dataaccess

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.scoredroid.infra.dataaccess.requestmodel.CreateMatchRepositoryRequest
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource

class InfraEntrypointTest {
    private val persistentDataSource = FakePersistentMatchDataSource()

    @Test
    fun `repository instance is always the same`() = runTest {
        val firstInstance = InfraEntrypoint.create(persistentDataSource).matchRepository
        val secondInstance = InfraEntrypoint.create(persistentDataSource).matchRepository

        assertThat(firstInstance).isSameInstanceAs(secondInstance)
    }

    @Test
    fun `repository is created with the given persistent data source`() = runTest {
        val matchName = "match name"
        persistentDataSource.createMatch(CreateMatchRepositoryRequest(name = matchName))

        val repository = InfraEntrypoint.create(persistentDataSource).matchRepository

        assertThat(repository.getMatch(0L)!!.name).isEqualTo(matchName)
    }
}
