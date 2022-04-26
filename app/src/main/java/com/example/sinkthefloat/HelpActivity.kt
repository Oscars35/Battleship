package com.example.sinkthefloat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sinkthefloat.databinding.ActivityHelpBinding
import com.example.sinkthefloat.databinding.ActivityMainBinding

class HelpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.backButton.id -> finish()
        }
    }
}