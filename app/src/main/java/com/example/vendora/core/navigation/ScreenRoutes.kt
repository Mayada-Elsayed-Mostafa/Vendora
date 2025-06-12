package com.example.vendora.core.navigation

import com.example.vendora.ui.cart_screen.CartItem
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute(
    val title: String,
) {
    // CartScreen
    @Serializable
    object CartScreen : ScreenRoute("CartScreen")

    //Checkout Screen
    @Serializable
    data class CheckoutScreenRoute(val token:String) : ScreenRoute("Checkout")


    @Serializable
    data class PaymentScreenRoute(val price : Double = 10.0,val token:String) : ScreenRoute("Payment")

    @Serializable
    data class VisaScreenRoute(val token:String) : ScreenRoute("Visa")

    @Serializable
    object AddAddressScreen : ScreenRoute("AddAddressScreen")

    @Serializable
    object AddressScreen : ScreenRoute("AddressScreen")

    @Serializable
    object DiscountScreen : ScreenRoute("DiscountScreen")
}