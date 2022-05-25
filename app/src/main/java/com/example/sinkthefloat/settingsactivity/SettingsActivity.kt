package com.example.sinkthefloat.settingsactivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sinkthefloat.R
import com.example.sinkthefloat.settingsactivity.fragment.SettingsFragment
import com.example.sinkthefloat.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var fragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener(this)
        setFragmentView()
    }

    private fun setFragmentView() {
        fragment = SettingsFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.preferencesFragment, fragment)
            commitNow()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.backButton.id -> finish()
        }
    }
}