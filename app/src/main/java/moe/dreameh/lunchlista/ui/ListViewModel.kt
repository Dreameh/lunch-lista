package moe.dreameh.lunchlista.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import moe.dreameh.lunchlista.api.CacheRepositoryProvider
import moe.dreameh.lunchlista.persistence.Cache
import moe.dreameh.lunchlista.persistence.CacheRepository
import moe.dreameh.lunchlista.vo.Resource

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CacheRepository = CacheRepositoryProvider.provideCacheRepository()
    var restaurantList = repository.getRestaurantList()

    fun listUpdate() {
        if(repository.getRestaurantList().value!!.data!!.timestamp > restaurantList.value!!.data!!.timestamp) {
            restaurantList = repository.getRestaurantList()
        }
    }

    fun getCurrentDate(): String? {
        return restaurantList.value!!.data!!.date
    }
}
