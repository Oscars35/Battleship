package com.example.sinkthefloat

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
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
    private lateinit var viewModel: GameActivityViewModel
    private var oceanLevel: Int = 0
    private var logs: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[GameActivityViewModel::class.java]
        getPreferencesInfo()
        createAndPassFragment()
    }

    private fun createAndPassFragment() {
        if(viewModel.firstTime.value == true) {
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
            viewModel.firstTime.value = false
        }
    }

    override fun onBackPressed() {
        return
    }

    private fun getPreferencesInfo() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        playerName = preferences.getString("player_name", "Player1").toString()
        oceanLevel = preferences.getString("ocean_level", "10").toString().toInt()
        difficulty = getEnumValue(preferences)
        addLog("Preferences: $playerName $oceanLevel $difficulty")
        logs = intent.getStringArrayListExtra("logsArray")!!
        addLog("Got intent info")
    }

    private fun getEnumValue(preferences: SharedPreferences): DifficultyLevel {
        return when(preferences.getString("ai_level", "Hard")) {
            "Easy" -> DifficultyLevel.EASY
            "Medium" -> DifficultyLevel.MEDIUM
            else -> DifficultyLevel.HARD
        }
    }

    private fun addLog(message: String) {
        logs.add(message)
    }

}