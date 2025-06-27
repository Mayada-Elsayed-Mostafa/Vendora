package com.example.vendora.ui.screens.sign

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vendora.R

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color.Black else Color.White
    val contentColor = if (isDark) Color.White else Color.Black
    val onButtonColor = if (isDark) Color.Black else Color.White
    val secondaryColor = if (isDark) Color.LightGray else Color.Gray
    val cardColor = if (isDark) Color.DarkGray else Color.White
    val buttonColor = if (isDark) Color.LightGray else Color.Black

    val signInState by viewModel.signInState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val showDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }

    LaunchedEffect(signInState.successMessage) {
        signInState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(signInState.errorMessage) {
        signInState.errorMessage?.let {
            dialogMessage.value = it
            showDialog.value = true
        }
    }

    LaunchedEffect(signInState.isSuccess) {
        if (signInState.isSuccess) {
            onNavigateToHome()
            viewModel.clearMessages()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 16.dp)
        ) {
            OutlinedButton(
                onClick = onContinueAsGuest,
                modifier = Modifier
                    .align(Alignment.TopEnd),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(1.dp, secondaryColor),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 8.dp,
                    vertical = 4.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = secondaryColor
                )
            ) {
                Text("Continue as Guest", fontSize = 14.sp)
            }
        }

        Image(
            painter = painterResource(if (isSystemInDarkTheme()) R.drawable.vendora_dark else R.drawable.vendora),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
        )

        Box(modifier = Modifier.fillMaxSize()) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 12.dp)
                    .offset(y = 4.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {}

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
                    .offset(y = 12.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.hello_welcome_back),
                        fontSize = 24.sp,
                        color = contentColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Start
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.email), color = secondaryColor) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password), color = secondaryColor) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.clearMessages()
                            viewModel.signInUser(email.trim(), password)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !signInState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor,
                            contentColor = onButtonColor
                        )
                    ) {
                        if (signInState.isLoading) {
                            CircularProgressIndicator(
                                color = contentColor,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(stringResource(R.string.sign_in), color = onButtonColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.don_t_have_an_account), color = secondaryColor)
                        TextButton(onClick = onNavigateToSignUp) {
                            Text(stringResource(R.string.sign_up), color = buttonColor)
                        }
                    }
                }
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp)
    )

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                viewModel.clearMessages()
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    viewModel.clearMessages()
                }) {
                    Text(stringResource(R.string.try_again), color = contentColor)
                }
            },
            title = { Text(stringResource(R.string.oops), color = contentColor) },
            text = { Text(dialogMessage.value, color = contentColor) },
            containerColor = cardColor
        )
    }

    val context = LocalContext.current
    BackHandler {
        (context as? Activity)?.finish()
    }
}
