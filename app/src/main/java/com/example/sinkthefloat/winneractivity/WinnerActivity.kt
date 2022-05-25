package com.example.sinkthefloat.winneractivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.sinkthefloat.R
import com.example.sinkthefloat.databinding.ActivityWinnerBinding
import com.example.sinkthefloat.gamebbdd.AppDatabase
import com.example.sinkthefloat.gamebbdd.Game
import com.example.sinkthefloat.settingsactivity.SettingsActivity
import java.time.LocalDateTime

class WinnerActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityWinnerBinding
    private var winner: String? = null
    private var logsArray: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        winner = intent.getStringExtra("winner")
        logsArray = intent.getStringArrayListExtra("logsArray")!!
        binding.winnerTv.text = getString(R.string.winner) + " " + winner

        addInfoToDatabase()

        binding.exitButton.setOnClickListener(this)
        binding.mailButton.setOnClickListener(this)
        binding.newGameButton.setOnClickListener(this)
        binding.logsEditText.setText(logsArray.joinToString(), TextView.BufferType.EDITABLE)
    }

    private fun addInfoToDatabase() {
        val db = AppDatabase.getInstance(this)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        db.gameDao().insertAll(Game(
            0,
            LocalDateTime.now().toString(),
            preferences.getString("player_name", "Player1").toString(),
            preferences.getString("ocean_level", "10").toString().toInt(),
            preferences.getString("ai_level", "Hard"),
            winner,
        ))
    }

    override fun onBackPressed() {
        return
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> goPreferencesMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goPreferencesMenu() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.newGameButton.id -> startNewGame()
            binding.mailButton.id -> sendMail()
            binding.exitButton.id -> exitApp()
        }
    }

    private fun sendMail() {
        if(TextUtils.isEmpty(binding.playerNameEditText.text)) binding.playerNameEditText.error = "You have to enter mail first"
        else sendMailViaIntent()
    }

    private fun sendMailViaIntent() {
        val uriMail = "mailto:" + binding.playerNameEditText.text.toString()
        val uriSubject = "?subject=${Uri.encode("Battleship: log")}"
        val uriText = "&body=${Uri.encode(logsArray.joinToString())}"
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("$uriMail$uriSubject$uriText"))
        startActivity(intent)
    }

    private fun startNewGame() {
        finish()
    }

    private fun exitApp() {
        finishAffinity()
    }
}