package moe.dreameh.assignment1.api

import kotlinx.coroutines.Deferred
import moe.dreameh.assignment1.room.Advice
import moe.dreameh.assignment1.room.Category
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AdviceService {

    @GET("getadvice")
    fun loadAdvices(): Deferred<Response<MutableList<Advice>>>

    @FormUrlEncoded
    @POST("addadvice")
    fun addAdvice(@Field("author") author: String?,
                  @Field("advice") advice: String?,
                  @Field("category") category: String?): Deferred<Response<Advice>>

    @GET("getcategories")
    fun loadCategories(): Deferred<Response<List<Category>>>
}