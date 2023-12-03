package org.scoredroid.entrypoint

import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.usecase.GetMatchFlow
import org.scoredroid.usecase.GetMatchesFlow

class GetMatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = GetMatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `getMatchFlowUseCase has the correct instance`() {
        assertCorrectInstance<GetMatchFlow> { getMatchFlowUseCase }
    }

    @Test
    fun `getMatchesUseCase has the correct instance`() {
        assertCorrectInstance<GetMatchesFlow> { getMatchesFlowUseCase }
    }

    private inline fun <reified T : Any>assertCorrectInstance(
        dependency: GetMatchEntrypoint.() -> Any,
    ) {
        entrypoint.dependency().shouldBeInstanceOf<T>()
    }
}
