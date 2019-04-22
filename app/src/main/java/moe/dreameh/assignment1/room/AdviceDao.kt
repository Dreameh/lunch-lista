package moe.dreameh.assignment1.room

import androidx.lifecycle.MutableLiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import moe.dreameh.assignment1.Advice

interface AdviceDao {

    @Query("SELECT * FROM advices")
    fun getAll(): MutableLiveData<MutableList<Advice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(advice: Advice)

    @Delete
    fun delete(advice: Advice)

    @Query("DELETE FROM advices")
    fun deleteAll()
}