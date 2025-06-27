package com.example.vendora.ui.payment_methods

import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.vendora.ui.cart_screen.CustomAppBar

@Composable
fun VisaScreen(
    token: String,
    orderId: Int,
    firstToken: String,
    discountCode: String,
    onPaymentResult: (Int,String,Boolean,String) -> Unit,
) {
    val iframeId = 929198
    val url = "https://accept.paymob.com/api/acceptance/iframes/$iframeId?payment_token=$token"
    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url.toString()
                    val urlsc = request?.url?.scheme
                    println("$$$$$$$$$$$$$$$$$$$$$"+urlsc)
                    if (url.contains("accept.paymobsolutions.com/api/acceptance/post_pay")) {
                        val uri = Uri.parse(url)
                        val isSuccess = uri.getQueryParameter("success") == "true"
                        val txnResponse = uri.getQueryParameter("txn_response_code")

                        if (isSuccess && txnResponse == "APPROVED") {
                            onPaymentResult(orderId,firstToken, true,discountCode)
                        } else {
                            onPaymentResult(orderId,firstToken, false,discountCode)
                        }

                        return true
                    }

                    return false
                }
            }
            loadUrl(url)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        CustomAppBar("VISA") {}

        Divider()
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize()
        )
    }
}