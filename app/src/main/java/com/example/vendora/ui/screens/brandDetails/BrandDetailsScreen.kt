package com.example.vendora.ui.screens.brandDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vendora.R

@Composable
fun BrandDetailsScreen(
    id: Long = 450846785767
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {

        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            TopScreenSearchBar()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreenSearchBar() {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchBarActive by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    SearchBar(
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        leadingIcon = {
            IconButton(
                onClick = {}
            ){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        query = searchQuery,
        onQueryChange = { searchQuery = it },
        active = isSearchBarActive,
        onActiveChange = { isSearchBarActive = it },
        onSearch = {},
        shape = SearchBarDefaults.fullScreenShape,
        trailingIcon = {
            Row {
                IconButton(
                    onClick = {}
                ){
                    Icon(
                        painter = painterResource(R.drawable.filter),
                        contentDescription = null,
                    )
                }
            }
        }
    ) {
        Column {
            Text("result 1")
            Text("result 1")
            Text("result 1")
        }
    }
}

@Preview
@Composable
private fun TopScreenSearchBarPreview() {
    TopScreenSearchBar()
}