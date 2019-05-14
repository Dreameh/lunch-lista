package moe.dreameh.lunchlista.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class RestaurantWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {


    override fun doWork(): Result {


        return Result.success()
    }

}