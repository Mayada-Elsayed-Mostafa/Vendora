package com.example.vendora.core.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navRoutes = routes.map {
        it.toString()
    }

    if (!navRoutes.contains(currentDestination?.route)) {
        return
    }

    NavigationBar(
        containerColor = (if (isSystemInDarkTheme()) Color.Black else Color.White)
    ) {
        routes.forEach { route ->
            val isSelected = currentDestination?.route == route.toString()
            NavigationBarItem(
                enabled = true,
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(
                            if (isSelected) route.icon else route.outLineIcon
                        ), contentDescription = route.name
                    )
                },
                label = {
                    Text(route.name)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,

                    ),
                selected = isSelected,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}