package com.app.article.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.article.R
import com.app.article.model.Article
import com.app.article.ui.ArticlesActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

class CategoryAdapter(private val context: Context, private var articleList: MutableMap<String, ArrayList<Article>>, private val listener : CategoryListener?) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var originalArticles = articleList
    var categoryCountEmpty = 0

    interface CategoryListener {
        fun onCategoryRefresh()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.row_category, viewGroup, false)
        return CategoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        if (position < articleList.size) {
            holder.containerItem.visibility = View.VISIBLE
            holder.ivRefresh.visibility = View.GONE

            var articles : ArrayList<Article> = arrayListOf()
            articleList.onEachIndexed { index, entry ->
                run {
                    if (index == position) {
                        articles = entry.value
                    }
                }
            }

            if (articles.isNotEmpty()) {
                val dateParse : Date = SimpleDateFormat("yyyy-MM-dd").parse(articles?.get(0)?.publishedAt)
                val date : String = SimpleDateFormat("yyyy-MM-dd").format(dateParse)
                holder.tvDate.text = date

                holder.categoryList.layoutManager = LinearLayoutManager(context)
                holder.categoryList.adapter = ItemCategoryAdapter(context, articles)
            } else {
                categoryCountEmpty++;
            }
        } else {
            if (categoryCountEmpty != articleList.size) {
                holder.containerItem.visibility = View.GONE
                holder.ivRefresh.visibility = View.VISIBLE

                holder.ivRefresh.setOnClickListener {
                    listener?.onCategoryRefresh()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (categoryCountEmpty == articleList.size) {
            (context as ArticlesActivity).showEmptyLabel(View.VISIBLE)
            return 0
        } else {
            (context as ArticlesActivity).showEmptyLabel(View.GONE)
            return articleList.size + 1
        }
    }

    inner class CategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var containerItem : LinearLayout = itemView.findViewById(R.id.containerItem)
        var tvDate : TextView = itemView.findViewById(R.id.tvDate)
        var categoryList : RecyclerView = itemView.findViewById(R.id.categoryList)
        var ivRefresh : ImageView = itemView.findViewById(R.id.ivRefresh)
    }

    fun filter(text : String) {
        val filterArticleList = originalArticles
        articleList = mutableMapOf()

        filterArticleList.onEachIndexed { index, entry ->
            run {
                val articles : ArrayList<Article> = arrayListOf()

                for (article in entry.value) {
                    if (article.title!!.lowercase().contains(text.toString().lowercase())) {
                        articles.add(article)
                    } else if (article.description!!.lowercase().contains(text.toString().lowercase())) {
                        articles.add(article)
                    }
                }

                if (articles.size > 0) {
                    articleList[entry.key] = articles
                }
            }
        }

        notifyDataSetChanged()
    }
}