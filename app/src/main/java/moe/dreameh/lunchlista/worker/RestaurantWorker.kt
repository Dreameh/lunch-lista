package moe.dreameh.lunchlista.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.dreameh.lunchlista.api.CacheRepositoryProvider
import moe.dreameh.lunchlista.api.RestaurantApiService
import moe.dreameh.lunchlista.api.awaitResult
import moe.dreameh.lunchlista.api.getOrThrow
import moe.dreameh.lunchlista.persistence.Cache
import moe.dreameh.lunchlista.persistence.CacheRepository
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RestaurantWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private val repository: CacheRepository = CacheRepositoryProvider.provideCacheRepository()


    override fun doWork(): Result {
        var result: Cache? = null
        GlobalScope.launch {
            try {
                val restaurantListResponse = RetrofitCreate().getCache().awaitResult().getOrThrow()
                withContext(Dispatchers.Main) {
                    result = restaurantListResponse.cache
                    Log.d("Resource.success", restaurantListResponse.message)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    when(e) {
                        is HttpException -> {
                            Result.failure()
                        }
                        else -> {
                            Result.failure()
                        }
                    }
                }
                e.printStackTrace()
            }
        }

        return Result.success()
    }

    companion object Factory {
        fun RetrofitCreate(): RestaurantApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://mauritz.cloud/")
                .build()

            return retrofit.create(RestaurantApiService::class.java)
        }
    }
}