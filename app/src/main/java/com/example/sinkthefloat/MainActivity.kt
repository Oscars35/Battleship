package com.example.sinkthefloat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sinkthefloat.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.helpButton.setOnClickListener(this)
        binding.exitButton.setOnClickListener(this)
    }

    override fun onClick(src: View) {
        when(src.id) {
            binding.helpButton.id -> startHelpActivity()
            binding.exitButton.id -> endApp()
        }
    }

    private fun startHelpActivity() {
        startActivity(Intent(this, HelpActivity::class.java))
    }

    private fun endApp() {
        finish()
        exitProcess(0)
    }
}