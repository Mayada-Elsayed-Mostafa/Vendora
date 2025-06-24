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
import com.example.vendora.core.navigation.Favorites
import com.example.vendora.core.navigation.Home
import com.example.vendora.core.navigation.Me
import com.example.vendora.core.navigation.OrderDetails
import com.example.vendora.core.navigation.PaymentResult
import com.example.vendora.core.navigation.ProductInfo
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.core.navigation.Search
import com.example.vendora.core.navigation.SignIn
import com.example.vendora.core.navigation.SignUp
import com.example.vendora.core.navigation.SplashScreen
import com.example.vendora.ui.cart_screen.CartScreen
import com.example.vendora.ui.cart_screen.CheckoutScreen
import com.example.vendora.ui.cart_screen.PaymentScreen
import com.example.vendora.ui.payment_methods.CashOnDeliveryScreen
import com.example.vendora.ui.payment_methods.VisaScreen
import com.example.vendora.ui.screens.SplashScreen
import com.example.vendora.ui.screens.address.view.AddAddressScreen
import com.example.vendora.ui.screens.address.view.AddressScreen
import com.example.vendora.ui.screens.brandDetails.BrandDetailsScreen
import com.example.vendora.ui.screens.category.CategoryScreen
import com.example.vendora.ui.screens.discount.view.CouponScreen
import com.example.vendora.ui.screens.discount.view.DiscountScreen
import com.example.vendora.ui.screens.favorites.FavoritesScreen
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
        NavHost(
            navController = navController,
            startDestination = SplashScreen,
        ) {
            composable<SplashScreen> {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(Home) {
                            popUpTo(SplashScreen) { inclusive = true }
                        }
                    },
                    onNavigateToSignIn = {
                        navController.navigate(SignIn) {
                            popUpTo(SplashScreen) { inclusive = true }
                        }
                    }
                )
            }

            composable<Home> {
                HomeScreen(
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToFavorites = { navController.navigate(Favorites) },
                    navigateToBrandDetails = { brandId ->
                        navController.navigate(BrandDetails(id = brandId))
                    },
                    navigateToLogin = { navController.navigate(SignIn) },
                    navigateToCoupon = { navController.navigate(ScreenRoute.CouponScreen) },
                    paddingValues = innerPadding
                )
            }

            composable<Category> {
                CategoryScreen(
                    navigateToProductInfo = { productId ->
                        navController.navigate(ProductInfo(productId))
                    },
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToLogin = { navController.navigate(SignIn) },
                    navigateToFavorite = { navController.navigate(Favorites) }
                )
            }

            composable<Me> {
                ProfileScreen(
                    navigateToCart = { navController.navigate(ScreenRoute.CartScreen) },
                    navigateToSettings = { navController.navigate(ScreenRoute.SettingsScreen) },
                    navigateToFavorite = { navController.navigate(Favorites) },
                    navigateToOrders = { navController.navigate(CustomerOrders) },
                    navigateToLogin = { navController.navigate(SignIn) }
                )
            }

            composable<Search> {
                SearchScreen { id ->
                    navController.navigate(ProductInfo(id))
                }
            }

            composable<ScreenRoute.CartScreen> {
                CartScreen(paddingValues = innerPadding, navController)
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
                        popUpTo(ScreenRoute.CartScreen) {
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
                    onNavigateToHome = { navController.navigate(Home) },
                    onContinueAsGuest = { navController.navigate(Home) })
            }

            composable<SignUp> {
                SignUpScreen(
                    onNavigateToSignIn = { navController.navigate(SignIn) },
                    onContinueAsGuest = { navController.navigate(Home) })
            }

            composable<ProductInfo> { navBackStackEntry ->
                val productInfo: ProductInfo = navBackStackEntry.toRoute()
                ProductInfoScreen(
                    productId = productInfo.id,
                    navigateToLogin = { navController.navigate(SignIn) }
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

            composable<Favorites> {
                FavoritesScreen(
                    onProductClick = { product ->
                        navController.navigate(ProductInfo(product.id))
                    }
                )
            }


            composable<ScreenRoute.CashDeliveryScreen> { navBackStackEntry ->

                val token: String =
                    navBackStackEntry.toRoute<ScreenRoute.CashDeliveryScreen>().firstToken
                val type: String = navBackStackEntry.toRoute<ScreenRoute.CashDeliveryScreen>().type
                val orderId: Int =
                    navBackStackEntry.toRoute<ScreenRoute.CashDeliveryScreen>().orderId
                CashOnDeliveryScreen(
                    token = token,
                    orderId = orderId,
                    type = type,
                    onNavigateBack = { navController.popBackStack(Home, false) }
                )
            }


            composable<ScreenRoute.CouponScreen> {
                CouponScreen() {
                    navController.popBackStack()
                }
            }

        }
    }
}