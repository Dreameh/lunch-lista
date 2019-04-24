package moe.dreameh.assignment1.ui

import android.app.Application
import android.util.Log
import androidx.core.view.OneShotPreDrawListener.add
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.Category
import moe.dreameh.assignment1.room.AdviceDao
import moe.dreameh.assignment1.room.AdviceDatabase
import moe.dreameh.assignment1.room.AdviceRepository
import moe.dreameh.assignment1.room.CategoryRepository
import java.util.Locale.filter
import kotlin.coroutines.CoroutineContext

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    private val repository: AdviceRepository
    val bigList: LiveData<MutableList<Advice>>


    init {
        val adviceDao = AdviceDatabase.getDatabase(application, scope).adviceDao()
        repository = AdviceRepository(adviceDao)
        bigList = repository.allAdvices
    }

    fun insert(advice: Advice) = scope.launch(Dispatchers.IO) {
        repository.insert(advice)
    }

    // Debugging function, DO NOT USE
    fun clearDB() = scope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun filterAdvice(category: String) = bigList.value?.filter { it.category == category }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
