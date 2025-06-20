package com.example.vendora.ui.ui_model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun GuestModeDialog(
    attributes: DialogAttributes
) {
    AlertDialog(
        onDismissRequest = attributes.onDismiss,
        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
        title = {
            Text(text = attributes.dialogTitle)
        },
        text = {
            Text(attributes.guestModeMessage)
        },
        confirmButton = {
            Button(
                onClick = attributes.onAccept,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            ) {
                Text(attributes.acceptTitle)
            }
        },
        dismissButton = {
            TextButton(
                onClick = attributes.onDismiss,
            ) {
                Text(
                    attributes.dismissTitle,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }
    )
}

data class DialogAttributes(
    val dialogTitle: String = "Guest Mode",
    val guestModeMessage: String = "You're currently using the app as a guest. Some features may be limited. Would you like to log in?",
    val acceptTitle: String = "Log In",
    val dismissTitle: String = "Continue as Guest",
    val onDismiss: () -> Unit,
    val onAccept: () -> Unit,
)