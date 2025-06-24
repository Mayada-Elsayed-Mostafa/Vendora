package com.example.vendora.ui.screens.discount.view

import android.widget.Toast
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vendora.R
import com.example.vendora.ui.cart_screen.CustomAppBar
import com.example.vendora.ui.screens.brandDetails.OnError
import com.example.vendora.ui.screens.brandDetails.OnLoading
import com.example.vendora.ui.screens.discount.viewModel.DiscountViewModel
import com.example.vendora.utils.wrapper.Result
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalContext


@Composable
fun CouponScreen(viewModel: DiscountViewModel = hiltViewModel(),back:()->Unit) {

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
                CustomAppBar("Discount Code") {back() }
                LazyColumn {
                    items(state.data){item ->
                        CouponItem(
                            code = item.code,
                            selected = selectedCode == item.code,
                            onSelect = { selectedCode = item.code}
                            )
                        Divider()
                    }
                }

            }

        }
    }

}

@Composable
fun CouponItem(
    code: String,
    selected:Boolean,
    onSelect:()->Unit,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Card (
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable {
            onSelect()
            clipboardManager.setText(AnnotatedString(code))
            Toast.makeText(context, "Copied: $code", Toast.LENGTH_SHORT).show()
                                                                   },
        colors =CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(16.dp)
    ){
        Row (
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                // imageVector = Icons.Default.Star,
                painter = painterResource(R.drawable.offer),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.DarkGray, shape = CircleShape),

                tint = Color.Unspecified
            )

            Spacer(Modifier.width(16.dp))

            Column (modifier = Modifier.weight(1f)){
                Text(
                    text = code,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Discount 10 % off",
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