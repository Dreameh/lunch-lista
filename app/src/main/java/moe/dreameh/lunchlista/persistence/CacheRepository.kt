package moe.dreameh.lunchlista.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.dreameh.lunchlista.api.RestaurantApiService
import moe.dreameh.lunchlista.api.awaitResult
import moe.dreameh.lunchlista.api.getOrThrow
import moe.dreameh.lunchlista.vo.Resource
import retrofit2.HttpException

class CacheRepository(private val apiService: RestaurantApiService) {

    fun getRestaurantList(): MutableLiveData<Resource<Cache>> {
        val result = MutableLiveData<Resource<Cache>>()
        result.setValue(Resource.loading((null)))
        val client = apiService
        GlobalScope.launch {
            try {
                val restaurantListResponse = client.getCache().awaitResult().getOrThrow()
                withContext(Dispatchers.Main) {
                    result.value = Resource.success(restaurantListResponse.cache)
                    Log.d("Resource.success", restaurantListResponse.message)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    when(e) {
                        is HttpException -> {
                            result.value = Resource.error("${e.message} | code ${e.response().code()}",
                                Cache("0", 0, "0", 0, mutableListOf()))
                        }
                        else -> result.value = Resource.error("${e.message}", Cache("0",
                            0, "0", 0, mutableListOf()))
                    }
                }
                e.printStackTrace()
            }
        }
        return result
    }


}