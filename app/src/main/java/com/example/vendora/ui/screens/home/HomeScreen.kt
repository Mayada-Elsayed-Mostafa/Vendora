package com.example.vendora.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vendora.R

@Composable
fun HomeScreen() {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            HomeHeader()
            Spacer(Modifier.height(16.dp))
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
    }
}

@Composable
fun HomeHeader() {
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

        IconButton(onClick = { /* do something */ }) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Favorite"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
fun GiftCardAd(modifier: Modifier = Modifier) {
    Card {

    }
}

//@Preview
//@Composable
//private fun HomeHeaderPreview() {
//    HomeHeader()
//}

@Preview
@Composable
private fun SearchBarPreview() {
    SearchBar()
}

//@Preview
//@Composable
//private fun GiftCardAdPreview() {
//    GiftCardAd()
//}