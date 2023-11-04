package org.scoredroid.entrypoint

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.usecase.AddTeam
import org.scoredroid.usecase.MoveTeam
import org.scoredroid.usecase.RemoveTeam
import org.scoredroid.usecase.RenameMatch
import org.scoredroid.usecase.RenameTeam

class EditMatchEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = EditMatchEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `renameMatchUseCase has the correct instance`() {
        assertCorrectInstance<RenameMatch> { renameMatchUseCase }
    }

    @Test
    fun `createMatchUseCase has the correct instance`() {
        assertCorrectInstance<AddTeam> { addTeamUseCase }
    }

    @Test
    fun `moveTeamUseCase has the correct instance`() {
        assertCorrectInstance<MoveTeam> { moveTeamUseCase }
    }

    @Test
    fun `removeMatchUseCase has the correct instance`() {
        assertCorrectInstance<RemoveTeam> { removeTeamUseCase }
    }

    @Test
    fun `renameTeamUseCase has the correct instance`() {
        assertCorrectInstance<RenameTeam> { renameTeamUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: EditMatchEntrypoint.() -> Any,
    ) {
        assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
