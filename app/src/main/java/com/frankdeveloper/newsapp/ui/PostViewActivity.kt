package com.frankdeveloper.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.frankdeveloper.newsapp.R
import kotlinx.android.synthetic.main.activity_post_view.*

class PostViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_view)

        val postUrl = intent.getStringExtra("post_url")

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(postUrl)
        }

    }
}