package moe.dreameh.assignment1.worker

import moe.dreameh.assignment1.room.Category
import retrofit2.Call
import retrofit2.http.GET

interface CategoryService {

    @GET("getcategories")
    fun loadCategories(): Call<List<Category>>
}