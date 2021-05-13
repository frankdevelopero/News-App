package com.frankdeveloper.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.frankdeveloper.newsapp.models.Post

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Post) : Long
    @Update
    suspend fun update(article: Post)

    @Query("SELECT * FROM post_table")
    fun getAllArticles(): LiveData<List<Post>>

    @Delete
    suspend fun deleteArticle(article: Post)
}