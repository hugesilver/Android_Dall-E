package dev.hugesilver.dalle_test

import PapagoService
import ResultTransferPapago
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import dev.hugesilver.dalle_test.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    fun postDall_E(translatedText: String) {
        var intent = Intent(this, ReturnActivity::class.java)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DallEService::class.java)

        service.postDallE(DallERequest(translatedText, 8, "256x256"))
            .enqueue(object : Callback<ResultDallEImages> {
                override fun onResponse(
                    call: Call<ResultDallEImages>,
                    response: Response<ResultDallEImages>
                ) {
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        Log.d("Result", "Dall-E onResponse 성공: " + response.raw().toString())
                        Log.d("Result", response.body()!!.data.toString())

                        try {
                            val resultArray = JSONArray(response.body()!!.data)
                            val urlArray: ArrayList<String> = ArrayList<String>()

                            for (i in 0 until resultArray.length()) {
                                urlArray.add(resultArray.getJSONObject(i).getString("url"))
                            }
                            intent.putStringArrayListExtra("images", urlArray)
                            startActivity(intent)
                            binding.loading.visibility = View.GONE
                        } catch (e: JSONException) {
                            Log.e("Error", e.toString())
                            Toast.makeText(
                                this@MainActivity,
                                "이미지 출력 중 실패: $e",
                                Toast.LENGTH_LONG
                            )
                            binding.loading.visibility = View.GONE
                        }
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        Log.d("Result", "Dall-E onResponse 실패: " + response.code())
                        Toast.makeText(
                            this@MainActivity,
                            "이미지 생성 중 실패하였습니다: ${response.code()}",
                            Toast.LENGTH_LONG
                        )
                        binding.loading.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<ResultDallEImages>, t: Throwable) {
                    Log.d("Result", "Dall-E onFailure: " + t.toString());
                    Toast.makeText(this@MainActivity, "이미지 생성 중 실패하였습니다: ${t}", Toast.LENGTH_LONG)
                    binding.loading.visibility = View.GONE
                }
            })
    }

    fun postPapago(text: String) {
        binding.editText.text.clear()
        binding.loading.visibility = View.VISIBLE
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PapagoService::class.java)

        service.postPapago("ko", "en", text)
            .enqueue(object : Callback<ResultTransferPapago> {
                override fun onResponse(
                    call: Call<ResultTransferPapago>,
                    response: Response<ResultTransferPapago>
                ) {
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        Log.d("Result", "Papago onResponse 성공: " + response.raw().toString());
                        postDall_E(response.body()!!.message.result.translatedText)
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        Log.d("Result", "Papago onResponse 실패: " + response.code())
                        Toast.makeText(
                            this@MainActivity,
                            "번역 중 실패하였습니다: ${response.code()}",
                            Toast.LENGTH_LONG
                        )
                        binding.loading.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<ResultTransferPapago>, t: Throwable) {
                    Log.d("Result", "Papago onFailure: " + t.toString())
                    Toast.makeText(this@MainActivity, "번역 중 실패하였습니다: ${t}", Toast.LENGTH_LONG)
                    binding.loading.visibility = View.GONE
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.editText.setOnKeyListener { _, keyCode, event ->
            // Enter key Action
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                postPapago(binding.editText.text.toString())
                true
            } else {
                false
            }
        }
    }
}