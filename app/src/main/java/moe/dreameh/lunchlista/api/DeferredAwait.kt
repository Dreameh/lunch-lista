package moe.dreameh.lunchlista.api

import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume

suspend fun <T : Any> Deferred<Response<T>>.awaitResult(): Result<T> {
    return suspendCancellableCoroutine { continuation ->

        GlobalScope.launch {
            try {
                val response = await()
                continuation.resume(
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            Result.Ok(it, response.raw())
                        } ?: "error".let {
                            if (response.code() ==200){
                                Result.Exception(Exception("body is empty"))
                            }
                            else{
                                Result.Exception(NullPointerException("Response body is null"))
                            }

                        }

                    } else {
                        Result.Error(HttpException(response), response.raw())
                    }
                )
            }
            catch (e:Throwable){
                //  Log.e("DeferredAwait",e.message)
                continuation.resume(Result.Exception(e))
            }

        }

        registerOnCompletion(continuation)
    }
}
private fun Deferred<Response<*>>.registerOnCompletion(continuation: CancellableContinuation<*>) {
    continuation.invokeOnCancellation {
        if (continuation.isCancelled)
            try {
                cancel()
            } catch (ex: Throwable) {
                //Ignore cancel exception
                ex.printStackTrace()
            }
    }
}
