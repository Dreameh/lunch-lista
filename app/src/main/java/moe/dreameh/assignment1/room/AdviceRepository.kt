package moe.dreameh.assignment1.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import moe.dreameh.assignment1.Advice

class AdviceRepository(private val adviceDao: AdviceDao) {

    val allAdvices: MutableLiveData<MutableList<Advice>> = adviceDao.getAll()

    @WorkerThread
    suspend fun insert(advice: Advice) {
        adviceDao.insert(advice)
    }

    @WorkerThread
    suspend fun delete(advice: Advice) {
        adviceDao.delete(advice)
    }
}