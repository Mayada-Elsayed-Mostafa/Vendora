package com.example.vendora.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vendora.R
import com.example.vendora.data.local.UserPreferences
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    var hasCheckedLogin by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        delay(1500)
        val userId = userPreferences.getUserId()
        isLoggedIn = userPreferences.isUserLoggedIn()
        hasCheckedLogin = true
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.vendora),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(400.dp)
                    .padding(16.dp)
            )
        }
    }

    LaunchedEffect(hasCheckedLogin) {
        if (hasCheckedLogin) {
            if (isLoggedIn) {
                onNavigateToHome()
            } else {
                onNavigateToSignIn()
            }
        }
    }
}
