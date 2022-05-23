package com.example.sinkthefloat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sinkthefloat.databinding.ActivityMainBinding
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.helpButton.setOnClickListener(this)
        binding.exitButton.setOnClickListener(this)
        binding.startButton.setOnClickListener(this)
        binding.backButton.setOnClickListener(this)
    }

    override fun onClick(src: View) {
        when(src.id) {
            binding.helpButton.id -> startHelpActivity()
            binding.exitButton.id -> endApp()
            binding.startButton.id -> selectFeatures()
            binding.backButton.id -> visibleButtons()
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
        /*fragment = SettingsFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.preferencesFragment, fragment)
            commit()
        }
        hideOrShowOtherStuff()*/
    }

    private fun visibleButtons() {
        binding.mainText.visibility = View.VISIBLE
        binding.helpButton.visibility = View.VISIBLE
        binding.startButton.visibility = View.VISIBLE
        binding.exitButton.visibility = View.VISIBLE
        binding.mainToolBar.visibility = View.VISIBLE
        binding.backButton.visibility = View.GONE
        binding.preferencesFragment.visibility = View.GONE
        supportFragmentManager.beginTransaction().remove(fragment)
        supportFragmentManager.beginTransaction().detach(fragment)

    }

    private fun selectFeatures() {
        startActivity(Intent(this, ConfigurationActivity::class.java))
    }

    private fun startHelpActivity() {
        startActivity(Intent(this, HelpActivity::class.java))
    }

    private fun endApp() {
        finish()
        exitProcess(0)
    }
}