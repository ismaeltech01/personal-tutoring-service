package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase

/* Possibly implement if later decide to add Google signon
@Composable
fun LoginRegisterPage(
    auth : FirebaseAuth,
    mainActivity : Activity
) {
    val showForm = remember {
        mutableStateOf(false)
    }

    Button (onClick = ) {
        Image(Icons.Rounded.Email, contentDescription = "Email icon")
        Text(text = "Email")
    }
}
*/
@Composable
fun LoginPage() {


}

@Composable
fun RegisterPage(
    //onRegisterClick : () -> Unit,
    auth : FirebaseAuth,
    mainActivity : Activity
) {
    //TODO: Use state to handle email & password
    var nameState by remember {
        mutableStateOf("")
    }

    var emailState by remember {
        mutableStateOf("")
    }

    var passwdState by remember {
        mutableStateOf("")
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    )
    {
        FormField(
            "Name",
            nameState,
            { nameState = it },
            false
        )

        FormField(
            "Email",
             emailState,
            { emailState = it },
            false
        )

        FormField(
            "Password",
            passwdState,
            { passwdState = it },
            false
        )

        Button(onClick = {
            //TODO: Add check to see if fields are empty
            auth.createUserWithEmailAndPassword(emailState, passwdState)
                .addOnCompleteListener(mainActivity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        //Problematic code
                        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(nameState).build()

                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User profile updated.")
                                }
                            }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            mainActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }) {}
    }
}

@Composable
fun FormField(
    text : String,
    value : String,
    onValueChange : (String) -> Unit,
    isPasswdField : Boolean
) {
    Column (
        horizontalAlignment = Alignment.Start
    ) {
        Text(text)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .border(2.dp, Color.Black)
                .padding(5.dp)
        )

    }
}