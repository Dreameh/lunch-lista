package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import moe.dreameh.assignment1.Category
import androidx.room.*

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category)

    @Delete
    fun delete(category: Category)

    @Query("SELECT name FROM categories WHERE id = :id")
    fun get(id: Int): String

    @Query("DELETE FROM categories")
    fun deleteAll()
}