package com.app.article.model

import com.google.gson.annotations.SerializedName

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

data class ArticleResponse(
    @SerializedName("status")
    val status : String,

    @SerializedName("totalResults")
    val totalResults : Int,

    @SerializedName("articles")
    val articles : ArrayList<Article>
)