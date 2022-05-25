package com.example.sinkthefloat.showgamesactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sinkthefloat.databinding.ActivityShowgamesBinding
import com.example.sinkthefloat.gamebbdd.AppDatabase


class ShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowgamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowgamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackButtonListener()
        initRecycler()
    }

    private fun setBackButtonListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun initRecycler() {
        binding.gamesRecycler.layoutManager = LinearLayoutManager(this)
        val adapter = GamesAdapter(AppDatabase.getInstance(this).gameDao().getAll())
        binding.gamesRecycler.adapter = adapter
    }
}