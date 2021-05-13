package com.frankdeveloper.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true) var id: Long ? = null,
    @SerializedName("story_id") var storyId: Long ? =null,
    @SerializedName("story_title") var title: String ? =null,
    @SerializedName("created_at") var createdAt: String ? =null,
    @SerializedName("author") var author: String ? =null,
    @SerializedName("story_url") var url: String ? =null,
    var status: Boolean = true
    )