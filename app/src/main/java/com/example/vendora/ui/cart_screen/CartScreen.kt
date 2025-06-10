package com.example.vendora.ui.cart_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.domain.model.payment.AuthTokenResponse
import com.example.vendora.ui.cart_screen.viewModel.PaymobViewModel
import com.example.vendora.utils.wrapper.Result
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val color: String? = null,
    val size: String? = null,
    var quantity: Int = 1
)


@Composable
fun CartScreen( paddingValues: PaddingValues = PaddingValues(), navController: NavHostController,viewModel: PaymobViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getTokenForAuthentication()
    }
    val getFirstToken by viewModel.getTokenState.collectAsState()
    var firstToken by remember { mutableStateOf("") }

    var cartItems by  remember {
        mutableStateOf(listOf(
            CartItem(1, "Essence Mascara Lash Princess", 250.00,"https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/thumbnail.webp", "Color", "M"),
            CartItem(2, "Eyeshadow Palette with Mirror", 120.00,"https://cdn.dummyjson.com/product-images/beauty/eyeshadow-palette-with-mirror/thumbnail.webp", "Color", "M"),
            CartItem(3, "Powder Canister", 150.00,"https://cdn.dummyjson.com/product-images/beauty/powder-canister/thumbnail.webp", "Color", "M"),
            CartItem(4, "Red Lipstick", 100.00,"https://cdn.dummyjson.com/product-images/beauty/red-lipstick/thumbnail.webp", "Color", "M"),
            CartItem(5, "Red Lipstick", 100.00,"https://cdn.dummyjson.com/product-images/beauty/red-lipstick/thumbnail.webp", "Color", "M"),
            CartItem(6, "Red Lipstick", 100.00,"https://cdn.dummyjson.com/product-images/beauty/red-lipstick/thumbnail.webp", "Color", "M"),
        ))
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ){
        Text(
            text = "Cart",
            style = MaterialTheme.typography.titleSmall,
            )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cartItems){ item ->
                CartItem(
                    item = item,
                    onCountChange = {newQuantity ->
                        cartItems = cartItems.map {
                            if (it.id == item.id) it.copy(quantity = newQuantity)
                            else it
                        }
                    },
                    onDelete = {
                        cartItems = cartItems.filter { it.id != item.id }
                    }

                )
            }
        }

        //////

        when (getFirstToken){
            is Result.Success -> {
                firstToken = (getFirstToken as Result.Success<AuthTokenResponse>).data.token
                println("token :$firstToken" )
                CheckoutButton(cartItems ){
                    navController.navigate(ScreenRoute.CheckoutScreenRoute(firstToken))
                }
            }
            is Result.Failure -> Text("Auth failed")
            Result.Loading -> Text("Authenticating...")

        }


        
    }
}

@Composable
fun CartItem (item: CartItem , onCountChange : (Int)->Unit , onDelete : ()->Unit ) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
           modifier = Modifier
               .fillMaxWidth()
               .height(120.dp)
               .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.imageUrl)
                    .build(),
                contentDescription = item.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF35383f))
            )


            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(2f)
            )
            {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))
                // color and size
                Row( verticalAlignment = Alignment.CenterVertically)
                {
                    Text(
                        text ="${item.color}",
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Text(
                        text = " | Size = ${item.size}",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${item.price}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )

            }

            //count and delete
            Column (
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ){
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                )
                {
                    IconButton(onClick = {
                        if (item.quantity >1){
                            onCountChange(item.quantity - 1)
                        }else {
                            onDelete()
                        }
                    },
                        modifier = Modifier.size(32.dp)
                        ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Decrease",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "${item.quantity}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                    )

                    IconButton(onClick = {
                        onCountChange(item.quantity + 1)
                    },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Increase",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun CheckoutButton(cartItems : List<CartItem> ,navToCheckout :()->Unit) {
    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column {
                Text(
                    text = "Total Price",
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "${totalPrice}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
                onClick = {navToCheckout()}
            ) {
                Text(
                    text = "Checkout",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W600),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Checkout",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
