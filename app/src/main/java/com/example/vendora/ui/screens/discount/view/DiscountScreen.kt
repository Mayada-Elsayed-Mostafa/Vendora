package com.example.vendora.ui.screens.discount.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vendora.ui.cart_screen.CustomAppBar
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.brandDetails.OnLoading
import com.example.vendora.ui.screens.discount.viewModel.DiscountViewModel
import com.example.vendora.utils.wrapper.Result


@Composable
fun DiscountScreen(viewModel: DiscountViewModel = hiltViewModel(),back: (selectedCode: String) -> Unit) {
    val discountCode by viewModel.discountCodes.collectAsState()
    var selectedCode by remember { mutableStateOf<String?>(null) }
    LaunchedEffect (Unit){
        viewModel.getDiscountCode()
    }

    when(val state = discountCode){
        is Result.Loading -> OnLoading()
        is Result.Failure ->  OnError { viewModel.getDiscountCode() }
        is Result.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                CustomAppBar("Discount Code") { }
                LazyColumn {
                    items(state.data){item ->
                        DiscountItem(
                            code = item.code,
                            selected = selectedCode == item.code,

                            ) {
                            selectedCode = item.code
                        }
                        Divider()
                    }
                }
                Spacer(Modifier.height(48.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground) ,
                    onClick = {
                    /* Selected Code */
                        selectedCode?.let { back(it) }

                    }
                ){
                    Text("Apply",style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),)
                }
            }

        }
    }

}

@Composable
fun DiscountItem(
    code: String,
    selected:Boolean,
    onSelect:()->Unit
) {
    Card (
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onSelect() },
        colors =CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp)
    ){
        Row (
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.DarkGray, shape = CircleShape)
                    .padding(12.dp),
                tint = Color.White
            )

            Spacer(Modifier.width(16.dp))

            Column (modifier = Modifier.weight(1f)){
                Text(
                    text = code,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
            }

            RadioButton(
                selected=selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.onSurface)
                )
        }
    }
}