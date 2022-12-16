package org.scoredroid.teams.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.teams.domain.usecase.AddTeam
import org.scoredroid.teams.domain.usecase.AddTeamUseCase
import org.scoredroid.teams.domain.usecase.MoveTeam
import org.scoredroid.teams.domain.usecase.MoveTeamUseCase
import org.scoredroid.teams.domain.usecase.RemoveTeam
import org.scoredroid.teams.domain.usecase.RemoveTeamUseCase
import org.scoredroid.teams.domain.usecase.RenameTeam
import org.scoredroid.teams.domain.usecase.RenameTeamUseCase

@Module
internal object TeamsUseCasesModule {
    @Provides
    fun provideAddTeam(repository: MatchRepository): AddTeamUseCase {
        return AddTeam(repository)
    }

    @Provides
    fun provideMoveTeam(repository: MatchRepository): MoveTeamUseCase {
        return MoveTeam(repository)
    }

    @Provides
    fun provideRemoveTeam(repository: MatchRepository): RemoveTeamUseCase {
        return RemoveTeam(repository)
    }

    @Provides
    fun provideRenameTeam(repository: MatchRepository): RenameTeamUseCase {
        return RenameTeam(repository)
    }
}
