package com.jik.personaltutoringservice.ui

import android.app.Activity.RESULT_OK
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
    userName : String?,
    email : String?,
    phone : String?
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
        Text("Hello, $userName!", fontSize = 20.sp)
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
            /* NOTE: If User not able to login with existing email account error continues,
                consider making separate button & page for login
            Button(
                onClick = onSignupClick,
                modifier = Modifier.fillMaxWidth(.5f)
            ) {
                Text(text = "Signup", color = Color.White, fontSize = 10.sp)
            }

             */
        } else {
            Text("Email: $email")
            Text("Phone: $phone")
        }
    }
}