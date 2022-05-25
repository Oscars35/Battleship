package com.example.sinkthefloat.gamebbdd

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sinkthefloat.DifficultyLevel
import java.sql.Date

@Entity
data class Game(
    @PrimaryKey val gameId: Int,
    @ColumnInfo(name = "Date") val date: Date?,
    @ColumnInfo(name = "playerName") val playerName: String?,
    @ColumnInfo(name = "oceanLevel") val oceanLevel: Int?,
    @ColumnInfo(name = "difficulty") val difficulty: DifficultyLevel?,
    @ColumnInfo(name = "winner") val winner: String?
)
