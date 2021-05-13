package com.frankdeveloper.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frankdeveloper.newsapp.R
import com.frankdeveloper.newsapp.models.Post
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter(private val items: MutableList<Post>) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    private var onItemClickListener: ((Post) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = items[position]
        holder.itemView.apply {
            tvSource.text = article.author
            tvTitle.text = article.title
            tvPublishedAt.text = article.createdAt

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Post) -> Unit) {
        onItemClickListener = listener
    }
}













