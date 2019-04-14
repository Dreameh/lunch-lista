package moe.dreameh.assignment1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.advice_list_row.view.*


class AdviceAdapter(private val adviceList: MutableList<Advice>?) : RecyclerView.Adapter<AdviceAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val author = view.author
        val content = view.content
        val category = view.category
    }

    // Essentially set the XML Layout advice_list_row as the layout for a "row"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.advice_list_row, parent, false)

        return MyViewHolder(itemView)
    }

    // Set ViewHolder variables to the list's item variables
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(adviceList?.get(position)) {
            holder.author.text = this?.author
            holder.category.text = this?.category
            holder.content.text = this?.content
        }
    }

    //Get size of advicelist
    override fun getItemCount() = adviceList!!.size

    fun removeAt(position: Int) {
        adviceList?.removeAt(position)
        notifyItemRemoved(position)

    }
}
