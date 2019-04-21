package moe.dreameh.assignment1


import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*


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
}
