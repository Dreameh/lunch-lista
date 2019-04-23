package moe.dreameh.assignment1.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import moe.dreameh.assignment1.Category

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: LiveData<List<Category>> = categoryDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(category: Category) = categoryDao.insert(category)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAll() = categoryDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun get(id: Int) = categoryDao.get(id)

}