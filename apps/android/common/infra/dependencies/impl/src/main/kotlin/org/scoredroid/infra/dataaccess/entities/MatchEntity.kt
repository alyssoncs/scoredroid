package org.scoredroid.infra.dataaccess.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ColumnsName.id) val id: Long,
    @ColumnInfo(name = ColumnsName.name) val name: String,
) {
    object ColumnsName {
        const val id = "id"
        const val name = "match_name"
    }
}
