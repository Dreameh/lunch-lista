package moe.dreameh.assignment1.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.IOException


class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
      val retrofit: Retrofit = Retrofit.Builder()
              .baseUrl("http://niksipirkka.co.nf/")
              .addConverterFactory(GsonConverterFactory.create())
              .build()

        val service = retrofit.create(AdviceService::class.java)
        val catService = retrofit.create(CategoryService::class.java)
        val call = service.run { loadAdvices() }
        val call2 = catService.run { loadCategories() }

        try {
            val response = call.execute()
            val response2 = call2.execute()

            // Show notification
             val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "my_channel_id")
                    .setSmallIcon(android.R.drawable.star_on)
                    .setContentTitle("Loading Completed")
                    .setContentText("Found " + response.body()!!.size + " advices." + "\n" +
                    "Found " + response2.body()!!.size + " categories")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)



            val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(applicationContext)
            // notificationId is a unique int for each notification that you
            // must define
            notificationManager.notify(1, builder.build())

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Result.success()
    }

    fun sendCategories():

}