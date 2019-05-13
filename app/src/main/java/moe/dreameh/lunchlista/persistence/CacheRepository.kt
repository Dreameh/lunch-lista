package moe.dreameh.lunchlista.persistence

import kotlinx.coroutines.Deferred
import moe.dreameh.lunchlista.api.RestaurantApiService
import retrofit2.Response

class CacheRepository(private val apiService: RestaurantApiService) {
    fun getCache() : Deferred<Response<Results>> {
        return apiService.getCache()
    }



}