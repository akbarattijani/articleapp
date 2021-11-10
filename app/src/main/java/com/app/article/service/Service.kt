package com.app.article.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

object Service {
    private const val BASE_URL = "https://newsapi.org/v2/"

    val get : Request by lazy {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Request::class.java)
    }
}