package moe.dreameh.assignment1.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import moe.dreameh.assignment1.Category
import androidx.room.*

@Dao
interface CategoryDao : BaseDao<Category> {

    @Query("SELECT name FROM Category")
    fun getAllNames(): List<String>
}