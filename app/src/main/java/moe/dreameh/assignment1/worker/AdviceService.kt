package moe.dreameh.assignment1.worker

import moe.dreameh.assignment1.room.Advice
import retrofit2.Call
import retrofit2.http.GET

interface AdviceService {

    @GET("getadvice")
    fun loadAdvices(): Call<List<Advice>>
}