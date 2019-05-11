package moe.dreameh.lunchlista

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import moe.dreameh.lunchlista.ui.SettingsFragment
import moe.dreameh.lunchlista.ui.ShowFragment

class MainActivity : AppCompatActivity() {


    private inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.showFragment, fragment)
        fragmentTransaction.commit()
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(ShowFragment())
            }
            R.id.navigation_dashboard -> {
                replaceFragment(SettingsFragment())
            }
        }
        false


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when(resources?.configuration?.orientation) {
            ORIENTATION_PORTRAIT -> {
                nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            }
            else -> nav_view.isVisible = false
        }

    }
}
