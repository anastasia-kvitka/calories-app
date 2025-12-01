package com.example.caloriesapp.ai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.File
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class ChatGPTService(private val apiKey: String) {

    private val api: ChatGPTApi by lazy {
        val client = OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $apiKey")
                        .build()
                )
            }
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatGPTApi::class.java)
    }

    suspend fun analyzeFoodImage(file: File): String {
        val base64 = encode(file)

        val request = ChatVisionRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                VisionMessage(
                    role = "user",
                    content = listOf(
                        VisionContent.Text(
                            text = """
                                Analyze the food in this image.
                                Return:
                                - List of foods on the plate
                                - Calories, protein, carbs, fat
                                - Portion size
                                - Diabetes warnings
                                - Healthy alternatives
                                - Short summary
                            """.trimIndent()
                        ),
                        VisionContent.ImageUrl(
                            image_url = VisionImageURL("data:image/jpeg;base64,$base64")
                        )
                    )
                )
            )
        )

        repeat(3) { attempt ->
            try {
                val response = api.chatCompletion(request)
                return response.choices.first().message.content
            } catch (e: Exception) {
                if (attempt == 2) return "Error: ${e.localizedMessage}"
                delay(1500)
            }
        }

        return "Unknown error"
    }

    fun compressImage(file: File): ByteArray {
        val bmp = BitmapFactory.decodeFile(file.absolutePath)
        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, out)
        return out.toByteArray()
    }
    private fun encode(file: File): String =
        Base64.encodeToString(compressImage(file), Base64.NO_WRAP)

    suspend fun recommendFoods(diseases: Set<String>): String {
        val prompt =
            if (diseases.isEmpty()) {
                """
            Give a short general nutrition recommendation for someone with
            no reported health conditions. Keep it under 6 sentences.
            """.trimIndent()
            } else {
                """
            The user has these health conditions: ${diseases.joinToString(", ")}.
            Provide a short, simple, friendly food recommendation.
            - Avoid medical advice
            - Max 6 sentences
            - Provide safe general suggestions
            - No clinical or diagnostic statements
            """.trimIndent()
            }

        val request = ChatVisionRequest(
            model = "gpt-4o-mini",
            messages = listOf(
                VisionMessage(
                    role = "user",
                    content = listOf(
                        VisionContent.Text(text = prompt)
                    )
                )
            )
        )

        return try {
            val response = api.chatCompletion(request)
            response.choices.first().message.content
        } catch (e: Exception) {
            "Error: ${e.localizedMessage ?: "Unknown error"}"
        }
    }


}
