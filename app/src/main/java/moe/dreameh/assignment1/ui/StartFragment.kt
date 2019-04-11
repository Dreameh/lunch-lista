package moe.dreameh.assignment1.ui

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.start_fragment.*
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.AdviceAdapter
import moe.dreameh.assignment1.R

class StartFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.i("StartFragment", "Activated")
        val rootView = inflater.inflate(R.layout.start_fragment, container, false)
        // Re-initialize viewModel taking the instance from the Activity
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        // get objects to the recyclerView
        viewAdapter = AdviceAdapter(viewModel.getAdvices().value)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Initialize LinearLayoutManager
        viewManager = LinearLayoutManager(context)

        recycler_view.apply {
            // Improve performance for the recyclerview
            setHasFixedSize(true)
            // Apply the layoutManager for the recyclerView
            layoutManager = viewManager
            // Set adapter
            adapter = viewAdapter
        }

        // Setting up categories spinner
        ArrayAdapter.createFromResource(
                context!!,
                R.array.categories,
                android.R.layout.simple_spinner_item).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        // Using the spinner to swap adapters.
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, views: View?, position: Int, id: Long) {
                // Getting the category spinner item and checking if it's related to "All" or the rest of them.
                when(parent?.getItemAtPosition(position).toString()) {
                    "All" -> recycler_view.adapter = AdviceAdapter(viewModel.getAdvices().value!!)
                    else -> {
                        recycler_view.adapter = AdviceAdapter(viewModel.filterAdvice(parent?.getItemAtPosition(position).toString()) as MutableList<Advice>)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing will be added here.
            }
        }

        when (activity?.resources?.configuration?.orientation) {
            ORIENTATION_PORTRAIT -> {
                fab_button.setOnClickListener {
                    it.findNavController().navigate(R.id.action_startFragment_to_addAdviceFragment)
                }
            }
            else -> fab_button.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAdvices().observe(this, Observer<MutableList<Advice>> {
            /*
            * if the size of this fragment's viewModel is less than the size of the observable one
            * add the latest to the mix.
             */
            if(viewModel.getAdvices().value!!.size < it.size)
                viewModel.addNewAdvice(it[it.size])
        })
    }

}
