package org.scoredroid.entrypoint.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.usecase.ClearTransientMatchData
import org.scoredroid.usecase.ClearTransientMatchDataUseCase
import org.scoredroid.usecase.SaveMatch
import org.scoredroid.usecase.SaveMatchUseCase

@Module
internal object PersistMatchChangesUseCasesModule {
    @Provides
    fun provideSaveMatch(repository: MatchRepository): SaveMatchUseCase {
        return SaveMatch(repository)
    }

    @Provides
    fun provideClearTransientMatchData(repository: MatchRepository): ClearTransientMatchDataUseCase {
        return ClearTransientMatchData(repository)
    }
}
