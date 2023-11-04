package org.scoredroid.entrypoint

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.usecase.RemoveMatch

class RemoveMatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = RemoveMatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `createMatchUseCase has the correct instance`() {
        assertCorrectInstance<RemoveMatch> { removeMatchUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: RemoveMatchEntrypoint.() -> Any,
    ) {
        Truth.assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
