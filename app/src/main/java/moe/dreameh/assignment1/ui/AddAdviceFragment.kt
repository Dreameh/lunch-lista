package moe.dreameh.assignment1.ui


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.add_advice_fragment.*
import kotlinx.android.synthetic.main.content_about.*
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.R
import moe.dreameh.assignment1.room.AdviceDatabase

class AddAdviceFragment : Fragment() {


    private lateinit var viewModel: SharedViewModel
    private lateinit var categoryModel: CategoryViewModel
    private val category = arrayOfNulls<String>(1)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.add_advice_fragment, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        categoryModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)

        // Disabling the "CANCEL" button for when it's not in portrait mode.
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            button_cancel.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_addAdviceFragment_to_startFragment)
                Toast.makeText(context, "No advice was added.",
                        Toast.LENGTH_LONG).show()
            }
        }

        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            button_cancel.isEnabled = false
        }

        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val currentValue: String? = sharedPreferences.getString("author", "Not Set")
        val selectedItem: String? = sharedPreferences.getString("list", "")

        selected_name.text = currentValue


        // Clicklistener for the "OK" button
        button_create.setOnClickListener {
            when {
                enter_content.text.isEmpty() -> enter_content.error = "Field cannot be left blank."
                else -> {
                    // Add a new advice to the obvservable adviceList
                    viewModel.insert(Advice(
                            currentValue,
                            category[0],
                            enter_content.text.toString()))

                    Toast.makeText(context, "A new advice has been" + " added.", Toast.LENGTH_LONG).show()
                    // Making sure that it will only navigate when the orientation is in portrait mode.
                    if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Navigation.findNavController(it).navigate(R.id.action_addAdviceFragment_to_startFragment)
                    }
                }
            }
        }
        // Setting up the listener for the category_spinner
        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category[0] = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nothing will be added here.
            }
        }
        // Adding all the items from "categories" string array to the category spinner
        categoryModel.nameList.observe(this, Observer {
            ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_item,
                    it).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                category_spinner.adapter = adapter

                category_spinner.setSelection(it.indexOf(selectedItem))
            }
        })

    }

}
