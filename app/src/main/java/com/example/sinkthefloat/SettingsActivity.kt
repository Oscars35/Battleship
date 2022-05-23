package com.example.sinkthefloat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.sinkthefloat.databinding.ActivitySettingsBinding

class SettingsActivity : FragmentActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var fragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
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

    override fun onClick(p0: View) {
        when(p0.id) {
            binding.backButton.id -> finish()
        }
    }
}