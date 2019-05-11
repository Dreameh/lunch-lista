package moe.dreameh.lunchlista.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_list_row.view.*
import moe.dreameh.lunchlista.R
import moe.dreameh.lunchlista.persistence.Restaurant
import net.nightwhistler.htmlspanner.HtmlSpanner

class RestaurantAdapter(private var restaurantList: MutableList<Restaurant>?, private var context: Context) : RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.restaraunt_name
        val image = view.restaurant_logo
        val menu = view.restaurant_menu
        val address = view.mapButton
        val phone = view.phoneButton

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
            holder.name.text = this?.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //holder.menu.text = Html.fromHtml(this?.menu, 0)
                holder.menu.text = HtmlSpanner().fromHtml(this?.menu)
            }
            //holder.updateWithUrl(this?.image as String)
            Picasso.get().load(this?.image).into(holder.image)

            val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=" + this?.address)

            holder.address.setOnClickListener {
                startActivity(context, Intent(Intent.ACTION_VIEW, gmmIntentUri), null)
            }

            holder.phone.setOnClickListener {
                startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + this?.phone)), null)
            }
        }
    }

    //Get size of restaurantlist
    override fun getItemCount() = restaurantList!!.size
}