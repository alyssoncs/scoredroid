package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.match.MatchEntrypoint
import org.scoredroid.match.domain.usecase.CreateMatchUseCase
import org.scoredroid.match.domain.usecase.GetMatchFlowUseCase
import org.scoredroid.match.domain.usecase.GetMatchesUseCase
import org.scoredroid.match.domain.usecase.RemoveMatchUseCase
import org.scoredroid.match.domain.usecase.RenameMatchUseCase
import org.scoredroid.match.domain.usecase.SaveMatchUseCase

@Module
object MatchUseCasesModule {

    @Provides
    fun provideMatchEntrypoint(dataSource: PersistentMatchDataSource): MatchEntrypoint {
        return MatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideCreateMatchUseCase(entrypoint: MatchEntrypoint): CreateMatchUseCase {
        return entrypoint.createMatchUseCase
    }

    @Provides
    fun provideGetMatchFlowUseCase(entrypoint: MatchEntrypoint): GetMatchFlowUseCase {
        return entrypoint.getMatchFlowUseCase
    }

    @Provides
    fun provideGetMatchesUseCase(entrypoint: MatchEntrypoint): GetMatchesUseCase {
        return entrypoint.getMatchesUseCase
    }

    @Provides
    fun provideRemoveMatchUseCase(entrypoint: MatchEntrypoint): RemoveMatchUseCase {
        return entrypoint.removeMatchUseCase
    }

    @Provides
    fun provideRenameMatchUseCase(entrypoint: MatchEntrypoint): RenameMatchUseCase {
        return entrypoint.renameMatchUseCase
    }

    @Provides
    fun provideSaveMatchUseCase(entrypoint: MatchEntrypoint): SaveMatchUseCase {
        return entrypoint.saveMatchUseCase
    }
}