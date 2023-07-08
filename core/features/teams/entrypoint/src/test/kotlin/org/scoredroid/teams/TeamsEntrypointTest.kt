package org.scoredroid.teams

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.scoredroid.infra.test.doubles.dataaccess.repository.FakePersistentMatchDataSource
import org.scoredroid.teams.domain.usecase.AddTeam
import org.scoredroid.teams.domain.usecase.MoveTeam
import org.scoredroid.teams.domain.usecase.RemoveTeam
import org.scoredroid.teams.domain.usecase.RenameTeam

class TeamsEntrypointTest {
    private val persistentMatchDataSource = FakePersistentMatchDataSource()
    private val entrypoint = TeamsEntrypoint.create(persistentMatchDataSource)

    @Test
    fun `createMatchUseCase has the correct instance`() {
        assertCorrectInstance<AddTeam> { addTeamUseCase }
    }

    @Test
    fun `getMatchFlowUseCase has the correct instance`() {
        assertCorrectInstance<MoveTeam> { moveTeamUseCase }
    }

    @Test
    fun `removeMatchUseCase has the correct instance`() {
        assertCorrectInstance<RemoveTeam> { removeTeamUseCase }
    }

    @Test
    fun `renameMatchUseCase has the correct instance`() {
        assertCorrectInstance<RenameTeam> { renameTeamUseCase }
    }

    private inline fun <reified T>assertCorrectInstance(
        dependency: TeamsEntrypoint.() -> Any,
    ) {
        assertThat(entrypoint.dependency()).isInstanceOf(T::class.java)
    }
}
