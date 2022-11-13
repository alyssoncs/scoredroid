package org.scoredroid.infra.dataaccess.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.database.MatchDatabase

@Module
object DatabaseModule {

    @Provides
    fun provideMatchDatabase(context: Context): MatchDatabase {
        return Room
            .databaseBuilder(
                context,
                MatchDatabase::class.java,
                "match-db",
            )
            .build()
    }

    @Provides
    fun provideMatchDao(db: MatchDatabase): MatchDao {
        return db.matchDao()
    }
}