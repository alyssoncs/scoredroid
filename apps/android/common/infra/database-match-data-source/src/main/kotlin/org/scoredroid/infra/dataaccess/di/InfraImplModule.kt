package org.scoredroid.infra.dataaccess.di

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.datasource.local.MatchDaoToPersistentMatchDataSourceAdapter
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource

@Module(includes = [DatabaseModule::class])
object InfraImplModule {

    @Provides
    fun provideMatchDaoToPersistentMatchDataSourceAdapter(
        matchDao: MatchDao,
    ): PersistentMatchDataSource {
        return MatchDaoToPersistentMatchDataSourceAdapter(matchDao)
    }
}
