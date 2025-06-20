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
import com.example.vendora.core.navigation.CustomerOrders
import com.example.vendora.core.navigation.Home
import com.example.vendora.core.navigation.Me
import com.example.vendora.core.navigation.OrderDetails
import com.example.vendora.core.navigation.PaymentResult
import com.example.vendora.core.navigation.ProductInfo
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.core.navigation.Search
import com.example.vendora.core.navigation.SignIn
import com.example.vendora.core.navigation.SignUp
import com.example.vendora.ui.cart_screen.CartScreen
import com.example.vendora.ui.cart_screen.CheckoutScreen
import com.example.vendora.ui.cart_screen.PaymentScreen
import com.example.vendora.ui.payment_methods.VisaScreen
import com.example.vendora.ui.screens.address.view.AddAddressScreen
import com.example.vendora.ui.screens.address.view.AddressScreen
import com.example.vendora.ui.screens.brandDetails.BrandDetailsScreen
import com.example.vendora.ui.screens.category.CategoryScreen
import com.example.vendora.ui.screens.discount.view.DiscountScreen
import com.example.vendora.ui.screens.home.HomeScreen
import com.example.vendora.ui.screens.order.CustomerOrders
import com.example.vendora.ui.screens.order.OrderDetailsScreen
import com.example.vendora.ui.screens.order.PaymentResultScreen
import com.example.vendora.ui.screens.productInfo.ProductInfoScreen
import com.example.vendora.ui.screens.profile.ProfileScreen
import com.example.vendora.ui.screens.search.SearchScreen
import com.example.vendora.ui.screens.setting.SettingsScreen
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
                CategoryScreen(
                    navigateToProductInfo = { productId ->
                        navController.navigate(ProductInfo(productId))
                    },
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToFavorite = {}
                )
            }

            composable<Me> {
                ProfileScreen(
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToFavorite = {},
                    navigateToOrders = { navController.navigate(CustomerOrders) },
                    navigateToSettings = { navController.navigate(ScreenRoute.SettingsScreen) },
                )
            }

            composable<Search> {
                SearchScreen { id ->
                    navController.navigate(ProductInfo(id))
                }
            }

            composable<ScreenRoute.CartScreen>{
                CartScreen(paddingValues = innerPadding , navController)
            }

            composable<ScreenRoute.CheckoutScreenRoute> { navBackStackEntry ->
                val token = navBackStackEntry.toRoute<ScreenRoute.CheckoutScreenRoute>().token
                CheckoutScreen(token, navController)
            }

            composable<ScreenRoute.PaymentScreenRoute> { navBackStackEntry ->
                val price: Double =
                    navBackStackEntry.toRoute<ScreenRoute.PaymentScreenRoute>().price
                val token: String =
                    navBackStackEntry.toRoute<ScreenRoute.PaymentScreenRoute>().token
                val orderId: Int =
                    navBackStackEntry.toRoute<ScreenRoute.PaymentScreenRoute>().orderId
                PaymentScreen(
                    token = token,
                    totalPrice = price,
                    orderId = orderId,
                    navController = navController
                )
            }

            composable<ScreenRoute.VisaScreenRoute> { navBackStackEntry ->
                val token = navBackStackEntry.toRoute<ScreenRoute.VisaScreenRoute>().token
                val firstToken = navBackStackEntry.toRoute<ScreenRoute.VisaScreenRoute>().firstToken
                val orderId = navBackStackEntry.toRoute<ScreenRoute.VisaScreenRoute>().orderId
                VisaScreen(
                    token = token,
                    firstToken = firstToken,
                    orderId = orderId
                ) { order_Id, first_token, _ ->
                    navController.navigate(
                        PaymentResult(id = order_Id, token = first_token)
                    ) {
                        popUpTo(ScreenRoute.CartScreen){
                            inclusive = true
                        }
                    }
                }
            }

            composable<BrandDetails> { navBackStackEntry ->
                val brandDetails: BrandDetails = navBackStackEntry.toRoute()
                BrandDetailsScreen(
                    id = brandDetails.id,
                    navigateUp = { navController.navigateUp() },
                    navigateToProduct = { productId -> navController.navigate(ProductInfo(productId)) }
                )
            }

            composable<SignIn> {
                SignInScreen(
                    onNavigateToSignUp = { navController.navigate(SignUp) },
                    onNavigateToHome = { navController.navigate(Home) })
            }

            composable<SignUp> {
                SignUpScreen(onNavigateToSignIn = { navController.navigate(SignIn) })
            }

            composable<ProductInfo> { navBackStackEntry ->
                val productInfo: ProductInfo = navBackStackEntry.toRoute()
                ProductInfoScreen(
                    productId = productInfo.id
                )
            }

            composable<ScreenRoute.AddAddressScreen> {
                AddAddressScreen(navController)
            }

            composable<ScreenRoute.AddressScreen> {
                AddressScreen(navController)
            }

            composable<ScreenRoute.DiscountScreen> {
                DiscountScreen { selectedCode ->
                    println(selectedCode)
                    if (selectedCode != null) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_discount_code", selectedCode)
                    }
                    navController.navigateUp()
                }
            }

            composable<CustomerOrders> {
                CustomerOrders(
                    navigateUp = { navController.navigateUp() },
                    navigateToOrderDetails = { id ->
                        navController.navigate(OrderDetails(id = id))
                    }
                )
            }

            composable<OrderDetails> { navBackStackEntry ->
                val orderDetails: OrderDetails = navBackStackEntry.toRoute()
                OrderDetailsScreen(
                    orderId = orderDetails.id,
                    navigateUp = { navController.navigateUp() }
                )
            }

            composable<PaymentResult> { navBackStackEntry ->
                val paymentResult: PaymentResult = navBackStackEntry.toRoute()
                PaymentResultScreen(
                    orderId = paymentResult.id,
                    token = paymentResult.token,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<ScreenRoute.SettingsScreen> {
                SettingsScreen(navController)
            }
        }
    }
}
