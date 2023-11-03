package org.scoredroid.entrypoint.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.usecase.CreateMatch
import org.scoredroid.usecase.CreateMatchUseCase

@Module
internal object CreateMatchUseCasesModule {
    @Provides
    fun provideCreateMatchUseCase(repository: MatchRepository): CreateMatchUseCase {
        return CreateMatch(repository)
    }
}
