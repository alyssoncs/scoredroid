package org.scoredroid.infra.dataaccess.di

import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.datasource.local.PersistentMatchDataSource
import org.scoredroid.infra.dataaccess.datasource.local.RoomMatchDataSource

@Module(includes = [DatabaseModule::class])
object RoomMatchDataSourceModule {

    @Provides
    fun provideRoomMatchDataSource(
        matchDao: MatchDao,
    ): PersistentMatchDataSource {
        return RoomMatchDataSource(matchDao)
    }
}
