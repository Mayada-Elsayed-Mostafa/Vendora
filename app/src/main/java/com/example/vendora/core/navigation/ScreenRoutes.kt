package com.example.vendora.core.navigation

import com.example.vendora.ui.cart_screen.CartItem
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute(
    val title: String,
){
    // CartScreen
    @Serializable
    object CartScreen : ScreenRoute("CartScreen")

    //Checkout Screen
    @Serializable
    object CheckoutScreenRoute : ScreenRoute("Checkout")


    @Serializable
    data class PaymentScreenRoute(val price : Double = 10.0) : ScreenRoute("Payment")
}