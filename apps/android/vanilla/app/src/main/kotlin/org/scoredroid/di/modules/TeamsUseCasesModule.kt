package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.teams.TeamsEntrypoint
import org.scoredroid.teams.domain.usecase.AddTeamUseCase
import org.scoredroid.teams.domain.usecase.MoveTeamUseCase
import org.scoredroid.teams.domain.usecase.RemoveTeamUseCase
import org.scoredroid.teams.domain.usecase.RenameTeamUseCase

@Module
object TeamsUseCasesModule {

    @Provides
    fun provideTeamsEntrypoint(dataSource: PersistentMatchDataSource): TeamsEntrypoint {
        return TeamsEntrypoint.create(dataSource)
    }

    @Provides
    fun provideAddTeamUseCase(entrypoint: TeamsEntrypoint): AddTeamUseCase {
        return entrypoint.addTeamUseCase
    }

    @Provides
    fun provideMoveTeamUseCase(entrypoint: TeamsEntrypoint): MoveTeamUseCase {
        return entrypoint.moveTeamUseCase
    }

    @Provides
    fun provideRemoveTeamUseCase(entrypoint: TeamsEntrypoint): RemoveTeamUseCase {
        return entrypoint.removeTeamUseCase
    }

    @Provides
    fun provideRenameTeamUseCase(entrypoint: TeamsEntrypoint): RenameTeamUseCase {
        return entrypoint.renameTeamUseCase
    }
}
