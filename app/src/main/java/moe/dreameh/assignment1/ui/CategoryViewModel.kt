package moe.dreameh.assignment1.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.dreameh.assignment1.Category
import moe.dreameh.assignment1.room.AdviceDatabase
import moe.dreameh.assignment1.room.AdviceDatabase.Companion.populateCategories
import moe.dreameh.assignment1.room.CategoryRepository
import kotlin.coroutines.CoroutineContext

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val catRepository: CategoryRepository
    private var categories: LiveData<MutableList<Category>>
    private var categoryList: List<Category> = ArrayList()

    init {
        val categoryDao = AdviceDatabase.getDatabase(application, scope).categoryDao()
        catRepository = CategoryRepository(categoryDao)
        categories = catRepository.allCategories

        Log.v("CategoryDao", categoryDao.getAll().value.toString())
        Log.v("Helvete: ", categories.value.toString())
    }

    fun fetchAllCategories(): MutableList<String> {
        val fetchedList: MutableList<String> = ArrayList()
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