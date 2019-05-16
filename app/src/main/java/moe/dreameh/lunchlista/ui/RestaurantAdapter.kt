package moe.dreameh.lunchlista.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.restaurant_list_row.view.*
import moe.dreameh.lunchlista.R
import moe.dreameh.lunchlista.persistence.Restaurant

class RestaurantAdapter(private var restaurantList: MutableList<Restaurant>, private var context: Context) :
    RecyclerView.Adapter<RestaurantAdapter.MyViewHolder>() {

    // Essentially set the XML Layout advice_list_row as the layout for a "row"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.restaurant_list_row, parent, false)
        )
    }

    // Set ViewHolder variables to the list's item variables
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(restaurantList[position]) {
            holder.bindItems(this, context)
        }
    }

    //Get size of restaurantList
    override fun getItemCount() = restaurantList.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.restaraunt_name
        val image = view.restaurant_logo
        val menu = view.restaurant_menu
        val address = view.mapButton
        val phone = view.phoneButton
        val info = view.infoButton

        fun bindItems(restaurant: Restaurant, context: Context) {
            this.name.text = restaurant.name

            this.menu.text = restaurant.menu

            var imgUrl: String? = restaurant.image

            if (imgUrl != null && imgUrl.trim().isEmpty()) {
                imgUrl = null
            }

            // Load image from the link provided from the cache.
            Picasso.get().load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .into(this.image)

            // Setup an Intent with the location provided by the Cache.
            val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=" + restaurant.address)

            // Start activity based upon the data given above.
            this.address.setOnClickListener {
                startActivity(context, Intent(Intent.ACTION_VIEW, gmmIntentUri), null)
            }

            // Starting activity with the Intent of reaching the phone dialer.
            this.phone.setOnClickListener {
                startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + restaurant.phone)), null)
            }

            // Adding a info button ClickListener so that when the user clicks, a dialog with information will appear.
            this.info.setOnClickListener {
                MaterialDialog(context).show {
                    customView(R.layout.custom_view)

                    val customViews = getCustomView()
                    title(text = restaurant.name)
                    val textView: TextView = customViews.findViewById(R.id.textView2)
                    textView.text = "${restaurant.info}\nWebbsida: ${restaurant.website}"

                    positiveButton(R.string.close)
                }
            }
        }
    }
}