package com.jik.personaltutoringservice.ui

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


//TODO: Implement way to tell if user is a tutor or not
//TODO: Check if user is looking at someone else's profile or their own
@Composable
fun ProfilePage(
    modifier : Modifier,
    onLoginClick : () -> Unit,
    onRegisterClick : () -> Unit,
    loggedIn : Boolean,
    fullName : String,
    userName : String,
    email : String,
    phone : String,
    address : String
) {

    //TODO: Implementing state might help refresh the page whenever login finished (From Guest to User)
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        //TODO: Find way to store & load images (maybe using AsyncImage)
        Image(
            Icons.Rounded.AccountCircle,
            contentDescription = "Profile Image",
            modifier = Modifier.size(100.dp)
        )

        NameDisplay(fullName = fullName)
        Spacer(modifier = Modifier.height(3.dp))

        if (!loggedIn) {
            Button(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(.5f)
            ) {
                Text(text = "Login", color = Color.White)
            }

            Button(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(.5f)
            ) {
                Text(text = "Register", color = Color.White)
            }
        } else {
            Row {
                Icon(Icons.Rounded.Person, "Username Icon")
                Spacer(modifier = Modifier.width(10.dp))
                Text(userName)
            }

            Row {
                Icon(Icons.Rounded.Email, "Email Icon")
                Spacer(modifier = Modifier.width(10.dp))
                Text(email)
            }

            Row {
                Icon(Icons.Rounded.Phone, "Phone Icon")
                Spacer(modifier = Modifier.width(10.dp))
                Text(phone)
            }

            Row {
                Icon(Icons.Rounded.Home, "Home Icon")
                Spacer(modifier = Modifier.width(10.dp))
                Text(address)
            }
        }
    }
}

@Composable
fun NameDisplay(fullName: String) {
    var name = ""
    try {
        val names = fullName.split(" ")
        name = if (names[1].contains("*")) "${names[0]} ${names[2]}" else fullName
    } catch (e : Exception) {
        Log.e(TAG, "NameDisplay:failure -> fullName is likely too short", e)
    }

    Text("Hello, $name!", fontSize = 20.sp)
}