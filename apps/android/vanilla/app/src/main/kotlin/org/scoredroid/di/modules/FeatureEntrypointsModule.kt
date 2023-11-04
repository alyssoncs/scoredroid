package org.scoredroid.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.entrypoint.CreateMatchEntrypoint
import org.scoredroid.entrypoint.GetMatchEntrypoint
import org.scoredroid.entrypoint.PersistMatchChangesEntrypoint
import org.scoredroid.entrypoint.PlayMatchEntrypoint
import org.scoredroid.entrypoint.RemoveMatchEntrypoint
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
    fun providePersistMatchChangesEntrypoint(dataSource: PersistentMatchDataSource): PersistMatchChangesEntrypoint {
        return PersistMatchChangesEntrypoint.create(dataSource)
    }

    @Provides
    fun providePlayMatchEntrypoint(dataSource: PersistentMatchDataSource): PlayMatchEntrypoint {
        return PlayMatchEntrypoint.create(dataSource)
    }

    @Provides
    fun provideRemoveMatchEntrypoint(dataSource: PersistentMatchDataSource): RemoveMatchEntrypoint {
        return RemoveMatchEntrypoint.create(dataSource)
    }
}
