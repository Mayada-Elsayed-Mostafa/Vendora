package com.example.vendora.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.core.navigation.SignIn
import com.example.vendora.ui.cart_screen.CartScreen
import com.example.vendora.ui.screens.brandDetails.BrandDetailsScreen
import com.example.vendora.ui.screens.category.CategoryScreen
import com.example.vendora.ui.screens.home.HomeScreen
import com.example.vendora.ui.screens.sign.SignInScreen

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
                Column(modifier = Modifier.padding(24.dp)) {
                    HomeScreen()
                }
            }

            composable<Category> {
                CategoryScreen()
            }

            composable<Me> {
                Column(modifier = Modifier.padding(24.dp)) {
                    Icon(imageVector = Icons.Filled.AccountBox, contentDescription = null)
                    Button(
                        onClick = {},
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Test Button")
                    }
                }
            }

            composable<ScreenRoute.CartScreen>{
                CartScreen()
            }

            composable<BrandDetails>{ navBackStackEntry ->
                val brandDetails: BrandDetails = navBackStackEntry.toRoute()
                BrandDetailsScreen(
                    id = brandDetails.id,
                    navigateUp = { navController.navigateUp() }
                )
            }

            composable<SignIn> {
                SignInScreen()
            }
        }
    }
}