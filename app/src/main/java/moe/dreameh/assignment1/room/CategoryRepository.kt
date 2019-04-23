package moe.dreameh.assignment1.room

import androidx.annotation.WorkerThread
import moe.dreameh.assignment1.Category

class CategoryRepository(private val categoryDao: CategoryDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAll() = categoryDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(category: Category) = categoryDao.insert(category)

}