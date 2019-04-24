package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import androidx.room.*

interface BaseDao <T> {


    @Query("SELECT * FROM #{T}")
    fun getAll(): LiveData<MutableList<T>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)


    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)

    @Query("DELETE FROM #{T}")
    fun deleteAll()

}