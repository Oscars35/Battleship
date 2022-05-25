package com.example.sinkthefloat.showgamesactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sinkthefloat.R
import com.example.sinkthefloat.databinding.ActivityShowGameBinding

class ShowGame : AppCompatActivity() {

    private var gameId: Int = 0
    private lateinit var date: String
    private lateinit var playerName: String
    private lateinit var oceanName: String
    private lateinit var difficulty: String
    private lateinit var winner: String

    private lateinit var binding: ActivityShowGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackButtonListener()
        getInfo()
        setInfo()
    }

    private fun setInfo() {
        binding.gameIdTv.text = "Game number: $gameId"
        binding.dateTv.text = "Date: $date"
        binding.playerNameTv.text = "Player name: $playerName"
        binding.oceanNameTv.text = "Ocean name: $oceanName"
        binding.difficultyTv.text = "Difficulty: $difficulty"
        binding.winnerTv.text = "Winner: $winner"
    }

    private fun setBackButtonListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getInfo() {
        gameId = intent.getIntExtra("gameId", 0)
        date = intent.getStringExtra("date").toString()
        playerName = intent.getStringExtra("playerName").toString()
        oceanName = intent.getStringExtra("oceanName").toString()
        difficulty = intent.getStringExtra("difficulty").toString()
        winner = intent.getStringExtra("winner").toString()
    }
}