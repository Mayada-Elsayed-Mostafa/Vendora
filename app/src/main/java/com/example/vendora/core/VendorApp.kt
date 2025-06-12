package com.example.vendora.core

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.vendora.core.navigation.BottomNavBar
import com.example.vendora.core.navigation.BrandDetails
import com.example.vendora.core.navigation.Category
import com.example.vendora.core.navigation.Home
import com.example.vendora.core.navigation.Me
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.core.navigation.SignIn
import com.example.vendora.core.navigation.SignUp
import com.example.vendora.ui.cart_screen.CartScreen
import com.example.vendora.ui.screens.brandDetails.BrandDetailsScreen
import com.example.vendora.ui.screens.category.CategoryScreen
import com.example.vendora.ui.screens.home.HomeScreen
import com.example.vendora.ui.screens.profile.ProfileScreen
import com.example.vendora.ui.screens.sign.SignInScreen
import com.example.vendora.ui.screens.sign.SignUpScreen

@Composable
fun VendorApp() {
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = {},
        topBar = {},
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        println(innerPadding)
        NavHost(
            navController = navController,
            startDestination = Me,
        ) {
            composable<Home> {
                HomeScreen(
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToFavorites = {},
                    navigateToBrandDetails = { brandId ->
                        navController.navigate(BrandDetails(id = brandId))
                    },
                    paddingValues = innerPadding
                )
            }

            composable<Category> {
                CategoryScreen(
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToFavorite = {}
                )
            }

            composable<Me> {
                ProfileScreen(
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToFavorite = {},
                    navigateToOrders = {}
                )
            }

            composable<ScreenRoute.CartScreen> {
                CartScreen()
            }

            composable<BrandDetails> { navBackStackEntry ->
                val brandDetails: BrandDetails = navBackStackEntry.toRoute()
                BrandDetailsScreen(
                    id = brandDetails.id,
                    navigateUp = { navController.navigateUp() }
                )
            }

            composable<SignIn> {
                SignInScreen(onNavigateToSignUp = { navController.navigate(SignUp) }, onNavigateToHome = { navController.navigate(Home)})
            }

            composable<SignUp> {
                SignUpScreen(onNavigateToSignIn = { navController.navigate(SignIn) })
            }

        }
    }
}
