package com.example.sinkthefloat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.sinkthefloat.databinding.ActivityConfigurationBinding
import com.example.sinkthefloat.databinding.ActivityMainBinding

class ConfigurationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityConfigurationBinding
    private lateinit var playerName: String
    private lateinit var oceanLevel: String
    private lateinit var difficulty: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when {
            checkForEmptyPlayerName() -> binding.playerNameEditText.error = "Player name is required"
            checkForEmptyOceanSelect() -> setNotSelectedError(binding.twentyRadioButton)
            checkForEmptyDifficultLevel() -> setNotSelectedError(binding.hardRadioButton)
            else -> startNewActivity()
        }
    }

    private fun startNewActivity() {
        getSelectedStuff()
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("playerName", playerName)
        intent.putExtra("difficulty", difficulty)
        intent.putExtra("oceanLevel", oceanLevel)
        startActivity(intent)
        finish()
    }

    private fun getSelectedStuff() {
        val radioButton = binding.root.findViewById<RadioButton>(binding.iaLevelRg.checkedRadioButtonId)
        difficulty = radioButton.text.toString()
        val radioButton2 = binding.root.findViewById<RadioButton>(binding.radioGroupGrid.checkedRadioButtonId)
        oceanLevel = radioButton2.text.toString()
        playerName = binding.playerNameEditText.text.toString()
    }

    private fun setNotSelectedError(radioButton: RadioButton) {
        radioButton.error = "Ocean level and Difficulty are required"
    }

    private fun checkForEmptyDifficultLevel(): Boolean {
        return binding.iaLevelRg.checkedRadioButtonId == -1
    }

    private fun checkForEmptyOceanSelect(): Boolean {
        return binding.radioGroupGrid.checkedRadioButtonId == -1
    }

    private fun checkForEmptyPlayerName(): Boolean {
        return TextUtils.isEmpty(binding.playerNameEditText.text)
    }
}