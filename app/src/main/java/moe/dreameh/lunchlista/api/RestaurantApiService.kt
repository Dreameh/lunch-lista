package moe.dreameh.lunchlista.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import moe.dreameh.lunchlista.persistence.Results
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface RestaurantApiService {


    @GET("lunch-meny?plain=true")
    fun getCache(): Deferred<Response<Results>>

    companion object Factory {
        fun create(): RestaurantApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://mauritz.cloud/")
                .build()


            return retrofit.create(RestaurantApiService::class.java)
        }
    }
}