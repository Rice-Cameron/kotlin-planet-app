package edu.oregonstate.cs492.final_project_team_20.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import edu.oregonstate.cs492.final_project_team_20.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}