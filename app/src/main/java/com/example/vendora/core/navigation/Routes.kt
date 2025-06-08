package com.example.vendora.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

// here we define class we will use in bottom navigation bar
data class BottomNavRoute<T : Any>(val name: String, val route: T, val icon: ImageVector){
    override fun toString(): String {
        return route::class.qualifiedName ?: "UnKnownRoute"
    }
}

val routes = listOf(
    BottomNavRoute("Home", Home, Icons.Filled.Home),
    BottomNavRoute("Category", Category, Icons.Outlined.Menu),
    BottomNavRoute("Me", Me, Icons.Filled.AccountCircle)
)

// Home Tab
@Serializable
object Home

// Category Tab
@Serializable
object Category

// Me Tab
@Serializable
object Me

@Serializable
data class BrandDetails(val id: Long = 450846785767)