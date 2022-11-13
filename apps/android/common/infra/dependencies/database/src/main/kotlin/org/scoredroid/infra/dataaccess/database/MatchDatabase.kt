package org.scoredroid.infra.dataaccess.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.scoredroid.infra.dataaccess.dao.MatchDao
import org.scoredroid.infra.dataaccess.entities.MatchEntity
import org.scoredroid.infra.dataaccess.entities.TeamEntity

@Database(
    entities = [MatchEntity::class, TeamEntity::class],
    version = 1,
)
abstract class MatchDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
}
