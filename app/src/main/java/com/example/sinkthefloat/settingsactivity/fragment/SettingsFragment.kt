package com.example.sinkthefloat.settingsactivity.fragment

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.widget.EditText
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.sinkthefloat.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        setPreferencesFromResource(R.xml.preferences, p1)
        setPreferenceRestriction()
    }

    private fun setPreferenceRestriction() {
        val preference: EditTextPreference = findPreference("player_name")!!
        preference!!.setOnBindEditTextListener { editText ->
            changeEditTextLength(editText)
        }
    }

    private fun changeEditTextLength(editText: EditText) {
        editText.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        editText.selectAll()
        editText.filters = arrayOf<InputFilter>(LengthFilter(10))
    }

}