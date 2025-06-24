package com.example.vendora.ui.screens.profile

import android.util.Log
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.vendora.R
import com.example.vendora.ui.cart_screen.viewModel.CartViewModel
import com.example.vendora.ui.ui_model.DialogAttributes
import com.example.vendora.ui.ui_model.GuestModeDialog

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToCart: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToOrders: () -> Unit,
    navigateToLogin: () -> Unit
) {

    val userInfo = viewModel.userInfo.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.collectUserState()
        Log.d("Profile", userInfo.value.email)
        Log.d("Profile", userInfo.value.isGuest.toString())
    }

    Scaffold(
        topBar = {
            ProfileAppBar(
                navigateToCart = navigateToCart,
                navigateToSettings = navigateToSettings
            )
        },
    ) { innerPadding ->
        println(innerPadding)
        OnSuccess(
            modifier = Modifier.padding(innerPadding),
            navigateToOrders = navigateToOrders,
            userInfo = userInfo.value,
            viewModel = viewModel,
            navigateToLogin = navigateToLogin
        )
    }
}

@Composable
fun OnSuccess(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel(),
    navigateToOrders: () -> Unit,
    userInfo: UserInfo,
    viewModel: ProfileViewModel,
    navigateToLogin: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val dialogAttributes = remember {
        mutableStateOf(
            DialogAttributes(
                onDismiss = { showDialog = false },
                onAccept = { navigateToLogin() }
            )
        )
    }

    if (showDialog) {
        GuestModeDialog(
            attributes = dialogAttributes.value
        )
    }

    Column(
        modifier = modifier
    ) {
        ProfileAvatarSection(
            email = userInfo.email,
            username = userInfo.fullName
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        //orders item
        OptionItem(
            icon = R.drawable.order,
            title = "Orders"
        ) {
            if (userInfo.isGuest) {
                dialogAttributes.value = DialogAttributes(
                    onDismiss = { showDialog = false },
                    onAccept = { navigateToLogin() }
                )
                showDialog = true
            } else {
                navigateToOrders()
            }
        }
        //wishlist item
        OptionItem(
            icon = R.drawable.wishlist,
            title = "Wishlist"
        ) {
            if (userInfo.isGuest) {
                dialogAttributes.value = DialogAttributes(
                    onDismiss = { showDialog = false },
                    onAccept = { navigateToLogin() }
                )
                showDialog = true
            } else {
                //TODO add navigation to favorites
            }
        }
        // logout item
        if (!userInfo.isGuest) {
            OptionItem(
                icon = R.drawable.logout,
                color = Color.Red,
                title = "Logout"
            ) {
                dialogAttributes.value = DialogAttributes(
                    guestModeMessage = "you are about to logout?",
                    acceptTitle = "logout",
                    dismissTitle = "cancel",
                    onDismiss = { showDialog = false },
                    onAccept = {
                        viewModel.logoutUser()
                        showDialog = false
                        navigateToLogin()
                    }
                )
                showDialog = true
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    navigateToCart: () -> Unit,
    navigateToSettings: () -> Unit
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

                IconButton(onClick = { navigateToSettings() }) {
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
fun ProfileAvatarSection(
    username: String,
    email: String
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp)
    ) {
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
            username,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            email,
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
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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
        navigateToSettings = {}
    )
}

//@Preview
//@Composable
//private fun ProfileAvatarSectionPreview() {
//    ProfileAvatarSection()
//}

@Preview
@Composable
private fun OptionItemPreview() {
    OptionItem(
        icon = R.drawable.outline_me,
        title = "Address",
        action = {}
    )
}