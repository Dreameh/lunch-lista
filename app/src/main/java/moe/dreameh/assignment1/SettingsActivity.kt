package moe.dreameh.assignment1


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.*
import moe.dreameh.assignment1.ui.CategoryViewModel
import moe.dreameh.assignment1.ui.SharedViewModel

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var categoryModel: CategoryViewModel

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            //setPreferencesFromResource(R.xml.root_preferences, rootKey)
            categoryModel = activity?.run {
                ViewModelProviders.of(this).get(CategoryViewModel::class.java)
            } ?: throw Exception("Invalid Activity")

            val context = preferenceManager.context
            val screen = preferenceManager.createPreferenceScreen(context)

            // EditText Preference settings
            val authorTitle = EditTextPreference(context)
            authorTitle.key = "author"
            authorTitle.title = "Author Name"
            authorTitle.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
            authorTitle.dialogTitle = "Author"

            val categoryList = ListPreference(context)
            categoryList.key = "list"
            categoryList.title = "Default Category"
            categoryList.dialogTitle = "Categories"
            categoryList.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
            categoryList.entries = categoryModel.fetchAllNames().toTypedArray()
            categoryList.entryValues = categoryModel.fetchAllNames().toTypedArray()

            val basicsCategory = PreferenceCategory(context)
            basicsCategory.key = "basics"
            basicsCategory.title ="Basics"
            screen.addPreference(basicsCategory)
            basicsCategory.addPreference(authorTitle)
            basicsCategory.addPreference(categoryList)

            preferenceScreen = screen
        }

    }
}

