package moe.dreameh.assignment1.room

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import moe.dreameh.assignment1.worker.RetrofitFactory
import retrofit2.*
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
        fetchAll()
        populateCategoriesIfNeeded(application)
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun specialInsert(advice: Advice) {
        adviceDao.insert(advice)

    }

    /**
     * Running the retrofit call from here, so that it will be easy to add it to the repository.
     * The cache and database.
     */
    private fun fetchAll() {
        val t = Thread {
            adviceDao.deleteAll()
        }
        t.start()
        try {
            t.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val service = RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val request = service.loadAdvices()
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        val output: MutableList<Advice>? = response.body()
                        val job = GlobalScope.launch {
                            output!!.forEach {
                                adviceDao.insert(it)
                            }
                        }
                        job.join()
                    }
                } catch (e: HttpException) {
                    e.printStackTrace()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }


    /**
     * Add (for now) hardcoded categories to the database
     * Also create a cache to avoid having to repeatedly lookup category names in a new thread
     */
    private fun populateCategoriesIfNeeded(application: Application) {
        categoryList = ArrayList()

        val killWithNoMercyThread = Thread {
            categoryDao.killItWithFire()
        }
        killWithNoMercyThread.start()
        try {
            killWithNoMercyThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }


        val service = RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val request = service.loadCategories()
            withContext(Dispatchers.Main) {
                val job2 = GlobalScope.launch {
                    val categoriesInDb = categoryDao.getAllCategories()
                    when {
                        categoriesInDb.isEmpty() -> {
                            try {
                                val response = request.await()
                                if (response.isSuccessful) {
                                    val output: List<Category>? = response.body()
                                    val job = GlobalScope.launch {
                                        output?.forEach {
                                            categoryList.add(Category(it.id, it.name))
                                            categoryDao.insert(Category(it.id, it.name))
                                        }
                                    }
                                    job.join()
                                }
                            } catch (e: HttpException) {
                                e.printStackTrace()
                            } catch (e: Throwable) {
                                e.printStackTrace()
                            }
                        }
                        else -> for (category in categoriesInDb) categoryList.add(Category(category.id, category.name))
                    }
                }
                job2.join()
            }
        }
    }

    private fun getNameList() {
        categoryNames = categoryDao.getAllNames()
    }
}