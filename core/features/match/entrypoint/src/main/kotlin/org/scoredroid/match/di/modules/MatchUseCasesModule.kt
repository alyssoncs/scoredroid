package org.scoredroid.match.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.match.domain.usecase.ClearTransientMatchData
import org.scoredroid.match.domain.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.match.domain.usecase.RemoveMatch
import org.scoredroid.match.domain.usecase.RemoveMatchUseCase
import org.scoredroid.match.domain.usecase.RenameMatch
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatch
import org.scoredroid.match.domain.usecase.SaveMatchUseCase

@Module
internal object MatchUseCasesModule {

    @Provides
    fun provideRemoveMatch(repository: MatchRepository): RemoveMatchUseCase {
        return RemoveMatch(repository)
    }

    @Provides
    fun provideRenameMatch(repository: MatchRepository): RenameMatchUseCase {
        return RenameMatch(repository)
    }

    @Provides
    fun provideSaveMatch(repository: MatchRepository): SaveMatchUseCase {
        return SaveMatch(repository)
    }

    @Provides
    fun provideClearTransientMatchData(repository: MatchRepository): ClearTransientMatchDataUseCase {
        return ClearTransientMatchData(repository)
    }
}
