package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var adviceTextView: TextView
    lateinit var getAdviceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adviceTextView = findViewById(R.id.tv_advice)
        getAdviceButton = findViewById(R.id.btn_getAdvice)

        getAdviceButton.setOnClickListener {
            getAdviceAPI()
        }

    }

    private fun getAdviceAPI() {
        CoroutineScope(IO).launch {
            try {
                val okHttpClient = OkHttpClient()
                val request = Request.Builder().url("https://api.adviceslip.com/advice").build()
                val response = okHttpClient.newCall(request).execute()
                if(response!=null){
                    val jsonObject = JSONObject(response.body!!.string())
                    val advice = jsonObject.getJSONObject("slip").getString("advice")
                    withContext(Main){
                        adviceTextView.text = advice
                    }
                    Log.d("MainActivity",response.message)
                }
            }catch(e: Exception){
                Log.d("MainActivity",e.toString())

            }
        }
    }
}