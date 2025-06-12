package com.example.vendora.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.network.okHttpClient
import com.example.vendora.GetProductsQuery
import com.example.vendora.R
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Composable
fun ProfileScreen(
    navigateToCart: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToOrders: () -> Unit
) {
    Scaffold(
        topBar = {
            ProfileAppBar(
                navigateToCart = navigateToCart,
                navigateToFavorite = navigateToFavorite
            )
        },
    ) { innerPadding ->
        println(innerPadding)
        OnSuccess(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun OnSuccess(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
    ) {
        ProfileAvatarSection()
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        //address item
        OptionItem(
            icon = R.drawable.pin,
            title = "Address"
        ) {
            coroutineScope.launch {
                val authInterceptor = Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("X-Shopify-Storefront-Access-Token", "702b21e78cf63f5083b1a9317c624bf7") // 👈 Change this
                        .build()
                    chain.proceed(request)
                }

                // 2. Create OkHttpClient with the interceptor
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()

                // Create a client
                val apolloClient = ApolloClient.Builder()
                    .serverUrl("https://mad45-ism-and1.myshopify.com/api/2025-04/graphql.json") // 👈 Use real URL
                    .okHttpClient(okHttpClient)
                    .build()

                // Execute your query. This will suspend until the response is received.
                val response = apolloClient.query(GetProductsQuery(Optional.present(10))).execute()

                println("products.data=${response.data?.products}")
            }
        }
        //orders item
        OptionItem(
            icon = R.drawable.order,
            title = "Orders"
        ) { }
        //wishlist item
        OptionItem(
            icon = R.drawable.wishlist,
            title = "Wishlist"
        ) { }
        // logout item
        OptionItem(
            icon = R.drawable.logout,
            color = Color.Red,
            title = "Logout"
        ) { }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    navigateToCart: () -> Unit,
    navigateToFavorite: () -> Unit
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
                    "Profile",
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

                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        painter = painterResource(R.drawable.settings),
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun ProfileAvatarSection() {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp)
    ){
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.user)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = "user avatar",
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(96.dp)
                .background(Color.Black)
        )

        Text(
            "Username name",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            "+201002176725",
            style = MaterialTheme.typography.titleSmall
        )

    }
}

@Composable
fun OptionItem(
    icon: Int,
    title: String,
    color: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    action: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
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

        Text(
            title,
            color = color,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

    }
}

@Preview
@Composable
private fun ProfileAppBarPreview() {
    ProfileAppBar(
        navigateToCart = {},
        navigateToFavorite = {}
    )
}

@Preview
@Composable
private fun ProfileAvatarSectionPreview() {
    ProfileAvatarSection()
}

@Preview
@Composable
private fun OptionItemPreview() {
    OptionItem(
        icon = R.drawable.outline_me,
        title = "Address",
        action = {}
    )
}