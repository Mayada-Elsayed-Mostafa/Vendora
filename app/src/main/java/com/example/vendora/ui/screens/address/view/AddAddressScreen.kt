package com.example.vendora.ui.screens.address.view

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.vendora.domain.model.address.Province
import com.example.vendora.ui.cart_screen.CustomAppBar
import com.example.vendora.ui.screens.address.viewModel.AddressViewModel
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.brandDetails.OnLoading
import com.example.vendora.utils.wrapper.Result


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

    val provinces by viewModel.provinces.collectAsState()
    val selectedProvince by viewModel.selectedProvince.collectAsState()
    var expandedProvince by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    Column (modifier = Modifier
        .fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)
        .padding(16.dp)) {

        CustomAppBar("Add New Address") {navController.popBackStack() }
        Spacer(modifier = Modifier.height(16.dp))

        when(val result = provinces){
            is Result.Failure -> OnError()
            is Result.Loading -> OnLoading()
            is Result.Success -> {

                CustomOutlinedTextField("Title", value = type, onValueChange = {type=it})

                CustomReadOnlyField(label = "country", value = "Egypt")
                CustomDropdownField(
                    label = "Select city",
                    selectedItem = selectedProvince,
                    items = result.data.country.provinces,
                    onItemSelected = { viewModel.changeSelectedProvince(it)} ,
                    itemLabel = { "$it " }
                )
                CustomOutlinedTextField("street", value = street, onValueChange = {street=it})
                CustomOutlinedTextField(
                    "phone",
                    value = phone,
                    onValueChange = {
                        phone=it
                        phoneError = if (viewModel.isValidPhoneNumber(it)) null else "Invalid phone number"
                    },
                    isError = phoneError != null,
                    errorMessage = phoneError
                )

                CustomOutlinedTextFieldWithIcon ("address", value = address, onValueChange = {address=it}, onClickButton = {/*Pick location*/})




            }
        }




        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isDefault, onCheckedChange = { isDefault = it })
            Text("Set as default address")
        }



        Spacer(modifier = Modifier.height(32.dp))

        AddAddressBottom("Save Address") {
            val address = AddressEntity(
                type = type,
                city = selectedProvince?:"",
                country = "Egypt",
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
fun CustomOutlinedTextField(
    placeholder:String,
    value: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit
)
{
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
            )
        },
        isError = isError,
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
    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
        )
    }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  CustomDropdownField(
    label: String,
    selectedItem:  String?,
    items: List<Province?>,
    onItemSelected: (String) -> Unit,
    itemLabel: (String) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedItem?.let { itemLabel(it) } ?: "",
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },
        readOnly = true,
        placeholder = { Text(label) },
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = {
                    if (item != null) {
                        Text(itemLabel(item.name))
                    }
                },
                onClick = {
                    if (item != null) {
                        onItemSelected(item.name)
                    }
                    expanded = false
                }
            )
        }
    }

    Divider(Modifier.padding(8.dp))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomReadOnlyField(
    label: String,
    value: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        placeholder = { Text(label) },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
    Divider(Modifier.padding(8.dp))
}

