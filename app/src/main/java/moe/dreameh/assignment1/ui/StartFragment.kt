package moe.dreameh.assignment1.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
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
        val rootView  = inflater.inflate(R.layout.start_fragment, container, true)

        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        // get objects to the recyclerView
        viewAdapter = AdviceAdapter(viewModel.getAdvices().value)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view.apply {
            // Improve performance for the recyclerview
            setHasFixedSize(true)
            // Apply the layoutManager for the recyclerView
            layoutManager = viewManager
            // Set adapter
            adapter = viewAdapter
        }

        fab_button?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_addAdviceFragment))

        ArrayAdapter.createFromResource(
                context!!,
                R.array.categories,
                android.R.layout.simple_spinner_item).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Getting the category spinner item and checking if it's related to "All" or the rest of them.
                if(parent.getItemAtPosition(position).toString() == "All") {
                    recycler_view.adapter = AdviceAdapter(viewModel.getAdvices().value!!)
                } else {
                    recycler_view.adapter = AdviceAdapter(viewModel.filterAdvice(parent.getItemAtPosition(position).toString()) as MutableList<Advice>)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nothing will be added here.
            }
        }
    }

    override fun onDestroyView() {
        if(view != null) {
            val parentViewGroup = view?.parent as ViewGroup?
            parentViewGroup?.removeAllViews()
        }
        super.onDestroyView()
    }


}
