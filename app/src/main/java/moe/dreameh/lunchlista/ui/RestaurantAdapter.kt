package moe.dreameh.lunchlista.ui

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_list_row.view.*
import moe.dreameh.lunchlista.R
import moe.dreameh.lunchlista.persistence.Restaurant

class RestaurantAdapter(private var restaurantList: MutableList<Restaurant>?) : RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.restaraunt_name
        val image = view.restaurant_logo
        val menu = view.restaurant_menu

        fun updateWithUrl(url: String) {
            Picasso.get().load(url).into(this.image)
        }
    }
    // Essentially set the XML Layout advice_list_row as the layout for a "row"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.restaurant_list_row, parent, false))
    }

    // Set ViewHolder variables to the list's item variables
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(restaurantList?.get(position)) {
            holder?.name.text = this?.name
            holder?.menu.text = Html.fromHtml(this?.menu)
            holder?.updateWithUrl(this?.image as String)

        }
        //notifyDataSetChanged()
    }

    //Get size of restaurantlist
    override fun getItemCount() = restaurantList!!.size
}