package com.sliide.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.sliide.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlexWebView(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PlexWebView(
    modifier: Modifier = Modifier
) {
    val webViewState = rememberWebViewState("https://tmobile.plex.tv/")
    val webViewClient = rememberCustomAccompanistWebViewClient()
    WebView(
        modifier = modifier.fillMaxSize(),
        state = webViewState,
        client = webViewClient,
        onCreated = { webView ->
            webView.setup()
        },
    )
}

fun WebView.setup(mediaPlaybackRequiresUserGesture: Boolean = true) {
    this.settings.javaScriptEnabled = true
    this.settings.builtInZoomControls = false
    this.isVerticalScrollBarEnabled = true
    this.settings.domStorageEnabled = true
    this.settings.databaseEnabled = true
    this.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

    this.settings.useWideViewPort = true
    this.settings.loadWithOverviewMode = true
    this.setLayerType(View.LAYER_TYPE_HARDWARE, null)

    val cookieManager: CookieManager = CookieManager.getInstance()
    cookieManager.setAcceptCookie(true)
    cookieManager.acceptCookie()
    cookieManager.setAcceptThirdPartyCookies(this, true)

    this.settings.mediaPlaybackRequiresUserGesture = mediaPlaybackRequiresUserGesture
}

@Composable
fun rememberCustomAccompanistWebViewClient(
): AccompanistWebViewClient {
    return remember {
        object : AccompanistWebViewClient() {
            private var loadedUrl: String? = ""
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loadedUrl = url
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
            ): Boolean {
                return true
            }
        }
    }
}
