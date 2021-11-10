package com.app.article.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.app.article.R
import com.app.article.component.ProgressDialog
import com.app.article.model.Article
import com.app.article.model.ArticleResponse
import com.app.article.service.Service
import com.app.article.storage.AppDatabase
import com.app.article.ui.adapter.CategoryAdapter
import com.app.article.util.Component
import kotlinx.android.synthetic.main.activity_articles.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

class ArticlesActivity : AppCompatActivity(), CategoryAdapter.CategoryListener {
    private var container : RecyclerView? = null
    private var llOffline : LinearLayout? = null
    private var llEmpty : LinearLayout? = null
    private var llEmpty2 : LinearLayout? = null
    private var srl : SwipeRefreshLayout? = null

    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        progressDialog = ProgressDialog(this)

        container = findViewById(R.id.container)
        container?.layoutManager = LinearLayoutManager(this)

        llOffline = findViewById(R.id.llOffline)
        llEmpty = findViewById(R.id.llEmpty)
        llEmpty2 = findViewById(R.id.llEmpty2)

        srl = findViewById(R.id.srl)
        srl?.setOnRefreshListener(OnRefreshListener {
            srl?.isRefreshing = false
            requestData()
        })

        val rlSearch : RelativeLayout = findViewById(R.id.rlSearch)
        val rlHeader : RelativeLayout = findViewById(R.id.rlHeader)

        val ivSearch : ImageView = findViewById(R.id.ivSearch)
        ivSearch.setOnClickListener {
            rlSearch.visibility = View.VISIBLE
            rlHeader.visibility = View.GONE
        }

        val etSearch : EditText = findViewById(R.id.etSearch)
        etSearch.addTextChangedListener {
            categoryAdapter.filter(it.toString())
        }

        val btnReload : Button = findViewById(R.id.btnReload)
        btnReload.setOnClickListener {
            requestData()
        }

        if (Component.isNetworkAvailable(this)) {
            llOffline?.visibility = View.GONE
            llEmpty?.visibility = View.GONE
            container?.visibility = View.VISIBLE

            requestData()
        } else {
            progressDialog.show()

            llOffline?.visibility = View.VISIBLE
            llEmpty?.visibility = View.GONE
            container?.visibility = View.GONE

            val articleList : List<Article> = AppDatabase.getInstance(this).articleDao().getAll()
            if (articleList.isNotEmpty()) {
                generateItem(articleList)
            } else {
                llEmpty?.visibility = View.VISIBLE
            }

            progressDialog.dismiss()
        }
    }

    private fun generateItem(articleList : List<Article>?) {
        llEmpty?.visibility = View.GONE
        container?.visibility = View.VISIBLE

        val articleData : MutableMap<String, ArrayList<Article>> = mutableMapOf()

        //Clean all record before insert
        AppDatabase.getInstance(this@ArticlesActivity).articleDao().deleteAll()

        for (article in articleList!!) {
            // Insert to database
            AppDatabase.getInstance(this@ArticlesActivity).articleDao().insert(article)

            val dateParse : Date = SimpleDateFormat("yyyy-MM-dd").parse(article.publishedAt)
            val date : String = SimpleDateFormat("yyyy-MM-dd").format(dateParse)

            if (articleData.containsKey(date)) {
                val articles : ArrayList<Article> = articleData[date]!!
                articles.add(article)

                articleData[date] = articles
            } else {
                val articles : ArrayList<Article> = arrayListOf()
                articles.add(article)

                articleData[date] = articles
            }
        }

        categoryAdapter = CategoryAdapter(this@ArticlesActivity, articleData, this@ArticlesActivity)
        container?.adapter = categoryAdapter
    }

    private fun requestData() {
        progressDialog.show()

        Service
            .get
            .news()
            .enqueue(object : Callback<ArticleResponse> {
                override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                    val articleList : List<Article> = AppDatabase.getInstance(this@ArticlesActivity).articleDao().getAll()
                    if (articleList.isNotEmpty()) {
                        generateItem(articleList)
                    } else {
                        llEmpty?.visibility = View.VISIBLE
                    }

                    progressDialog.dismiss()
                }

                override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                    val articleList : List<Article>? = response.body()?.articles
                    generateItem(articleList)

                    progressDialog.dismiss()
                    llOffline?.visibility = View.GONE
                }
            }
        )
    }

    fun showEmptyLabel(active : Int) {
        llEmpty2?.visibility = active
    }

    override fun onCategoryRefresh() {
        requestData()
    }
}