package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import moe.dreameh.assignment1.Category
import androidx.room.*

@Dao
interface CategoryDao : BaseDao<Category> {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.


    @Query("DELETE FROM categories")
    fun deleteAll()

    @Query("SELECT name FROM categories")
    fun getAllNames(): LiveData<MutableList<String>>
}