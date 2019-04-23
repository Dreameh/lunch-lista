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
import moe.dreameh.assignment1.room.CategoryDao
import moe.dreameh.assignment1.room.CategoryRepository
import kotlin.coroutines.CoroutineContext

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val catRepository: CategoryRepository
    private var nameList: MutableList<String> = ArrayList()

    init {
        val categoryDao = AdviceDatabase.getDatabase(application, scope).categoryDao()
        catRepository = CategoryRepository(categoryDao)
        setAllCategoryNames()

        Log.v("CategoryDao", categoryDao.toString())
    }

    private fun setAllCategoryNames() = scope.launch(Dispatchers.IO) {
        nameList.addAll(catRepository.getAllNames())
    }

    fun fetchAllNames(): MutableList<String> = nameList


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}