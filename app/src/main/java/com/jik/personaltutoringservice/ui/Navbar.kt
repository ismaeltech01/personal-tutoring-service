package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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

val IconModifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp).fillMaxSize()

@Composable
fun Navbar(modifier : Modifier) {
    Row (horizontalArrangement = Arrangement.Center, modifier = NavbarModifier) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp).weight(.18f).fillMaxHeight()) {
            Icon(Icons.Rounded.Home, "Home button", IconModifier)
        }
        Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp).weight(.18f).fillMaxHeight() ) {
            Icon(Icons.Rounded.Search, "Search button", IconModifier)
        }
        Button (onClick = { /*TODO*/ }, modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp).weight(.28f).fillMaxHeight()) {
            Icon(Icons.Rounded.AccountCircle, "Profile button", IconModifier)
        }
        Button (onClick = { /*TODO*/ }, modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp).weight(.18f).fillMaxHeight()) {
            Icon(Icons.Rounded.MailOutline, "Mail button", IconModifier)
        }
        Button (onClick = { /*TODO*/ }, modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp).weight(.18f).fillMaxHeight()) {
            Icon(Icons.Rounded.MoreVert, "More button", IconModifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavbarPreview() {
    PersonalTutoringServiceTheme {
        Navbar()
    }
}