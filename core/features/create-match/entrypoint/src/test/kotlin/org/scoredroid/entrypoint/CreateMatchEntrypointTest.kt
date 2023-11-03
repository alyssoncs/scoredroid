package org.scoredroid.entrypoint

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.usecase.CreateMatch

class CreateMatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = CreateMatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `createMatchUseCase has the correct instance`() {
        assertCorrectInstance<CreateMatch> { createMatchUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: CreateMatchEntrypoint.() -> Any,
    ) {
        Truth.assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
