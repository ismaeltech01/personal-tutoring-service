package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val IconModifier = Modifier
    .padding(0.dp)
    .fillMaxSize()

@Composable
fun Navbar(modifier : Modifier, homeOnClick : () -> Unit, searchOnClick : () -> Unit, profileOnClick : () -> Unit, messagesOnClick : () -> Unit, moreOnClick : () -> Unit) {
    Row (horizontalArrangement = Arrangement.Center, modifier = modifier) {
        Button(onClick = homeOnClick, modifier = Modifier
            .padding(1.dp)
            .weight(.2f)
            .fillMaxHeight()
            .background(Color.Transparent)) {
            Icon(Icons.Rounded.Home, "Home button", IconModifier)
        }
        Button(onClick = searchOnClick, modifier = Modifier
            .padding(1.dp)
            .weight(.2f)
            .fillMaxHeight()
            .background(Color.Transparent)) {
            Icon(Icons.Rounded.Search, "Search button", IconModifier)
        }
        Button (onClick = profileOnClick, modifier = Modifier
            .padding(1.dp)
            .weight(.2f)
            .fillMaxHeight()
            .background(Color.Transparent)) {
            Icon(Icons.Rounded.AccountCircle, "Profile button", IconModifier)
        }
        Button (onClick = messagesOnClick, modifier = Modifier
            .padding(1.dp)
            .weight(.2f)
            .fillMaxHeight()
            .background(Color.Transparent)) {
            Icon(Icons.Rounded.MailOutline, "Mail button", IconModifier)
        }
        Button (onClick = moreOnClick, modifier = Modifier
            .padding(1.dp)
            .weight(.2f)
            .fillMaxHeight()
            .background(Color.Transparent)) {
            Icon(Icons.Rounded.MoreVert, "More button", IconModifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavbarPreview() {
    PersonalTutoringServiceTheme {
        Column {
            Spacer(modifier = Modifier.fillMaxHeight())
            Navbar(Modifier, {}, {}, {}, {}, {})
        }
    }
}