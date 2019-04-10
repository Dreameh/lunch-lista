package moe.dreameh.assignment1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_advice.*

class AddAdvice : AppCompatActivity() {
    private var bundle = Bundle()
    private val category = arrayOfNulls<String>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_advice)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                android.R.layout.simple_spinner_item).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            category_spinner.adapter = adapter
        }

        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category[0] = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nothing will be added here.
            }
        }

        // Set cancel result
        button_cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        button_create.setOnClickListener {
            when {
                enter_name.text.isEmpty() -> enter_name.error = "Field cannot be left blank."
                enter_content.text.isEmpty() -> enter_content.error = "Field cannot be left blank."
                else -> {
                    // Add the contents of the fields into the bundle.
                    addAdviceBundle()

                    setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
                    finish()
                }
            }
        }
    }

    private fun addAdviceBundle() {
        // Insert all the content into the bundle.
        with (bundle) {
            putString("AUTHOR", enter_name.text.toString())
            putString("CONTENT", enter_content.text.toString())
            putString("CATEGORY", category[0])
        }
    }
}
