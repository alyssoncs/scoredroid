package org.scoredroid.entrypoint

import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.usecase.DecrementScore
import org.scoredroid.usecase.IncrementScore

class PlayMatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = PlayMatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `IncrementScoreUseCase has the correct instance`() {
        assertCorrectInstance<IncrementScore> { incrementScoreUseCase }
    }

    @Test
    fun `DecrementScoreUseCase has the correct instance`() {
        assertCorrectInstance<DecrementScore> { decrementScoreUseCase }
    }

    private inline fun <reified T : Any>assertCorrectInstance(
        dependency: PlayMatchEntrypoint.() -> Any,
    ) {
        entrypoint.dependency().shouldBeInstanceOf<T>()
    }
}
