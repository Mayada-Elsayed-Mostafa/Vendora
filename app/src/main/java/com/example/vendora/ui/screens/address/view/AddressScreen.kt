package com.example.vendora.ui.screens.address.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.vendora.R
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.domain.model.address.AddressEntity
import com.example.vendora.ui.cart_screen.CustomAppBar
import com.example.vendora.ui.cart_screen.CustomEmpty
import com.example.vendora.ui.screens.address.viewModel.AddressViewModel
import com.example.vendora.utils.wrapper.Result


@Composable
fun AddressScreen(navController: NavHostController,viewModel: AddressViewModel = hiltViewModel()) {
    val addressState by viewModel.address.collectAsState()
    val message by viewModel.message.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getAllAddresses()
    }


    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp)
            ) {
                CustomAppBar("Address") { navController.popBackStack() }
                Spacer(Modifier.height(8.dp))
            }
        },
        bottomBar = {
            AddAddressBottom("Add New Address") {
                navController.navigate(ScreenRoute.AddAddressScreen)
            }

        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp) )
        {
            when (addressState) {
                is Result.Loading -> CircularProgressIndicator()
                is Result.Success -> {
                    val addresses = (addressState as Result.Success<List<AddressEntity>>).data
                    if(addresses.isEmpty()){
                        CustomEmpty("Add New Address")
                    }else{
                        LazyColumn {
                            items(addresses) { address ->

                                AddressItem(
                                    address = address,
                                    onClickDefault = {
                                        viewModel.insertAddress(address.copy(isDefault = true))
                                    },
                                    navToUpdateAddress={
                                        /*nav to update*/
                                        navController.navigate(ScreenRoute.AddAddressScreen)
                                    },
                                    onDeleteClick = {
                                        viewModel.deleteAddress(address.id)
                                    }
                                )
                                Divider(Modifier.padding(vertical = 8.dp))
                            }


                        }
                    }

                }
                is Result.Failure -> {
                    Text("Failed to load addresses")
                }
            }


        }
    }




}





@Composable
fun AddressItem(
    address: AddressEntity,
    navToUpdateAddress: () -> Unit,
    onClickDefault: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val showDeleteDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navToUpdateAddress() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Row (
                        modifier =  Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainer),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row (
                            modifier =  Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .background(MaterialTheme.colorScheme.onSurface),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "adddress",
                                tint = MaterialTheme.colorScheme.surfaceContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                    }
                    Spacer(Modifier.width(16.dp))
                    Column (verticalArrangement = Arrangement.SpaceBetween){

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = address.type,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(address.phone, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(address.address, style = MaterialTheme.typography.bodyMedium)
                        }

                    }


                }

                IconButton(
                    onClick = {showDeleteDialog.value = true},
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.trash),
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = address.isDefault,
                    onClick = { onClickDefault() }
                )
                Text("Set as default")

                if (address.isDefault) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "DEFAULT",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }

    if (showDeleteDialog.value) {
        ConfirmDeleteDialog(
            message = "Are you sure you want to delete this address?",
            onConfirm = {
                onDeleteClick()
                showDeleteDialog.value = false
            },
            onDismiss = {
                showDeleteDialog.value = false
            }
        )
    }
}



@Composable
fun AddAddressBottom(title:String,navTo:()->Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground
            ),
            onClick = {

                navTo()
            }
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Checkout",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun ConfirmDeleteDialog(
    title: String = "Confirm Deletion",
    message: String = "Are you sure you want to delete this item?",
    confirmButtonText: String = "Delete",
    cancelButtonText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelButtonText)
            }
        }
    )
}
