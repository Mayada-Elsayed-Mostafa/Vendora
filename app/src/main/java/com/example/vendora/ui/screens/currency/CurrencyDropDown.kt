package com.example.vendora.ui.screens.currency

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vendora.utils.wrapper.Result


@Composable
fun CurrencyDropDown(currencyViewModel: CurrencyViewModel = hiltViewModel()) {
    val state by currencyViewModel.currencies.collectAsState()
    val selected by currencyViewModel.selectedCurrency.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        currencyViewModel.getCurrency()
    }

    Box(modifier = Modifier
        .padding(16.dp)
        .clickable { expanded = true }) {

        when (state) {
            is Result.Loading -> {}
            is Result.Success -> {
                val currencies = (state as Result.Success).data.data.keys.toList()

                Text(text = selected)

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(text = currency) },
                            onClick = {
                                currencyViewModel.changeSelectedCurrency(currency)
                                expanded = false
                            }
                        )
                    }
                }
            }

            is Result.Failure -> {
                Text(
                    text = "You used all your monthly requests",
                    style = MaterialTheme.typography.titleSmall
                    )
            }
        }
    }
}
