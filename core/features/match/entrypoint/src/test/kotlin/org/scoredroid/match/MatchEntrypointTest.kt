package org.scoredroid.match

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.match.domain.usecase.RenameMatch

class MatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = MatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `renameMatchUseCase has the correct instance`() {
        assertCorrectInstance<RenameMatch> { renameMatchUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: MatchEntrypoint.() -> Any,
    ) {
        assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
