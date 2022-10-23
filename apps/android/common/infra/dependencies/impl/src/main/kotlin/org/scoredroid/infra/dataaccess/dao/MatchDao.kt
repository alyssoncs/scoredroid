package org.scoredroid.infra.dataaccess.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.scoredroid.infra.dataaccess.entities.MatchEntity
import org.scoredroid.infra.dataaccess.entities.TeamEntity

@Dao
interface MatchDao {
    @Insert
    suspend fun insertMatch(match: MatchEntity, teams: List<TeamEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Delete
    suspend fun deleteMatch(match: MatchEntity)

    @Query(
        """
        SELECT * 
        FROM `match` JOIN team 
        ON `match`.id = match_id
        WHERE `match`.id = :matchId
        """
    )
    fun getMatch(matchId: Long): Map<MatchEntity, List<TeamEntity>>
}
