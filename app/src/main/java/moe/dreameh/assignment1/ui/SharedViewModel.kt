package moe.dreameh.assignment1.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.dreameh.assignment1.room.Advice
import moe.dreameh.assignment1.room.AdviceRepository
import kotlin.coroutines.CoroutineContext

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    val bigList: LiveData<MutableList<Advice>>

    private val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
    private val repository: AdviceRepository = AdviceRepository(application)
    val categories: LiveData<MutableList<String>>

    init {
        bigList = repository.allAdvices
        categories = repository.categoryNames
    }

    fun insert(advice: Advice) = scope.launch(Dispatchers.IO) {
        repository.specialInsert(advice)
    }

    fun filterAdvice(category: String) = bigList.value?.filter { it.category == category }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getAuthor(): String? {
        return pref.getString("author", "Anonymous")
    }

    fun getDefaultCategory(): String? {
        return repository.getCategoryName(getDefaultCategoryFor())
    }

    fun getDefaultCategoryFor(): Int {
        return repository.categoryNames.value!!.indexOf(pref.getString("list", "0"))
    }

}
