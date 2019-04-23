package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import moe.dreameh.assignment1.Category
import androidx.room.*

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAll(): MutableList<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category)

    @Delete
    fun delete(category: Category)

    @Query("DELETE FROM categories")
    fun deleteAll()

}