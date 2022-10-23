package org.scoredroid.infra.dataaccess.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "team",
    primaryKeys = [TeamEntity.matchId, TeamEntity.position],
    foreignKeys = [
        ForeignKey(
            entity = MatchEntity::class,
            parentColumns = [MatchEntity.id],
            childColumns = [TeamEntity.matchId],
            onDelete = CASCADE,
        )
    ],
)
data class TeamEntity(
    @ColumnInfo(name = ColumnsName.name) val name: String,
    @ColumnInfo(name = ColumnsName.score) val score: Int,
    @ColumnInfo(name = ColumnsName.matchId) val matchId: Long,
    @ColumnInfo(name = ColumnsName.position) val position: Int,
) {
    companion object ColumnsName {
        const val name = "name"
        const val score = "score"
        const val matchId = "match_id"
        const val position = "position"
    }
}
