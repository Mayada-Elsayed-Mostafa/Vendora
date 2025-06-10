package com.example.vendora.ui.payment_methods

import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.vendora.ui.cart_screen.CustomAppBar

@Composable
fun VisaScreen(token:String) {
    val iframeId = 929198
    val url = "https://accept.paymob.com/api/acceptance/iframes/$iframeId?payment_token=$token"

    Column (
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ){
        CustomAppBar("VISA") { }
        Spacer(Modifier.height(24.dp))
        Divider()

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            }
        )
    }
}