package moe.dreameh.lunchlista.persistence

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import moe.dreameh.lunchlista.api.RestaurantApiService
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class CacheRepository(private val apiService: RestaurantApiService) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    init {
        loadRestaurants()
    }

    var restaurants: MutableList<Restaurant> = mutableListOf()
    var datum: MutableList<Date> = ArrayList()


    fun getCache() : Deferred<Response<Results>> {
        return apiService.getCache()
    }




    private fun loadRestaurants() = scope.launch(IO) {
        val request = getCache()
        try {
            val response = request.await()
            if (response.isSuccessful) {
                val results = response.body()!!
                insertDatum(Date(results.cache.day, results.cache.week_number, results.cache.date))
                results.cache.restaurants.forEach { restaurant ->
                    insert(restaurant)
                }
                Log.d("Result", "Restaurants List: ${restaurants.size}")
            } else {
                Log.d("Error", "${response.code()}")
            }
        } catch (e: Throwable) {
            Log.d("Error", "${e.message}")
        }
        Log.d("Result", "Restaurant: ${restaurants.get(0)}")
    }

    private fun insert(restaurant: Restaurant) {
        restaurants.add(restaurant)
    }

    private fun insertDatum(date: Date) {
        datum.add(date)
    }

}