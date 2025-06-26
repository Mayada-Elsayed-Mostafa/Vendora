package com.example.vendora.ui.screens.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.vendora.R
import com.example.vendora.domain.model.product.Product
import com.example.vendora.ui.cart_screen.CustomAppBar
import com.example.vendora.ui.screens.currency.changeCurrency
import com.example.vendora.ui.screens.search.LottieLoader

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()

    if (favorites.isEmpty()) {
        Box(
            Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieLoader(
                    resId = R.raw.favorite_animation,
                    modifier = Modifier
                        .width(250.dp)
                        .height(250.dp)
                )
                Text(
                    text = "No favorite products yet!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            items(favorites) { product ->
                FavoriteProductCard(
                    product = product,
                    isFavorite = true,
                    onFavoriteClick = { viewModel.removeFromFavorites(product.id) },
                    onProductClick = { onProductClick(product) }
                )
            }
        }
    }
}


@Composable
fun FavoriteProductCard(
    product: Product,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onProductClick: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .clickable { onProductClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = product.image.src,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = Color.Black
                    )
                }
            }
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = product.variants[0].price.toDouble().changeCurrency(context),
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
