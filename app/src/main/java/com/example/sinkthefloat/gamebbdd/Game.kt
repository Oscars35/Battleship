package com.example.sinkthefloat.gamebbdd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sinkthefloat.DifficultyLevel
import java.time.LocalDateTime

@Entity(tableName = "game_table")
data class Game(
    @PrimaryKey(autoGenerate = true) val gameId: Int = 0,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "playerName") val playerName: String?,
    @ColumnInfo(name = "oceanName") val oceanName: Int?,
    @ColumnInfo(name = "difficulty") val difficulty: String?,
    @ColumnInfo(name = "winner") val winner: String?
){
}
