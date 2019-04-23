package moe.dreameh.assignment1.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getAllNames(): List<String> = categoryDao.getAllNames()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(category: Category): Unit = categoryDao.insert(category)

}