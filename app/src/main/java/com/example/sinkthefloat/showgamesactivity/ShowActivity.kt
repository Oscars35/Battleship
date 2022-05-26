package com.example.sinkthefloat.showgamesactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sinkthefloat.R
import com.example.sinkthefloat.databinding.ActivityShowgamesBinding
import com.example.sinkthefloat.gamebbdd.AppDatabase
import com.example.sinkthefloat.showgamesactivity.fragment.RecyclerFragment


class ShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowgamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowgamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackButtonListener()
        initFragment()
    }

    private fun initFragment() {
        val fragment = RecyclerFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, fragment)
            commit()
        }
    }

    private fun setBackButtonListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}