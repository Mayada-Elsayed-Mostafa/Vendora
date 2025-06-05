package com.example.vendora.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.vendora.R
import com.example.vendora.data.remote.RemoteDataSourceImpl
import com.example.vendora.data.repo_implementation.ProductsRepositoryImpl
import com.example.vendora.domain.model.brands.SmartCollection
import com.example.vendora.domain.usecase.brands.GetBrandsUseCase
import com.example.vendora.ui.ui_model.GiftCardAd
import com.example.vendora.ui.ui_model.couponList
import com.example.vendora.utils.wrapper.Result

@Composable
fun HomeScreen() {

    val repo = ProductsRepositoryImpl(RemoteDataSourceImpl())
    val useCase = GetBrandsUseCase(repo)
    val factory = HomeViewModelFactory(useCase)
    val viewModel: HomeViewModel = viewModel(factory = factory)

    val brands = viewModel.brands.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchBrands()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            Column {
                HomeHeader()
                Spacer(Modifier.height(16.dp))
            }
        }

        when (brands.value) {
            is Result.Failure -> item(span = { GridItemSpan(maxLineSpan) }) {
                Text((brands.value as Result.Failure).exception.toString())
            }

            Result.Loading -> item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            }

            is Result.Success -> {
                val list = (brands.value as Result.Success).data.smart_collections
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }

                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    GiftCardAd(couponList)
                }

                items(list) { brand ->
                    BrandCard(
                        brand,
                        Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )
                }
            }
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
    val pagerState = rememberPagerState(pageCount = { giftCardsList.size })
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
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
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

@Composable
fun BrandCard(brand: SmartCollection, modifier: Modifier) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .shadow(elevation = 4.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(brand.image.src)
                .build(),
            contentDescription = brand.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(8.dp)
        ) {
            Text(
                text = brand.title,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

//@Preview
//@Composable
//private fun BrandCardPreview() {
//    val brand = SmartCollection(
//        handle = "adidas",
//        id = 450846785767,
//        image = Image(
//            alt = "ADIDAS",
//            height = 200,
//            src = "https://cdn.shopify.com/s/files/1/0747/4964/0935/collections/smart_collections_2.jpg?v=1748957094",
//            width = 200
//        ),
//        title = "ADIDAS"
//    )
//    BrandCard(brand, Modifier.width(90.dp))
//}

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