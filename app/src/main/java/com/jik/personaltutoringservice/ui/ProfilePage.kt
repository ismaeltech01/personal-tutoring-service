package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

@Composable
fun ProfilePage(modifier : Modifier) {
    var userName = "Guest"

    Column (modifier = modifier) {
        Image(Icons.Rounded.AccountCircle, contentDescription = "Profile Image")
        Text("Hello, $userName!")
    }
}