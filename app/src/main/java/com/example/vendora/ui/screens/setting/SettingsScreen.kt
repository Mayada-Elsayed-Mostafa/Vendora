package com.example.vendora.ui.screens.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.vendora.R
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.ui.screens.currency.CurrencyDropDown
import com.example.vendora.ui.screens.currency.CurrencyViewModel
import com.example.vendora.ui.screens.profile.OnSuccess
import com.example.vendora.ui.screens.profile.OptionItem
import com.example.vendora.ui.screens.profile.ProfileAppBar

@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            SettingsAppBar(
                navigateToCart = {navController.navigate(ScreenRoute.CartScreen)},
            )
        },
    ) { innerPadding ->

        HorizontalDivider()
        SettingBody(modifier = Modifier.padding(innerPadding),navController)

    }
}

@Composable
fun SettingBody(modifier: Modifier ,navController: NavHostController) {
    Column(modifier = modifier) {
        OptionItem(
            icon = R.drawable.pin,
            title = "Address"
        ) {
            navController.navigate(ScreenRoute.AddressScreen)
        }
        HorizontalDivider(Modifier.padding(horizontal = 16.dp))

        CurrencyItem(
            icon = R.drawable.sack_dollar,
            action = {}
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppBar(
    navigateToCart: () -> Unit,

) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_category),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        actions = {
            Row {
                IconButton(onClick = { navigateToCart() }) {
                    Icon(
                        painter = painterResource(R.drawable.cart),
                        contentDescription = "cart",
                        modifier = Modifier.size(24.dp)
                    )
                }


            }
        }
    )
}


@Composable
fun CurrencyItem(
    icon: Int,
    color: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    currencyViewModel: CurrencyViewModel = hiltViewModel(),
    action: () -> Unit
) {

    val getChangeRate by currencyViewModel.getChangeRate.collectAsState()

    LaunchedEffect(Unit) {
        currencyViewModel.getCurrency()
        currencyViewModel.getRates()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .clickable { action() }
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        CurrencyDropDown()

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

    }
}