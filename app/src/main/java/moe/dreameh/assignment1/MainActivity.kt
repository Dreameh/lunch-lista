package moe.dreameh.assignment1


import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tb: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tb)
        when (resources?.configuration?.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {

                supportActionBar?.show()
            }
            else -> supportActionBar?.hide()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.settings_menu -> {
                // start settings activity
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                return true
            }
            R.id.about_menu -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
