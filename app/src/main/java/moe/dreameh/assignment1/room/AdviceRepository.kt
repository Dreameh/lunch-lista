package moe.dreameh.assignment1.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import moe.dreameh.assignment1.Advice

class AdviceRepository(private val adviceDao: AdviceDao) {

    val allAdvices: LiveData<MutableList<Advice>> = adviceDao.getAll()

    @WorkerThread
    suspend fun insert(advice: Advice) {
        adviceDao.insert(advice)
    }

    @WorkerThread
    suspend fun delete(advice: Advice) {
        adviceDao.delete(advice)
    }

    @WorkerThread
    suspend fun deleteAll() {
        adviceDao.deleteAll()
    }

}