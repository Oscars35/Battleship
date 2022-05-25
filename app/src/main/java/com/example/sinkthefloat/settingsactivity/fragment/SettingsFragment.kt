package com.example.sinkthefloat.settingsactivity.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.sinkthefloat.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        setPreferencesFromResource(R.xml.preferences, p1)
    }

}