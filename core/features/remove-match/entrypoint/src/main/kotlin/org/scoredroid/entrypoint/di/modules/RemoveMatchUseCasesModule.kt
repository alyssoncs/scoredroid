package org.scoredroid.entrypoint.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.usecase.RemoveMatch
import org.scoredroid.usecase.RemoveMatchUseCase

@Module
internal object RemoveMatchUseCasesModule {
    @Provides
    fun provideRemoveMatchUseCase(repository: MatchRepository): RemoveMatchUseCase {
        return RemoveMatch(repository)
    }
}
