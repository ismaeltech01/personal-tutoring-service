package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jik.personaltutoringservice.ui.theme.PersonalTutoringServiceTheme
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

val NavbarModifier = Modifier.background(color = Color.Blue).fillMaxWidth().fillMaxHeight(.1f)
val IconModifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
val ButtonModifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)

@Composable
fun Navbar() {
    Row (horizontalArrangement = Arrangement.Center, modifier = NavbarModifier) {
        NavbarButton(Icons.Rounded.Home, "Home Navbar button")
        NavbarButton(Icons.Rounded.Search, "Search Navbar button")
        //Replace with profile picture
        NavbarButton(Icons.Rounded.AccountCircle, "Profile Navbar button")
        NavbarButton(Icons.Rounded.MailOutline, "Messaging Navbar button")
        NavbarButton(Icons.Rounded.MoreVert, "More Navbar button")
    }
}

@Composable
fun NavbarButton(icon : ImageVector, desc : String) {
    Button(onClick = { /*TODO*/ }, modifier = ButtonModifier) {
        Icon(icon, desc, IconModifier)
    }
}

@Preview(showBackground = true)
@Composable
fun NavbarPreview() {
    PersonalTutoringServiceTheme {
        Navbar()
    }
}