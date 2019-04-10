package moe.dreameh.assignment1.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_add_advice.*
import kotlinx.android.synthetic.main.advice_list_row.*
import moe.dreameh.assignment1.Advice

import moe.dreameh.assignment1.R

class AddAdviceFragment : Fragment() {

    private var viewModel: SharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.add_advice_fragment, container, false)
        val category = arrayOfNulls<String>(1)

        setupCategorySpinner(rootView.context)

        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category[0] = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nothing will be added here.
            }
        }

        button_cancel.setOnClickListener {
            Navigation.findNavController(rootView).navigate(R.id.action_addAdviceFragment_to_startFragment)
            Toast.makeText(context, "No advice was added.",
                    Toast.LENGTH_LONG).show()
        }
        button_create.setOnClickListener {
            when {
                enter_name.text.isEmpty() -> enter_name.error = "Field cannot be left blank."
                enter_content.text.isEmpty() -> enter_content.error = "Field cannot be left blank."
                else -> {
                    // Add a new advice to the obvservable adviceList
                    viewModel.setNewAdvice(Advice(
                            enter_name.text.toString(),
                            category[0],
                            enter_content.text.toString()))

                    Toast.makeText(context, "A new advice has been" + " added.", Toast.LENGTH_LONG).show()
                }
            }
            Navigation.findNavController(rootView).navigate(R.id.action_addAdviceFragment_to_startFragment)
        }
        return rootView
    }

    private fun setupCategorySpinner(context: Context) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                context,
                R.array.categories,
                android.R.layout.simple_spinner_item).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            category_spinner.adapter = adapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
