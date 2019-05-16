package moe.dreameh.lunchlista.ui

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zubair.alarmmanager.builder.AlarmBuilder
import com.zubair.alarmmanager.enums.AlarmType
import com.zubair.alarmmanager.interfaces.IAlarmListener
import kotlinx.android.synthetic.main.show_fragment.*
import moe.dreameh.lunchlista.R
import moe.dreameh.lunchlista.vo.Status
import net.nightwhistler.htmlspanner.HtmlSpanner


class ShowFragment : Fragment(), IAlarmListener {

    private lateinit var viewModel: ListViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private var builder: AlarmBuilder? = null


    private fun toast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

    override fun onResume() {
        var prefs = PreferenceManager.getDefaultSharedPreferences(context)
        super.onResume()
        builder?.addListener(this)
        if(!prefs.getBoolean("sync", false)) {
            builder = null
            Log.d("Sync", "De-activated")
        }
    }

    override fun onPause() {
        super.onPause()
        builder?.removeListener(this)
    }

    override fun perform(context: Context, intent: Intent) {
        var prefs = PreferenceManager.getDefaultSharedPreferences(context)
        viewModel.listUpdate()
        Log.d("Perform", "---> Activated")

        if(prefs.getBoolean("sync_notification", false)) {
            val currentDate = viewModel.getCurrentDate()

            val builder = NotificationCompat.Builder(context, "my_channel_id")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Ny Lunch Lista!")
                .setContentText("Lunch listan för $currentDate är nerladdad.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val notificationManager = NotificationManagerCompat.from(context)
            // notificationId is a unique int for each notification that you
            // must define
            notificationManager.notify(1, builder.build())
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var prefs = PreferenceManager.getDefaultSharedPreferences(context)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (prefs.getBoolean("sync", false)) {
                val calender: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 13)
                }

                builder = AlarmBuilder().with(context!!)
                    .setTimeInMilliSeconds(calender.timeInMillis)
                    .setId("UPDATE_INFO_SYSTEM_SERVICE")
                    .setAlarmType(AlarmType.REPEAT)

                Log.d("AlarmBuilder", "--> Activated!")
            } else {
                builder = null
                Log.d("AlarmBuilder", "--> De-activated!")
            }
        }


        viewAdapter = RestaurantAdapter(mutableListOf(), context!!)
        viewManager = LinearLayoutManager(context)
        recycler_view.adapter = viewAdapter


        viewModel.restaurantList.observe(this, Observer{
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
