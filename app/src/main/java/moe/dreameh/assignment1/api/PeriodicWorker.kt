package moe.dreameh.assignment1.api

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

// Data keys
const val ADVICE = "ADVICE"


class PeriodicWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    private var result: Int = 0

    private fun setWorkerResult(amount: Int) {
        result = amount
    }

    override fun doWork(): Result {
        val service = RetrofitFactory.makeRetrofitService()
        val request = service.loadAdvices()
        val input: Data = inputData
        try {
            val job = GlobalScope.launch {
                val response = request.await()
                if (response.isSuccessful) {
                    setWorkerResult(response.body()!!.size - input.getInt("ADVICE", 0))
                }
            }
            job.start()
        } catch (e: IOException) {
            e.printStackTrace()
            return Result.failure()
        }

        val output = Data.Builder()
                .putInt(ADVICE, result)
                .build()
        return Result.success(output)
    }

}