package moe.dreameh.lunchlista.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import moe.dreameh.lunchlista.api.CacheRepositoryProvider
import moe.dreameh.lunchlista.persistence.CacheRepository

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CacheRepository = CacheRepositoryProvider.provideCacheRepository()
    var restaurantList = repository.getRestaurantList()
}
