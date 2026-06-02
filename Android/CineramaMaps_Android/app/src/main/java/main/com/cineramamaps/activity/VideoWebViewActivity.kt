package main.com.cineramamaps.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class VideoWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)

        val videoUrl = intent.getStringExtra("video_url")

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        videoUrl?.let {
            webView.loadUrl(it)
        }
    }
}