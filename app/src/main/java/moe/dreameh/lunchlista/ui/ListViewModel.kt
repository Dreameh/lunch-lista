package moe.dreameh.lunchlista.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.dreameh.lunchlista.api.CacheRepositoryProvider
import moe.dreameh.lunchlista.persistence.CacheRepository
import moe.dreameh.lunchlista.persistence.Restaurant
import kotlin.coroutines.CoroutineContext

class ListViewModel : ViewModel() {

    // Coroutine tasker?
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: CacheRepository = CacheRepositoryProvider.provideCacheRepository()
    var restaurants: MutableList<Restaurant> = mutableListOf()

    init {
        loadRestaurants()
    }

    private fun insert(restaurant: Restaurant) {
        restaurants.add(restaurant)
    }

    fun loadRestaurants() = scope.launch(IO) {
        val request = repository.getCache()
        try {
            val response = request.await()
            if (response.isSuccessful) {
                val results = response.body()!!
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
}
