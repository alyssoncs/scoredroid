package org.scoredroid.match.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.match.domain.usecase.RenameMatch
import org.scoredroid.match.domain.usecase.RenameMatchUseCase

@Module
internal object MatchUseCasesModule {

    @Provides
    fun provideRenameMatch(repository: MatchRepository): RenameMatchUseCase {
        return RenameMatch(repository)
    }
}
