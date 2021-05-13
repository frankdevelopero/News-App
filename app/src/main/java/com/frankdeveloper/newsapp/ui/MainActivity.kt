package com.frankdeveloper.newsapp.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.frankdeveloper.newsapp.R
import com.frankdeveloper.newsapp.adapters.NewsAdapter
import com.frankdeveloper.newsapp.db.ArticleDatabase
import com.frankdeveloper.newsapp.repository.NewsRepository
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.frankdeveloper.newsapp.models.Post
import com.frankdeveloper.newsapp.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private val TAG = "MainActivity"
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private var listNews: ArrayList<Post> = ArrayList()
    lateinit var newsRepository: NewsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)

        setupRecyclerView()
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        newsAdapter.setOnItemClickListener {
            val intent = Intent(this, PostViewActivity::class.java)
            intent.putExtra("post_url", it.url)
            startActivity(intent)
        }

        getData()

    }

    private fun getData(){

        lifecycleScope.launch {
            try {
                if(hasInternetConnection()) {
                    val response = newsRepository.getBreakingNews()
                    if (response.isSuccessful) {
                        for (post in response.body()!!.posts) {

                            if (post.title != null && post.author != null && post.createdAt != null && post.url != null && post.storyId != null) {
                                listNews.add(post)
                                insertPost(post)
                            }
                        }
                        newsAdapter.notifyDataSetChanged()
                    }

                }else{
                    getSavedData()
                }
            } catch(t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
            newsAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getSavedData(){

        viewModel.getSavedNews().observe(this, Observer { articles ->
            if(articles != null){
                listNews.addAll(articles)
            }
            newsAdapter.notifyDataSetChanged()

        })

    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = this.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(listNews)
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
            val swipeHandler = object : SwipeToDeleteCallback(context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = rvBreakingNews!!.adapter as NewsAdapter
                    updatePost(listNews[viewHolder.adapterPosition])
                    adapter.removeAt(viewHolder.adapterPosition)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rvBreakingNews)
        }

        swipeRefreshLayout.apply {
            setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                getDataRefresh()
            })
        }
    }

    private fun insertPost(post: Post){
        lifecycleScope.launch{
            newsRepository.insert(post)
        }
    }

    private fun updatePost(post: Post){
        post.status = false
        lifecycleScope.launch{
            newsRepository.insert(post)
        }
    }

    private fun getDataRefresh() {
        getData()

    }
}