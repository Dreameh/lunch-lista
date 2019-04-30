package moe.dreameh.assignment1.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import moe.dreameh.assignment1.R
import moe.dreameh.assignment1.room.CategoryRepository

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var categoryModel: CategoryViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        categoryModel = activity?.run {
            ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val list: ListPreference? = findPreference("default_category")



        categoryModel.nameList.observe(this, Observer {
            list?.entries = it.toTypedArray()
            list?.entryValues = it.toTypedArray()
        })
        list?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
    }

}