package moe.dreameh.lunchlista.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import moe.dreameh.lunchlista.api.CacheRepositoryProvider
import moe.dreameh.lunchlista.persistence.CacheRepository
import moe.dreameh.lunchlista.persistence.Date
import moe.dreameh.lunchlista.persistence.Restaurant

class ListViewModel : ViewModel() {

    private val repository: CacheRepository = CacheRepositoryProvider.provideCacheRepository()
    var restaurants: MutableList<Restaurant> = mutableListOf()
    var datum: MutableList<Date>

    init {
        restaurants = repository.restaurants
        datum = repository.datum
        Log.d("Datum size", "$datum.size")
    }

    fun getDatum(): Date = when (datum.isEmpty()) {
        true -> Date("", 0, "")
        else -> datum.first()
    }


}
