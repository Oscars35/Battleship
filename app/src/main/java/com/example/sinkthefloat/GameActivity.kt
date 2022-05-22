package com.example.sinkthefloat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.sinkthefloat.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    object Constants {
        const val MAX_BOATS = 3
        const val MIN_PROBABILITY = 6
        const val MEDIUM_PROBABILITY = 4
        const val MAX_PROBABILITY = 2
    }

    private lateinit var binding: ActivityGameBinding
    private lateinit var playerName: String
    private lateinit var difficulty: DifficultyLevel
    private lateinit var fragment: GridsFragment
    private var oceanLevel: Int = 0
    private var logs: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentInfo()
        createAndPassFragment()
    }

    private fun createAndPassFragment() {
        fragment = GridsFragment().apply {
            arguments = Bundle().apply {
                putString("playerName", playerName)
                putString("oceanLevel", oceanLevel.toString())
                putSerializable("difficulty", difficulty)
                putStringArrayList("logsArray", logs)
            }
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.gridsFragment, fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        return
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.beginTransaction().detach(fragment)
        finish()
    }

    private fun getIntentInfo() {
        oceanLevel = intent.getStringExtra("oceanLevel")!!.toInt()
        playerName = intent.getStringExtra("playerName")!!
        difficulty = (intent.getSerializableExtra("difficulty") as DifficultyLevel?)!!
        logs = intent.getStringArrayListExtra("logsArray")!!
        addLog("Got intent info")
    }

    private fun addLog(message: String) {
        logs.add(message)
    }

}