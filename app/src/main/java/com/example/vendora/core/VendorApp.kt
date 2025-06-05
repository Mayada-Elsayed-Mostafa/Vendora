package com.example.vendora.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vendora.core.navigation.BottomNavBar
import com.example.vendora.core.navigation.Category
import com.example.vendora.core.navigation.Home
import com.example.vendora.core.navigation.Me
import com.example.vendora.ui.screens.home.HomeScreen

@Composable
fun VendorApp() {
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = {},
        topBar = {},
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home> {
                Column(modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)) {
                    HomeScreen()
                }
            }

            composable<Category> {
                Column(modifier = Modifier.padding(24.dp)) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
                }
            }

            composable<Me> {
                Column(modifier = Modifier.padding(24.dp)) {
                    Icon(imageVector = Icons.Filled.AccountBox, contentDescription = null)
                }
            }
        }
    }
}