package org.scoredroid.infra.dataaccess.dao

import androidx.room.Dao
import org.scoredroid.infra.dataaccess.entities.MatchEntity

@Dao
interface MatchDao {
    fun insertMatch(match: MatchEntity)
}
