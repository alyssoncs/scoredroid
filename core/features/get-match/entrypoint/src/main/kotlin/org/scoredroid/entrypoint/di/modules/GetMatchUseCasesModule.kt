package org.scoredroid.entrypoint.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.usecase.GetMatchFlow
import org.scoredroid.usecase.GetMatchFlowUseCase
import org.scoredroid.usecase.GetMatches
import org.scoredroid.usecase.GetMatchesUseCase

@Module
internal object GetMatchUseCasesModule {
    @Provides
    fun provideGetMatchFlow(repository: MatchRepository): GetMatchFlowUseCase {
        return GetMatchFlow(repository)
    }

    @Provides
    fun provideGetMatches(repository: MatchRepository): GetMatchesUseCase {
        return GetMatches(repository)
    }
}
