package moe.dreameh.assignment1.room

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.HashMap
import kotlin.coroutines.CoroutineContext

class AdviceRepository(application: Application) {

    // Coroutine tasker?
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    // Dao and cache related objects
    private var adviceDao: AdviceDao
    private var categoryDao: CategoryDao
    private lateinit var categoryList: ArrayList<Category>
    lateinit var categoryNames: LiveData<MutableList<String>>

    init {
        adviceDao = AdviceDatabase.getDatabase(application).adviceDao()
        categoryDao = AdviceDatabase.getDatabase(application).categoryDao()
        populateCategoriesIfNeeded()
        getNameList()
    }

    val allAdvices: LiveData<MutableList<Advice>> = adviceDao.getAll()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(advice: Advice): Unit = adviceDao.insert(advice)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(): Unit = adviceDao.deleteAll()

    fun getCategoryName(id: Int): String? {
        return categoryList[id].name
    }

    /**
     * Add (for now) hardcoded categories to the database
     * Also create a cache to avoid having to repeatedly lookup category names in a new thread
     */
    private fun populateCategoriesIfNeeded() {
        categoryList = ArrayList()

        val t = Thread {
            val categoriesInDb = categoryDao.getAllCategories()

            when {
                categoriesInDb.isEmpty() -> {
                    val categories = arrayOf("Lifestyle", "Technology", "Miscellaneous")

                    categories.withIndex().forEach { (index, value) ->
                        categoryList.add(Category(index, value))
                        categoryDao.insert(Category(index, value))
                    }
                }
                else -> for (category in categoriesInDb) categoryList.add(Category(category.id, category.name))
            }
        }

        t.start()
        // Waiting for the thread might cause a short UI lockup during startup  but we'll live with that.
        // For longer-running initialization tasks some kind of progress dialog might be motivated.
        try {
            t.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun getNameList() {
        categoryNames = categoryDao.getAllNames()
    }
}