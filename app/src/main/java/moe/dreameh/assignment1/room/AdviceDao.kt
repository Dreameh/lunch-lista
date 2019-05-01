package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AdviceDao : BaseDao<Advice> {

    @Query("SELECT * FROM Advice")
    fun getAll(): LiveData<MutableList<Advice>>

    @Query("DELETE FROM Advice")
    fun deleteAll()
}