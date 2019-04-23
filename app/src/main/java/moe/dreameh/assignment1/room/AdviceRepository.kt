package moe.dreameh.assignment1.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import moe.dreameh.assignment1.Advice

class AdviceRepository(private val adviceDao: AdviceDao) {

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