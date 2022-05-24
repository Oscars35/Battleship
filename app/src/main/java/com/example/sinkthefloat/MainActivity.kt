package com.example.sinkthefloat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.sinkthefloat.databinding.ActivityMainBinding
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var logsArray: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        addLog("Set action bar")
        binding.helpButton.setOnClickListener(this)
        binding.exitButton.setOnClickListener(this)
        binding.startButton.setOnClickListener(this)
    }

    override fun onClick(src: View) {
        when(src.id) {
            binding.helpButton.id -> startHelpActivity()
            binding.exitButton.id -> endApp()
            binding.startButton.id -> goGame()
        }
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
        addLog("Starting preferences menu")
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun goGame() {
        addLog("Starting game activity")
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("logsArray", logsArray)
        startActivity(intent)
    }

    private fun startHelpActivity() {
        addLog("Starting help activity")
        startActivity(Intent(this, HelpActivity::class.java))
    }

    private fun endApp() {
        finish()
        exitProcess(0)
    }

    private fun addLog(message: String) {
        logsArray.add(message)
    }
}