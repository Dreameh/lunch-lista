package moe.dreameh.lunchlista.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.preference.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import moe.dreameh.lunchlista.BuildConfig
import moe.dreameh.lunchlista.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val version: Preference? = findPreference("build_version")
        version?.summary = BuildConfig.VERSION_NAME

        val version_code: Preference? = findPreference("build_version_code")
        version_code?.summary = BuildConfig.VERSION_CODE.toString()

        val about: Preference? = findPreference("about")
        about?.setOnPreferenceClickListener {
            val string = "Denna applikation är framtagen av:\nLucas Pentinsaari\n\nBaserat på:\nhttps://lunch.mauritzonline.com/\n\n" +
                    "API gjord av:\nMauritz Nilsson\n" +
                    "\nVid problem skicka ett mail till:\ndreameh@protonmail.com"
            MaterialDialog(context!!).show {
                customView(R.layout.custom_view)
                val customViews = getCustomView()
                title(text = "Om applikationen")
                val textView: TextView = customViews.findViewById(R.id.textView2)
                textView.text = string
                positiveButton(R.string.close)
            }
            true
        }

        val notification = findPreference<PreferenceCategory>("notification")
        val sync = findPreference<SwitchPreferenceCompat>("sync")
        notification?.isVisible = false


        sync?.setOnPreferenceClickListener {
            notification?.isVisible = sync.isChecked
            true
        }


        val filter = findPreference<SwitchPreferenceCompat>("filter")
        val filterBy = findPreference<MultiSelectListPreference>("filter-by")
        filterBy?.isVisible = false

        filter?.setOnPreferenceClickListener {
            filterBy?.isVisible = filter.isChecked
            true
        }

    }
}

