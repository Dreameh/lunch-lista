package moe.dreameh.lunchlista.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.show_fragment.*
import moe.dreameh.lunchlista.R
import moe.dreameh.lunchlista.persistence.Restaurant


class ShowFragment : Fragment() {

    companion object {
        fun newInstance() = ShowFragment()
    }

    private lateinit var viewModel: ListViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var typeface: Typeface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        viewManager = LinearLayoutManager(context)



        val list = mutableListOf(Restaurant(
            1,
            "https://www.aland.com/img/lunchguiden/62.png?ts=1486380764",
            "60 North Bar & Cafe",
            " ",
            "Mariehamn Airport, Flygfältsvägen 67, 22150 Jomala",
            "0358 44 7262000",
            "<ul><li>Baconlindad kycklingfilé med vitlökssås & klyftpotatis <em>(G)</em> - 11.00€</li><li>Sallad med sweetchilikyckling, glasnudlar & apelsin <em>(G)</em> - 11.00€</li><li>Tacochips med tacofärs, majs & ost <span>Serveras med liten sallad</span> <em>(G, L)</em> - 9.90€</li><li>Rotsaksgratäng <span>Serveras med liten sallad & vitlökscréme</span> <em>(G, V)</em> - 11.00€</li></ul>"
        ))


        viewAdapter = RestaurantAdapter(list, context!!)
        recycler_view.apply {
            // Improve performance for the recyclerview
            setHasFixedSize(true)
            // Apply the layoutManager for the recyclerView
            layoutManager = viewManager
            // Set adapter
            adapter = viewAdapter


        }

    }

}
