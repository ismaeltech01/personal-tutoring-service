package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun RegisterPage(
    onRegisterClick : () -> Unit
) {
    Column {
        Button(onClick = onRegisterClick) {
            Image(Icons.Rounded.Email, contentDescription = "Email icon")
            Text(text = "Email")
        }
    }
}