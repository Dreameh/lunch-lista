package moe.dreameh.assignment1.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import kotlinx.android.synthetic.main.start_fragment.*
import moe.dreameh.assignment1.room.Advice
import moe.dreameh.assignment1.AdviceAdapter
import moe.dreameh.assignment1.R
import moe.dreameh.assignment1.api.PeriodicWorker
import moe.dreameh.assignment1.room.SharedViewModel
import java.util.concurrent.TimeUnit

class StartFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support
        // library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MY CHANNEL NAME"
            val description = "MY CHANNEL DESCRIPTION"

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("my_channel_id", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change
            // the importance or other notification behaviors after this
            val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.start_fragment, container, false)
        // Re-initialize viewModel taking the instance from the Activity
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createNotificationChannel()

        // Initialize LinearLayoutManager
        viewManager = LinearLayoutManager(context)

        // Not working Part of the code
        val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val workManager = WorkManager.getInstance()

        viewModel.bigList.observe(this, Observer<MutableList<Advice>> { list ->

            // get objects to the recyclerView
            viewAdapter = AdviceAdapter(list)
            viewAdapter.notifyItemInserted(0)

            recycler_view.apply {
                // Improve performance for the recyclerview
                setHasFixedSize(true)
                // Apply the layoutManager for the recyclerView
                layoutManager = viewManager
                // Set adapter
                adapter = viewAdapter

            }

            // Not working Part of the code
            val input = Data.Builder()
                    .putInt("ADVICE", list.size)
                    .build()

            val request = PeriodicWorkRequestBuilder<PeriodicWorker>(viewModel.getInterval(), TimeUnit.MINUTES)
                    .setInputData(input)
                    .setConstraints(constraint)
                    .build()

            workManager.enqueueUniquePeriodicWork("SYNC",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    request)

            val observableWorkinfo = workManager.getWorkInfoByIdLiveData(request.id)

            observableWorkinfo.observe(this, Observer {
                if (it != null && it.state == WorkInfo.State.SUCCEEDED) {
                    var output = it.outputData.getInt("ADVICE", 0)

                    if (output > 0) {

                        val builder = NotificationCompat.Builder(context!!, "my_channel_id")
                                .setSmallIcon(android.R.drawable.star_on)
                                .setContentTitle("NiksiPirkka Syncing Completed")
                                .setContentText("found $output new advices")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        val notificationManager = NotificationManagerCompat.from(context!!)
                        // notificationId is a unique int for each notification that you
                        // must define
                        notificationManager.notify(1, builder.build())

                        viewModel.scheduledFetching()
                    }
                }
            })
        })




        viewModel.categories.observe(this, Observer {
            ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_item,
                    it).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter

                spinner.post(kotlinx.coroutines.Runnable {
                    spinner.setSelection(viewModel.getDefaultCategoryFor())
                })
            }
        })

        // Using the spinner to swap adapters.
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, views: View?, position: Int, id: Long) {
                // Getting the category spinner item and checking if it's related to "All" or the rest of them.
                when (parent?.getItemAtPosition(position).toString()) {
                    "All" -> recycler_view.adapter = AdviceAdapter(viewModel.bigList.value!!)
                    else -> {
                        recycler_view.adapter = AdviceAdapter(viewModel.filterAdvice(parent?.getItemAtPosition(position).toString()) as MutableList<Advice>)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing will be added here.
            }
        }

        when (activity?.resources?.configuration?.orientation) {
            ORIENTATION_PORTRAIT -> {
                fab_button.setOnClickListener {
                    it.findNavController().navigate(R.id.action_startFragment_to_addAdviceFragment)
                }
            }
            else -> fab_button.hide()
        }
    }
}
