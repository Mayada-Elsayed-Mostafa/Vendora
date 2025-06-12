package com.example.vendora.ui.screens.address.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.ui.cart_screen.CustomAppBar
import com.example.vendora.ui.screens.address.viewModel.AddressViewModel



@Composable
fun AddAddressScreen(navController: NavHostController,viewModel: AddressViewModel = hiltViewModel()) {
    var type by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val message by viewModel.message.collectAsState()
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    Column (modifier = Modifier
        .fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)
        .padding(16.dp)) {

        CustomAppBar("Add New Address") {navController.popBackStack() }
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField("type", value = type, onValueChange = {type=it})
        CustomOutlinedTextField("city", value = city, onValueChange = {city=it})
        CustomOutlinedTextField("street", value = street, onValueChange = {street=it})
        CustomOutlinedTextField("phone", value = phone, onValueChange = {phone=it})
        CustomOutlinedTextField("country", value = country, onValueChange = {country=it})
        CustomOutlinedTextFieldWithIcon ("address", value = address, onValueChange = {address=it}, onClickButton = {/*Pick location*/})


        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isDefault, onCheckedChange = { isDefault = it })
            Text("Set as default address")
        }

        Spacer(modifier = Modifier.height(32.dp))

        AddAddressBottom("Save Address") {
            val address = AddressEntity(
                type = type,
                city = city,
                country = country,
                isDefault = isDefault,
                address = address,
                phone = phone
            )
            viewModel.insertAddress(address)
            navController.popBackStack()
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(placeholder:String,value: String,onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(8.dp),

    )
    Divider(Modifier.padding(8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextFieldWithIcon(placeholder:String,value: String,onValueChange: (String) -> Unit,onClickButton: () -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            IconButton(onClick = onClickButton) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Pick location",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
    Divider(Modifier.padding(8.dp))
}