package com.app.article.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.article.R
import com.app.article.model.Article
import com.app.article.ui.ArticleActivity
import com.bumptech.glide.Glide
import java.util.ArrayList

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

class ItemCategoryAdapter(private val context: Context, private var articleList : List<Article>?) : RecyclerView.Adapter<ItemCategoryAdapter.ItemCategoryViewHolder>(), Filterable {

    var originalList : List<Article>? = articleList

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) : ItemCategoryViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.row_category_item, viewGroup, false)
        return ItemCategoryViewHolder(v)
    }

    override fun onBindViewHolder(itemCategoryViewHolder: ItemCategoryViewHolder, i: Int) {
        itemCategoryViewHolder.tvTitle.text = articleList?.get(i)?.title
        itemCategoryViewHolder.tvDesc.text = articleList?.get(i)?.description

        Glide.with(context)
            .load(articleList?.get(i)?.urlToImage)
            .into(itemCategoryViewHolder.articleImage);

        itemCategoryViewHolder.container.setOnClickListener {
            val intent = Intent(context, ArticleActivity::class.java)
            intent.putExtra("url", articleList?.get(i)?.url)
            intent.putExtra("title", "${articleList?.get(i)?.title?.substring(0, 15)} - ${articleList?.get(i)?.url?.substring(0, 20)}")

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return articleList!!.size
    }

    inner class ItemCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container : LinearLayout = itemView.findViewById(R.id.container) as LinearLayout
        var articleImage : ImageView = itemView.findViewById(R.id.articleImage) as ImageView
        var tvTitle : TextView = itemView.findViewById(R.id.tvTitle) as TextView
        var tvDesc : TextView = itemView.findViewById(R.id.tvDesc) as TextView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filteredResult: List<Article>
                filteredResult = if (charSequence.isEmpty()) {
                    originalList!!
                } else {
                    getFilteredResult(charSequence.toString().lowercase())
                }
                val results = FilterResults()
                results.values = filteredResult
                return results
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                articleList = filterResults.values as List<Article>
                notifyDataSetChanged()
            }
        }
    }

    private fun getFilteredResult(constraint: String): List<Article> {
        val results: MutableList<Article> = ArrayList<Article>()
        for (article in originalList!!) {
            if (article.title?.lowercase()!!.contains(constraint)) {
                results.add(article)
            } else if (article.description?.lowercase()!!.contains(constraint)) {
                results.add(article)
            }
        }
        return results
    }
}