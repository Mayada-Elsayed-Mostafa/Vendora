package com.example.vendora.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute(
    val title: String,
) {
    // CartScreen
    @Serializable
    object CartScreen : ScreenRoute("CartScreen")
}