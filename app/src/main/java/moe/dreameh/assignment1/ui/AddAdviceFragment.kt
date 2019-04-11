package moe.dreameh.assignment1.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.add_advice_fragment.*
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.R

class AddAdviceFragment : Fragment() {


    private lateinit var viewModel: SharedViewModel
    private val category = arrayOfNulls<String>(1)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.i("AddAdviceFragment", "Activated")
        val rootView = inflater.inflate(R.layout.add_advice_fragment, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("AddAdviceFragment", "onActivityCreated")
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        button_cancel.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_addAdviceFragment_to_startFragment)
            Toast.makeText(context, "No advice was added.",
                    Toast.LENGTH_LONG).show()
        }
        button_create.setOnClickListener {
            when {
                enter_name.text.isEmpty() -> enter_name.error = "Field cannot be left blank."
                enter_content.text.isEmpty() -> enter_content.error = "Field cannot be left blank."
                else -> {
                    // Add a new advice to the obvservable adviceList
                    viewModel.addNewAdvice(Advice(
                            enter_name.text.toString(),
                            category[0],
                            enter_content.text.toString()))

                    Toast.makeText(context, "A new advice has been" + " added.", Toast.LENGTH_LONG).show()
                    Navigation.findNavController(it).navigate(R.id.action_addAdviceFragment_to_startFragment)
                }
            }
        }

        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                category[0] = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nothing will be added here.
            }
        }

        ArrayAdapter.createFromResource(
                context,
                R.array.add_categories,
                android.R.layout.simple_spinner_item).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            category_spinner.adapter = adapter
        }
    }

}
