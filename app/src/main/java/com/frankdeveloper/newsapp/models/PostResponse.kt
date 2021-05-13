package com.frankdeveloper.newsapp.models

import com.google.gson.annotations.SerializedName

class PostResponse(
    @SerializedName("hits") var posts: ArrayList<Post>
)