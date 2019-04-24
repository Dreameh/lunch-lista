package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import androidx.room.*
import moe.dreameh.assignment1.Advice

@Dao
interface AdviceDao {

    @Query("SELECT * FROM advices")
    fun getAll(): LiveData<MutableList<Advice>>
}