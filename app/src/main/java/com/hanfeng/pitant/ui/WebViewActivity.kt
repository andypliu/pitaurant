package com.hanfeng.pitant.ui

import android.webkit.WebView
import android.os.Bundle
import android.app.Activity
import android.util.Log
import com.hanfeng.pitant.R
import android.webkit.WebViewClient




class WebViewActivity : Activity() {

        private var webView: WebView? = null

        public override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.webview)

            webView = findViewById(R.id.webview)
            webView!!.settings.javaScriptEnabled = true
            webView!!.webViewClient = WebViewClient()
            if (intent.hasExtra("uri")) {
                Log.d("WebViewActivity", intent.getStringExtra("uri"))
                webView!!.loadUrl(intent.getStringExtra("uri"))
            }
        }

}