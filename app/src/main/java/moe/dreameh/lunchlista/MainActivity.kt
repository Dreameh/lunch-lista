package moe.dreameh.lunchlista

import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import net.nightwhistler.htmlspanner.HtmlSpanner

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkFirstRun()

        // Setting up the bottom navigation as the navigation.
        when(resources?.configuration?.orientation) {
            ORIENTATION_PORTRAIT -> {
                navController = Navigation.findNavController(this, R.id.nav_host_fragment)
                nav_view.setupWithNavController(navController)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun checkFirstRun() {
        val prefName = "SharedPrefs"
        val prefVersionKeyCode = "version_code"
        // Getting the current version code
        val currentVersionCode = BuildConfig.VERSION_CODE

        // Get saved version code
        val prefs = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val savedVersionCode = prefs.getInt(prefVersionKeyCode, -1)

        // Check for first run or upgrade
        when {
            currentVersionCode == savedVersionCode -> {
                Log.d("Version", "--> No change")
            }
            savedVersionCode == -1 -> {
                MaterialDialog(this).show {
                    title(R.string.welcome_title)
                    message(R.string.welcome_message)
                    positiveButton(R.string.close)
                }
                Log.d("Version", "--> First run")
            }
            else -> {
                Log.d("Version", "--> Upgrade detected...")
                MaterialDialog(this).show {
                    title(R.string.update_title)
                    message(text = getString(R.string.update_message, BuildConfig.VERSION_NAME, HtmlSpanner().fromHtml(
                        getString(R.string.updates))))
                    positiveButton(R.string.close)
                }
            }
        }

        prefs.edit().putInt(prefVersionKeyCode, currentVersionCode).apply()
    }
}
