package moe.dreameh.assignment1.room

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import moe.dreameh.assignment1.Advice
import kotlin.coroutines.CoroutineContext

class AdviceRepository(private var adviceDao: AdviceDao, private var categoryDao: CategoryDao,
                       application: Application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    init {
         adviceDao = AdviceDatabase.getDatabase(application, scope).adviceDao()
         categoryDao = AdviceDatabase.getDatabase(application, scope).categoryDao()
    }

    val allAdvices: LiveData<MutableList<Advice>> = adviceDao.getAll()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(advice: Advice): Unit = adviceDao.insert(advice)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(advice: Advice): Unit = adviceDao.delete(advice)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(): Unit = adviceDao.deleteAll()
}