package com.jik.personaltutoringservice.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginPage(
    viewModel: MainViewModel,
    activity: Activity,
    navigate: () -> Unit
) {
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
        Image(
            Icons.Rounded.AccountBox,
            contentDescription = "Login icon",
            modifier = Modifier.size(100.dp)
        )

        EmailField(
            value = emailState,
            onValueChange = { emailState = it }
        )

        PasswordField(
            value = passwdState,
            onValueChange = { passwdState = it }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(onClick = {
            val res = viewModel.LogIn(emailState, passwdState, activity)
            if (res != -1) { navigate() }
        })
        {
            Text("Login")
        }
    }
}

@Composable
fun RegisterPage(
    viewModel: MainViewModel,
    activity: Activity,
    navigate: () -> Unit
) {
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
        Image(
            Icons.Rounded.AccountBox,
            contentDescription = "Register icon",
            modifier = Modifier.size(100.dp)
        )

        NameField(
            value = nameState,
            onValueChange = { nameState = it }
        )


        EmailField(
            value = emailState,
            onValueChange = { emailState = it }
        )

        PasswordField(
            value = passwdState,
            onValueChange = { passwdState = it }
        )

        Spacer(modifier = Modifier.height(5.dp))

        Button(onClick = {
            val res = viewModel.Register(nameState, emailState, passwdState, activity)
            if (res != -1) { navigate() }
        })
        {
            Text("Register")
        }
    }
}

@Composable
fun NameField(
    value : String,
    onValueChange : (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Name") },
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Icon(Icons.Rounded.Info, contentDescription = "Name Icon") }
    )
}

@Composable
fun EmailField(
    value : String,
    onValueChange : (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Icon(Icons.Rounded.Email, contentDescription = "Email Icon") }
    )
}

@Composable
fun PasswordField(
    value : String,
    onValueChange : (String) -> Unit
) {
    var passwdVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = "Password Icon") },
        trailingIcon = {
            val icon = if (!passwdVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (!passwdVisible) "Show Password" else "Hide Password"

            IconButton(onClick = { passwdVisible = !passwdVisible }) {
                Icon(icon, description)
            }
        },
        visualTransformation = if (passwdVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}