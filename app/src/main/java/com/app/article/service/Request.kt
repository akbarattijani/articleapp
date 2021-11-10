package com.app.article.service

import com.app.article.model.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

interface Request {

    @GET("everything?q=tesla&from=2021-10-10&sortBy=publishedAt&apiKey=aafb04bc91964bdcb32de7dd80e1a5f5")
    fun news() : Call<ArticleResponse>
}