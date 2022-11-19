package org.scoredroid.match.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.repository.MatchRepository
import org.scoredroid.match.domain.usecase.CreateMatch
import org.scoredroid.match.domain.usecase.CreateMatchUseCase
import org.scoredroid.match.domain.usecase.GetMatchFlow
import org.scoredroid.match.domain.usecase.GetMatchFlowUseCase
import org.scoredroid.match.domain.usecase.GetMatches
import org.scoredroid.match.domain.usecase.GetMatchesUseCase
import org.scoredroid.match.domain.usecase.RemoveMatch
import org.scoredroid.match.domain.usecase.RemoveMatchUseCase
import org.scoredroid.match.domain.usecase.RenameMatch
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatch
import org.scoredroid.match.domain.usecase.SaveMatchUseCase

@Module
internal object MatchUseCasesModule {
    @Provides
    fun provideCreateMatch(repository: MatchRepository): CreateMatchUseCase {
        return CreateMatch(repository)
    }

    @Provides
    fun provideGetMatchFlow(repository: MatchRepository): GetMatchFlowUseCase {
        return GetMatchFlow(repository)
    }

    @Provides
    fun provideGetMatches(repository: MatchRepository): GetMatchesUseCase {
        return GetMatches(repository)
    }

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
}
