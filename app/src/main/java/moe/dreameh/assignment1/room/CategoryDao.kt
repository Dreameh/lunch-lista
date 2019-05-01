package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao : BaseDao<Category> {

    @Query("SELECT name FROM Category")
    fun getAllNames(): LiveData<MutableList<String>>

    @Query("SELECT * FROM Category")
    fun getAllCategories(): List<Category>
}