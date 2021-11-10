package com.app.article.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.app.article.R
import com.app.article.component.ProgressDialog

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

class ArticleActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val ivBack : ImageView = findViewById(R.id.ivBack)
        val tvHeader : TextView = findViewById(R.id.tvHeader)

        val bundle : Bundle = intent.extras!!
        tvHeader.text = bundle.getString("title")

        ivBack.setOnClickListener(this)
        progressDialog = ProgressDialog(this)
        progressDialog.show()

        val rlFailed : RelativeLayout = findViewById(R.id.rlFailed)
        val webView : WebView = findViewById(R.id.wvArticle)
        webView.settings.javaScriptEnabled = true

        val btnReload : Button = findViewById(R.id.btnReload)
        btnReload.setOnClickListener {
            progressDialog.show()
            webView.reload()
        }

        var isFailed : Boolean = false
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                println("webview onPageStarted")
                isFailed = false;
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                println("webview onPageFinished")
                super.onPageFinished(view, url)
                progressDialog.dismiss()

                if (isFailed) {
                    rlFailed.visibility = View.VISIBLE
                    webView.visibility = View.GONE
                } else {
                    rlFailed.visibility = View.GONE
                    webView.visibility = View.VISIBLE
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                println("webview onReceivedError")
                isFailed = true
            }
        }

        webView.loadUrl(bundle.getString("url")!!)
    }

    override fun onClick(view : View?) {
        onBackPressed()
    }
}