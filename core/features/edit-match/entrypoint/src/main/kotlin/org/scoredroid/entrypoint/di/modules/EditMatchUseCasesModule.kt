package org.scoredroid.entrypoint.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.usecase.AddTeam
import org.scoredroid.usecase.AddTeamUseCase
import org.scoredroid.usecase.MoveTeam
import org.scoredroid.usecase.MoveTeamUseCase
import org.scoredroid.usecase.RemoveTeam
import org.scoredroid.usecase.RemoveTeamUseCase
import org.scoredroid.usecase.RenameMatch
import org.scoredroid.usecase.RenameMatchUseCase
import org.scoredroid.usecase.RenameTeam
import org.scoredroid.usecase.RenameTeamUseCase

@Module
internal object EditMatchUseCasesModule {

    @Provides
    fun provideRenameMatch(repository: MatchRepository): RenameMatchUseCase {
        return RenameMatch(repository)
    }

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
