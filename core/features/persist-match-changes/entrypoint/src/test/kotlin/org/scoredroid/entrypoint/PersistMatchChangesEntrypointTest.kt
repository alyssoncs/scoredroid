package org.scoredroid.entrypoint

import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.usecase.ClearTransientMatchData
import org.scoredroid.usecase.SaveMatch

class PersistMatchChangesEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = PersistMatchChangesEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `saveMatchUseCase has the correct instance`() {
        assertCorrectInstance<SaveMatch> { saveMatchUseCase }
    }

    @Test
    fun `clearTransientMatchDataUseCase has the correct instance`() {
        assertCorrectInstance<ClearTransientMatchData> { clearTransientMatchDataUseCase }
    }

    private inline fun <reified T : Any>assertCorrectInstance(
        dependency: PersistMatchChangesEntrypoint.() -> Any,
    ) {
        entrypoint.dependency().shouldBeInstanceOf<T>()
    }
}
