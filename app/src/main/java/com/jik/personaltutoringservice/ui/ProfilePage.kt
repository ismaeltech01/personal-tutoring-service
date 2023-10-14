package com.jik.personaltutoringservice.ui

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfilePage(
    modifier : Modifier,
    onLoginClick : () -> Unit
) {
    var registered = true;
    var userName = "Guest"

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(
            Icons.Rounded.AccountCircle,
            contentDescription = "Profile Image",
            modifier = Modifier.size(100.dp)
        )
        Text("Hello, $userName", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(3.dp))
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(.5f)
        ) {
            if (!registered)
                Text(text = "Login", color = Color.White, fontSize = 10.sp)
        }
    }
}