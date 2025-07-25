package com.example.vendora.core.navigation

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
    data class PaymentScreenRoute(val price : Double = 10.0,val token:String,val orderId: Int,val promoCode:String) : ScreenRoute("Payment")

    @Serializable
    data class VisaScreenRoute(
        val token:String,
        val orderId: Int,
        val firstToken: String,
        val discountCode: String
    ) : ScreenRoute("Visa")

    @Serializable
    object AddAddressScreen : ScreenRoute("AddAddressScreen")

    @Serializable
    object AddressScreen : ScreenRoute("AddressScreen")

    @Serializable
    object DiscountScreen : ScreenRoute("DiscountScreen")

    @Serializable
    object SettingsScreen : ScreenRoute("SettingsScreen")

    @Serializable
    data class CashDeliveryScreen(
        val orderId: Int,
        val firstToken: String,
        val type: String,
        val discountCode:String
    ) : ScreenRoute("CashDeliveryScreen")

    @Serializable
    object CouponScreen : ScreenRoute("CouponScreen")
}