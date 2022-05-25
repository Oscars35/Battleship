package com.example.sinkthefloat.gamebbdd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sinkthefloat.gamebbdd.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): List<Game>

    @Query("SELECT * FROM game WHERE `playerName` LIKE :name")
    fun loadAllByName(name: String): List<Game>

    @Insert
    fun insertAll(vararg game: Game)

    @Delete
    fun delete(game: Game)
}