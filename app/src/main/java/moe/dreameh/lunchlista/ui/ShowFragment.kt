package moe.dreameh.lunchlista.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_fragment.*
import moe.dreameh.lunchlista.R
import net.nightwhistler.htmlspanner.HtmlSpanner
import java.lang.Thread.sleep


class ShowFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>


    private fun toast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.show_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewAdapter = RestaurantAdapter(viewModel.restaurants, context!!)
        sleep(300)
        viewAdapter.notifyDataSetChanged()
        viewManager = LinearLayoutManager(context)


        recycler_view.apply {
            // Improve performance for the recyclerview
            setHasFixedSize(true)
            // Apply the layoutManager for the recyclerView
            layoutManager = viewManager
            // Set adapter
            adapter = viewAdapter

        }

        sleep(300)
        if (viewModel.getDatum().date!!.isEmpty()) {
            datum.text =
                HtmlSpanner().fromHtml("<b>Lunch för</b> ${null} (${null} | ${null})")
        } else {
            datum.text =
                HtmlSpanner().fromHtml("<b>Lunch för</b> ${viewModel.getDatum().date} (${viewModel.getDatum().day} | ${viewModel.getDatum().week_number})")
        }

    }

}
