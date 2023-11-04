package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun InvalidCardDialog(
    onDismiss : () -> Unit,
    onConfirm : () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Rounded.WarningAmber,
                contentDescription = "Card invalid warning icon",
                modifier = Modifier
                    .size(80.dp),
                tint = Color.Yellow
            )

            Text(
                text = "Invalid Credit Card on file. Please enter a valid credit card to continue.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
            )

            Button(onClick = onConfirm) {
                Text("Edit Card Info")
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun InvalidCardDialogPreview() {
    InvalidCardDialog(onDismiss = { /*TODO*/ }) {
    }
}