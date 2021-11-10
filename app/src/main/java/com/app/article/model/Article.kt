package com.app.article.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Entity(tableName = "article")
data class Article (
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "author")
    @SerializedName("author")
    val author : String?,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title : String?,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description : String?,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    val url : String?,

    @ColumnInfo(name = "urlToImage")
    @SerializedName("urlToImage")
    val urlToImage : String?,

    @ColumnInfo(name = "publishedAt")
    @SerializedName("publishedAt")
    val publishedAt : String?,

    @ColumnInfo(name = "content")
    @SerializedName("content")
    val content : String?
)