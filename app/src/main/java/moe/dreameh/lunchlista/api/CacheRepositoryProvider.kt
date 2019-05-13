package moe.dreameh.lunchlista.api

import moe.dreameh.lunchlista.persistence.CacheRepository

object CacheRepositoryProvider {
    fun provideCacheRepository(): CacheRepository {
        return CacheRepository(RestaurantApiService.Factory.create())
    }
}