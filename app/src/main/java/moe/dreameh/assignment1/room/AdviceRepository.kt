package moe.dreameh.assignment1.room

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import moe.dreameh.assignment1.api.RetrofitFactory
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

    fun specialInsert(advice: Advice) = scope.launch(IO) {
        val service = RetrofitFactory.makeRetrofitService()
        val addAdvice = service.addAdvice(
                advice.author, advice.content, advice.category)
        try {
            val response = addAdvice.await()
            Log.d("addAdvice status: ", response.message())
            if(response.isSuccessful) {
                insert(advice)
            }
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * Running the retrofit call from here, so that it will be easy to add it to the repository.
     * The cache and database.
     *
     * Note: Using coroutines so that it can be running asynchronisly, so that the database don't complain.
     */
    fun fetchAll() = scope.launch(IO) {
        val service = RetrofitFactory.makeRetrofitService()
        val request = service.loadAdvices()
        try {
            val response = request.await()
            Log.d("fetchAll status: ", response.message())
            if (response.isSuccessful) {
                val output: MutableList<Advice>? = response.body()
                if(allAdvices.value!!.size < output!!.size) {
                    // Making sure that only new advices gets loaded, when successful.
                    // Otherwise no need for killing the advices, and instead loading it
                    // From the database/cache.
                    doKillAdvices()
                    Log.d("Output size", output.size.toString())
                    output.forEach { advice ->
                        adviceDao.insert(advice)
                    }
                }
            }
        } catch (e: HttpException) {
            e.printStackTrace()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    private suspend fun doKillAdvices() {
        deleteAll()
    }

    /**
     * Add (for now) hardcoded categories to the database
     * Also create a cache to avoid having to repeatedly lookup category names in a new thread
     *
     * Note: Using coroutines so that it can be running asynchronisly, so that the database don't complain.
     */

    private fun populateCategoriesIfNeeded() = scope.launch(IO) {
        categoryList = ArrayList()
        val service = RetrofitFactory.makeRetrofitService()
        val request = service.loadCategories()
        val categoriesInDb = categoryDao.getAllCategories()

        when {
            categoriesInDb.isEmpty() -> {
                try {
                    val response = request.await()
                    Log.d("categoryFetch status: ", response.message())
                    if (response.isSuccessful) {
                        val output: List<Category>? = response.body()
                        if (output!!.size > categoriesInDb.size) {
                            Log.d("Output size", output.size.toString())
                            output.forEach {
                                categoryList.add(Category(it.id, it.name))
                                categoryDao.insert(Category(it.id, it.name))
                            }
                        }
                    } else { }
                } catch (e: HttpException) {
                    e.printStackTrace()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            else -> for (category in categoriesInDb) categoryList.add(Category(category.id, category.name))
        }
    }

    private fun getNameList() {
        categoryNames = categoryDao.getAllNames()
    }

    /**
     * Setting up a periodic sync between the server and the application
     * TODO: If the application database/cache has less entries than the received output, only then
     *       shall the application do a kill with fire of the database and the cache.
     * TODO: Should check for internet connection before the sync, or it would block the entire stack.
     */


}