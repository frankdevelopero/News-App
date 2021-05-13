package com.frankdeveloper.newsapp.repository

import com.frankdeveloper.newsapp.api.RetrofitInstance
import com.frankdeveloper.newsapp.db.ArticleDatabase
import com.frankdeveloper.newsapp.models.Post

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getBreakingNews() = RetrofitInstance.api.getBreakingNews()

    suspend fun insert(article: Post) = db.getArticleDao().insert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

}