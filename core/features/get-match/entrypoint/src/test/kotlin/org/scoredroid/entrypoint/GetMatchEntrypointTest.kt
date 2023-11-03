package org.scoredroid.entrypoint

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.usecase.GetMatchFlow
import org.scoredroid.usecase.GetMatches

class GetMatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = GetMatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `getMatchFlowUseCase has the correct instance`() {
        assertCorrectInstance<GetMatchFlow> { getMatchFlowUseCase }
    }

    @Test
    fun `getMatchesUseCase has the correct instance`() {
        assertCorrectInstance<GetMatches> { getMatchesUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: GetMatchEntrypoint.() -> Any,
    ) {
        Truth.assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
