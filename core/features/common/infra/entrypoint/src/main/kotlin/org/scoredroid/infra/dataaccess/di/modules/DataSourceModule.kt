package org.scoredroid.infra.dataaccess.di.modules

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.datasource.local.InMemoryMatchDataSource
import org.scoredroid.infra.dataaccess.datasource.local.TransientMatchDataSource

@Module
internal object DataSourceModule {
    @Provides
    fun provideTransientMatchDataSource(): TransientMatchDataSource {
        return InMemoryMatchDataSource.instance
    }
}
