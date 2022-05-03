package com.example.sinkthefloat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sinkthefloat.databinding.ActivityWinnerBinding

class WinnerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWinnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val winner: String? = intent.getStringExtra("winner")
        binding.winnerTv.text = winner
    }
}