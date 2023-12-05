package dev.hugesilver.dalle_test

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DallEService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.OPENAI_API_KEY}"
    )
    @POST("/v1/images/generations")
    fun postDallE(
        @Body request: DallERequest
    ): Call<ResultDallEImages>
}

data class ResultDallEImages (
    var data: ArrayList<Any>
)

data class DallERequest(
    val model: String,
    val prompt: String,
    val n: Int,
    val size: String
)