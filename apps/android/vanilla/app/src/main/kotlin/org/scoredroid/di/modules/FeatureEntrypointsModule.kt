package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.entrypoint.CreateMatchEntrypoint
import org.scoredroid.entrypoint.GetMatchEntrypoint
import org.scoredroid.entrypoint.PlayMatchEntrypoint
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource

@Module
object FeatureEntrypointsModule {

    @Provides
    fun provideCreateMatchEntrypoint(dataSource: PersistentMatchDataSource): CreateMatchEntrypoint {
        return CreateMatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideGetMatchEntrypoint(dataSource: PersistentMatchDataSource): GetMatchEntrypoint {
        return GetMatchEntrypoint.create(dataSource)
    }

    @Provides
    fun providePlayMatchEntrypoint(dataSource: PersistentMatchDataSource): PlayMatchEntrypoint {
        return PlayMatchEntrypoint.create(dataSource)
    }
}
