package moe.dreameh.assignment1.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.room.AdviceDatabase
import moe.dreameh.assignment1.room.CategoryRepository
import kotlin.coroutines.CoroutineContext

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val catRepository: CategoryRepository
    var nameList: LiveData<MutableList<String>>

    init {
        val categoryDao = AdviceDatabase.getDatabase(application, scope).categoryDao()
        catRepository = CategoryRepository(categoryDao)
        nameList = catRepository.allNames
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}