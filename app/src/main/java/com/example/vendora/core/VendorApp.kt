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
import androidx.navigation.toRoute
import com.example.vendora.core.navigation.BottomNavBar
import com.example.vendora.core.navigation.BrandDetails
import com.example.vendora.core.navigation.Category
import com.example.vendora.core.navigation.Home
import com.example.vendora.core.navigation.Me
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.ui.cart_screen.CartScreen
import com.example.vendora.ui.cart_screen.CheckoutScreen
import com.example.vendora.ui.cart_screen.PaymentScreen
import com.example.vendora.ui.payment_methods.VisaScreen
import com.example.vendora.ui.screens.address.view.AddAddressScreen
import com.example.vendora.ui.screens.address.view.AddressScreen
import com.example.vendora.ui.screens.brandDetails.BrandDetailsScreen
import com.example.vendora.ui.screens.discount.view.DiscountScreen
import com.example.vendora.ui.screens.home.HomeScreen

@Composable
fun VendorApp() {
    val navController = rememberNavController()

    Scaffold(
        snackbarHost = {},
        topBar = {

        },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        println(innerPadding)
        NavHost(
            navController = navController,
            startDestination = Home,
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
                Column(modifier = Modifier.padding(24.dp)) {
                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
                }
            }

            composable<Me> {
                /*Column(modifier = Modifier.padding(24.dp)) {
                    Icon(imageVector = Icons.Filled.AccountBox, contentDescription = null)
                }*/

            }

            composable<ScreenRoute.CartScreen>{
                CartScreen(paddingValues = innerPadding , navController)
            }

            composable<ScreenRoute.CheckoutScreenRoute>{ navBackStackEntry ->
                val token = navBackStackEntry.toRoute<ScreenRoute.CheckoutScreenRoute>().token
                CheckoutScreen(token,navController)
            }

            composable<ScreenRoute.PaymentScreenRoute>{ navBackStackEntry ->
                val price:Double =navBackStackEntry.toRoute<ScreenRoute.PaymentScreenRoute>().price
                val token:String =navBackStackEntry.toRoute<ScreenRoute.PaymentScreenRoute>().token
                PaymentScreen( token = token,totalPrice = price, navController = navController)
            }

            composable<ScreenRoute.VisaScreenRoute> { navBackStackEntry ->
                val token = navBackStackEntry.toRoute<ScreenRoute.VisaScreenRoute>().token
                VisaScreen(token)
            }

                composable<BrandDetails>{ navBackStackEntry ->
                val brandDetails: BrandDetails = navBackStackEntry.toRoute()
                BrandDetailsScreen(
                    id = brandDetails.id,
                    navigateUp = { navController.navigateUp() }
                )
            }

            composable<ScreenRoute.AddAddressScreen>{
                AddAddressScreen(navController)
            }

            composable<ScreenRoute.AddressScreen>{
                AddressScreen(navController)
            }

            composable<ScreenRoute.DiscountScreen>{
                DiscountScreen(){
                    navController.popBackStack()
                }
            }
        }
    }
}