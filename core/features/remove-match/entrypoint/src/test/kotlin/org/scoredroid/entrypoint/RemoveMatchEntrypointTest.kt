package org.scoredroid.entrypoint

import io.kotest.matchers.types.shouldBeInstanceOf
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

    private inline fun <reified T : Any>assertCorrectInstance(
        dependency: RemoveMatchEntrypoint.() -> Any,
    ) {
        entrypoint.dependency().shouldBeInstanceOf<T>()
    }
}
