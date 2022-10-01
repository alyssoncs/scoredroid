package org.scoredroid.infra.dataaccess.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.datasource.local.TransientMatchDataSource
import org.scoredroid.infra.dataaccess.repository.MatchRepository

@Module
internal object RepositoryModule {
    @Provides
    fun provideMatchRepository(
        transientDataSource: TransientMatchDataSource,
        persistentDataSource: PersistentMatchDataSource,
    ): MatchRepository {
        return MatchRepository(transientDataSource, persistentDataSource)
    }
}
