package moe.dreameh.lunchlista.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_fragment.*
import moe.dreameh.lunchlista.R
import moe.dreameh.lunchlista.vo.Status
import net.nightwhistler.htmlspanner.HtmlSpanner


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

        viewAdapter = RestaurantAdapter(mutableListOf(), context!!)
        viewManager = LinearLayoutManager(context)
        recycler_view.adapter = viewAdapter


        viewModel.getList().observe(this, Observer{
            when(it?.status) {
                Status.LOADING -> {
                    Log.d("ShowFragment", "--> Loading...")
                    tv_loading.visibility = View.VISIBLE
                    progress.visibility = View.VISIBLE
                    recycler_view.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    Log.d("showFragment", "--> Success! | loaded ${it.data?.restaurants?.size} entries")
                    tv_loading.visibility = View.GONE
                    progress.visibility = View.GONE
                    recycler_view.visibility = View.VISIBLE
                    recycler_view.apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = RestaurantAdapter(it.data!!.restaurants, context)
                    }
                    /*datum.text = HtmlSpanner().fromHtml(getString(R.string.placeholder_date, it.data!!.date,
                        it.data.day, it.data.week_number))*/

                    datum.text = HtmlSpanner().fromHtml("<b>Lunch för</b> ${it.data!!.date} (${it.data.day} " +
                            "| vecka: ${it.data.week_number})")
                }
                Status.ERROR -> {
                    Log.d("ShowFragment", "--> Error!")
                    toast("Error: ${it.message}")
                }
            }
        })



    }
}
