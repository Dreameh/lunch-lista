package moe.dreameh.lunchlista.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import moe.dreameh.lunchlista.api.CacheRepositoryProvider
import moe.dreameh.lunchlista.persistence.Cache
import moe.dreameh.lunchlista.persistence.CacheRepository
import moe.dreameh.lunchlista.vo.Resource

class ListViewModel : ViewModel() {

    private val repository: CacheRepository = CacheRepositoryProvider.provideCacheRepository()

    fun getList(): MutableLiveData<Resource<Cache>> {
        return repository.getRestaurantList()
    }
}
