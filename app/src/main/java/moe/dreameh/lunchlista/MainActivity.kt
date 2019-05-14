package moe.dreameh.lunchlista

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





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
}
