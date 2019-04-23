package moe.dreameh.assignment1.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.Category

class CategoryRepository(private val categoryDao: CategoryDao) {


    val allNames: LiveData<MutableList<String>> = categoryDao.getAllNames()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(category: Category): Unit = categoryDao.insert(category)

}