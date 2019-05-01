package moe.dreameh.assignment1.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import moe.dreameh.assignment1.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var viewModel: SharedViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val list: ListPreference? = findPreference("default_category")



        viewModel.categories.observe(this, Observer {
            list?.entries = it.toTypedArray()
            list?.entryValues = it.toTypedArray()
            list?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        })

    }

}