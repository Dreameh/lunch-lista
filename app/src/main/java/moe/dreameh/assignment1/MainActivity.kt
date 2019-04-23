package moe.dreameh.assignment1


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import moe.dreameh.assignment1.room.AdviceDatabase


class MainActivity : AppCompatActivity() {
    private var adviceDatabase: AdviceDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tb: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tb)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.settings_menu -> consume { startActivity(Intent(this, SettingsActivity::class.java)) }
        R.id.about_menu -> consume { startActivity(Intent(this, AboutActivity::class.java)) }
        else -> super.onOptionsItemSelected(item)
    }
}
