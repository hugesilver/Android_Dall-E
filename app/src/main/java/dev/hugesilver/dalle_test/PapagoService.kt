import dev.hugesilver.dalle_test.BuildConfig
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface PapagoService {
    @FormUrlEncoded
    @Headers(
        "Content-Type: application/x-www-form-urlencoded; charset=UTF-8",
        "X-Naver-Client-Id: ${BuildConfig.PAPAGO_CLIENT_ID}",
        "X-Naver-Client-Secret: ${BuildConfig.PAPAGO_CLIENT_SECRET}"
    )
    @POST("/v1/papago/n2mt")
    fun postPapago(
        @Field("source") source: String,
        @Field("target") target: String,
        @Field("text") text: String
    ): Call<ResultTransferPapago>
}

data class ResultTransferPapago (
    var message: Message
)

data class Message(
    var result: Result
)

data class Result (
    var srcLangType: String = "",
    var tarLangType: String = "",
    var translatedText: String = ""
)