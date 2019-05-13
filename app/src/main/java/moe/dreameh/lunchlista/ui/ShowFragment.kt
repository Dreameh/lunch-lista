package moe.dreameh.lunchlista.ui

import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_fragment.*
import moe.dreameh.lunchlista.R


class ShowFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>


    private fun toast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        return inflater.inflate(R.layout.show_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var progress = ProgressBar(context)
        progress.isIndeterminate = true
        progress.progress = 0

        viewManager = LinearLayoutManager(context)


        // A good wait for the list is all the adapter would need
        kotlin.run {
            viewAdapter = RestaurantAdapter(viewModel.restaurants, context!!)
            Log.d("viewAdapter", "${viewAdapter.itemCount}")
            sleep(400)
        }

        recycler_view.apply {
            // Improve performance for the recyclerview
            setHasFixedSize(true)
            // Apply the layoutManager for the recyclerView
            layoutManager = viewManager
            // Set adapter
            adapter = viewAdapter
            Log.d("viewAdapter", "${viewAdapter.itemCount}")
        }

        Log.d("viewAdapter", "${viewAdapter.itemCount}")
    }

}
