package com.example.vendora.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vendora.R
import com.example.vendora.core.navigation.ScreenRoute
import com.example.vendora.ui.ui_model.GiftCardAd
import com.example.vendora.ui.ui_model.couponList

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            HomeHeader(navController)
            Spacer(Modifier.height(16.dp))
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }

        item {
            GiftCardAd(couponList)
        }
    }
}

@Composable
fun HomeHeader(navController: NavController) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.user)
                .crossfade(true)
                .build(),
            contentDescription = "user avatar",
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(
                text = "Welcome 👋",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Zeyad Ma'moun",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { /* do something */ }) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "Favorite"
            )
        }

        IconButton(onClick = { navController.navigate(ScreenRoute.CartScreen) }) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Favorite"
            )
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var userQuery by remember { mutableStateOf("") }
    val searchBarBackgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = Color(0xFFC0C0C0)

    OutlinedTextField(
        value = userQuery,
        onValueChange = {
            userQuery = it
        },
        modifier = modifier,
        placeholder = {
            Text(
                text = "Search for products",
                style = MaterialTheme.typography.titleSmall,
                color = contentColor.copy(alpha = 0.7f) // Slightly transparent placeholder
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.titleSmall,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = searchBarBackgroundColor,
            unfocusedContainerColor = searchBarBackgroundColor,
            disabledContainerColor = searchBarBackgroundColor,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = Color.Transparent, // No border when focused
            unfocusedBorderColor = Color.Transparent, // No border when unfocused
            errorBorderColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = contentColor,
        ),
        shape = RoundedCornerShape(8.dp),
    )
}

@Composable
fun GiftCardAd(giftCardsList: List<GiftCardAd>) {
    // here we can replace the number with amount of gift card coming from api.
    val pagerState = rememberPagerState(pageCount = {giftCardsList.size})
    val context = LocalContext.current

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Card(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .height(IntrinsicSize.Min)
                    .clip(shape = RoundedCornerShape(16.dp))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            "${giftCardsList[page].couponAmount} %",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )

                        Text(
                            giftCardsList[page].couponTitle,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            giftCardsList[page].couponDesc,
                            maxLines = 2,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(giftCardsList[page].couponImage)
                            .build(),
                        contentDescription = giftCardsList[page].couponTitle,
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                            .width(170.dp)
                    )
                }
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun HomeHeaderPreview() {
//    HomeHeader()
//}

//@Preview
//@Composable
//private fun SearchBarPreview() {
//    SearchBar()
//}

//@Preview
//@Composable
//private fun GiftCardAdPreview() {
//    GiftCardAd(couponList)
//}

//@Preview(showSystemUi = true)
//@Composable
//private fun HomeScreenPreview() {
//    HomeScreen()
//}