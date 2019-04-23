package moe.dreameh.assignment1.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.dreameh.assignment1.Category
import moe.dreameh.assignment1.room.AdviceDatabase
import moe.dreameh.assignment1.room.CategoryRepository
import kotlin.coroutines.CoroutineContext

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val catRepository: CategoryRepository
    private var categories: MutableList<Category> = ArrayList()
    private var categoryList: List<Category> = ArrayList()
    private var number: Int = 0

    init {
        val categoryDao = AdviceDatabase.getDatabase(application).categoryDao()
        catRepository = CategoryRepository(categoryDao)
        populateCategories()
    }

    private fun loadCategories() = scope.launch(Dispatchers.IO) {
        categories = catRepository.getAll()
    }

    private fun populateCategories() = scope.launch(Dispatchers.IO) {
        catRepository.insert(Category(1, "Lifestyle"))
        catRepository.insert(Category(2, "Technology"))
        catRepository.insert(Category(3, "Miscellaneous"))
    }

    fun fetchAllCategories(): MutableList<String> {
        val fetchedList: MutableList<String> = ArrayList()
        for (item: Category in categoryList) {
            item.let { fetchedList.add(it.name!!) }
        }
        return fetchedList
    }

    fun size() = categories.size
}