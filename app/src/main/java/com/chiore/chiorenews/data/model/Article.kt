package com.chiore.chiorenews.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "news"
)
@Parcelize
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    @Ignore
    val source: Source?,
    val title: String?,
    @PrimaryKey
    val url: String,
    val urlToImage: String?,
) : Parcelable {

    constructor(
        author: String?,
        content: String?,
        description: String?,
        publishedAt: String?,
        title: String?,
        url: String?,
        urlToImage: String?,
    ) : this(
        author,
        content,
        description,
        publishedAt,
        Source("", ""),
        title,
        "",
        urlToImage
    )

}