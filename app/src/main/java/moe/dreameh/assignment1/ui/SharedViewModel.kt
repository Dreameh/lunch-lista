package moe.dreameh.assignment1.ui

import android.app.Application
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
    private val catRepository: CategoryRepository
    val bigList: LiveData<MutableList<Advice>>
    private val categories: LiveData<List<Category>>
    var categoryList: List<Category> = ArrayList()

    init {
        val adviceDao = AdviceDatabase.getDatabase(application).adviceDao()
        val categoryDao = AdviceDatabase.getDatabase(application).categoryDao()

        catRepository = CategoryRepository(categoryDao)
        repository = AdviceRepository(adviceDao)
        categories = catRepository.allCategories

        bigList = repository.allAdvices
        populateCategories()
        if(categories.value.isNullOrEmpty()) {
            populateCategories()
        } else {
            categoryList = categories.value!!
        }
    }

    fun insert(advice: Advice) = scope.launch(Dispatchers.IO) {
        repository.insert(advice)
    }

    // Debugging function, DO NOT USE
    private fun populateDB() = scope.launch(Dispatchers.IO) {
        repository.insert(Advice(
                "Han Kolo",
                "Lifestyle",
                "I still get a funny feeling about that old man and the kid. I'm not sure what it is about them, but they're trouble"))

        repository.insert( Advice(
                "Obi-wan Henobi",
                "Technology",
                "These aren't the droids you're looking for."
        ))

        repository.insert(Advice(
                "Darth Vaber",
                "Miscellaneous",
                "Impressive. Most impressive. Obi-Wan has taught you well. You have controlled your fear. Now, release your anger. Only your hatred can destroy me."
        ))
    }

    // Debugging function, DO NOT USE
    fun clearDB() = scope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun filterAdvice(category: String) = bigList.value?.filter { it.category == category }

    fun populateCategories() = scope.launch(Dispatchers.IO) {
        catRepository.insert(Category(1, "Lifestyle"))
        catRepository.insert(Category(2, "Technology"))
        catRepository.insert(Category(3, "Miscellaneous"))
    }

    fun fetchAllCategories(): List<String> {
        var fetchedList: MutableList<String> = ArrayList()
        for (item: Category in categoryList) {
            item.let { fetchedList.add(it.name!!) }
        }
        return fetchedList
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
