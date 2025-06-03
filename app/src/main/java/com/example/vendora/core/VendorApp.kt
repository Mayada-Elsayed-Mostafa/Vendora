package com.example.vendora.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun VendorApp() {
    val navController = rememberNavController()

    Text("Hello Android", modifier = Modifier.padding(32.dp))

//    Scaffold(
//        snackbarHost = {},
//        topBar = {}
//    ) { innerPadding ->
//        NavHost(navController = navController, startDestination = ) { }
//    }
}

// here we define our destinations