package org.scoredroid.infra.dataaccess.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.scoredroid.infra.dataaccess.entities.MatchEntity
import org.scoredroid.infra.dataaccess.entities.TeamEntity

data class InsertMatchDaoRequestModel(
    @ColumnInfo(name = MatchEntity.ColumnsName.name)
    val name: String,
)

@Dao
interface MatchDao {
    @Insert(entity = MatchEntity::class)
    suspend fun insertMatch(match: InsertMatchDaoRequestModel): Long

    @Insert
    suspend fun insertTeams(teams: List<TeamEntity>)

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Delete
    suspend fun deleteMatch(match: MatchEntity)

    @Query(
        """
        SELECT 
            * 
        FROM 
            match LEFT JOIN team ON id = match_id
        WHERE 
            id = :matchId
        """
    )
    suspend fun getMatchById(matchId: Long): Map<MatchEntity, List<TeamEntity>>
}
