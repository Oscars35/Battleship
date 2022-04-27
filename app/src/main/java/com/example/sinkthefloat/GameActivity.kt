package com.example.sinkthefloat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sinkthefloat.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = intent.getStringExtra("playerName").toString()
        binding.textView2.text = intent.getStringExtra("difficulty").toString()
        binding.textView3.text = intent.getStringExtra("oceanLevel").toString()
    }
}