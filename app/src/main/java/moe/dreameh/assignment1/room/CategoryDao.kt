package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import moe.dreameh.assignment1.Category
import androidx.room.*

@Dao
interface CategoryDao {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAll(): LiveData<MutableList<Category>>

    @Insert
    fun insert(vararg category: Category)

    @Delete
    fun delete(vararg category: Category)

    @Query("DELETE FROM categories")
    fun deleteAll()

}