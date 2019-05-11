package moe.dreameh.lunchlista

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
                replaceFragment(SettingsActivity.SettingsFragment())
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
