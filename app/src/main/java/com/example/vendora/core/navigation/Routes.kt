package com.example.vendora.core.navigation

import com.example.vendora.R
import com.example.vendora.domain.model.order.DiscountCode
import kotlinx.serialization.Serializable

// here we define class we will use in bottom navigation bar
data class BottomNavRoute<T : Any>(
    val name: String,
    val route: T,
    val icon: Int,
    val outLineIcon: Int
){
    override fun toString(): String {
        return route::class.qualifiedName ?: "UnKnownRoute"
    }
}

val routes = listOf(
    BottomNavRoute("Home", Home, R.drawable.home,R.drawable.outline_home),
    BottomNavRoute("Search", Search, R.drawable.search,R.drawable.outline_search),
    BottomNavRoute("Category", Category, R.drawable.category,R.drawable.outline_category),
    BottomNavRoute("Me", Me, R.drawable.me,R.drawable.outline_me)
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
object Search

@Serializable
data class BrandDetails(val id: Long = 450846785767)

@Serializable
object SignIn

@Serializable
object SignUp

@Serializable
data class ProductInfo(val id: Long)

@Serializable
object CustomerOrders

@Serializable
data class OrderDetails(val id: Long)

@Serializable
data class PaymentResult(
    val id: Int,
    val token: String,
    val isSuccess: Boolean,
    val discountCode: String
)

@Serializable
object Favorites

@Serializable
object SplashScreen