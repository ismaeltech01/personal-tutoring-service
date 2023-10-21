package com.jik.personaltutoringservice.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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
            if (isValidEmail(emailState)) {
                val res = viewModel.LogIn(emailState, passwdState, activity)
                if (res != -1) {
                    navigate()
                }
            } else {
                Toast.makeText(
                    activity,
                    "Invalid email. Try again.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        })
        {
            Text("Login")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(
    viewModel: MainViewModel,
    activity: Activity,
    navigate: () -> Unit
) {
    var firstNameState by remember {
        mutableStateOf("")
    }

    var middleNameState by remember {
        mutableStateOf("")
    }

    var lastNameState by remember {
        mutableStateOf("")
    }

    var usernameState by remember {
        mutableStateOf("")
    }

    var phoneState by remember {
        mutableStateOf("")
    }

    var addressState by remember {
        mutableStateOf("")
    }

    var emailState by remember {
        mutableStateOf("")
    }


    var passwdState by remember {
        mutableStateOf("")
    }

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 15.dp)
    )
    {
        item {
            Image(
                Icons.Rounded.AccountBox,
                contentDescription = "Register icon",
                modifier = Modifier.size(100.dp)
            )

            NameField(
                value = firstNameState,
                onValueChange = { firstNameState =  it },
                text = "First Name"
            )

            NameField(
                value = middleNameState,
                onValueChange = { middleNameState = it },
                text = "Middle Name (Optional)"
            )

            NameField(
                value = lastNameState,
                onValueChange = { lastNameState = it },
                text = "Last Name"
            )

            UsernameField(
                value = usernameState,
                onValueChange = { usernameState = it }
            )

            PhoneField(
                value = phoneState,
                onValueChange = {
                    //Used to restrict user input to numbers
                    if (it.length <= 10 && !Regex("[^0-9]").containsMatchIn(it)) phoneState = it
                }
            )

            AddressField(
                value = addressState,
                onValueChange = { addressState = it }
            )

            EmailField(
                value = emailState,
                onValueChange = { emailState = it }
            )

            PasswordField(
                value = passwdState,
                onValueChange = { passwdState = it }
            )

            Text(
                text = "NOTE: Email & Password will be used to Login!",
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Button(onClick = {
                if (isValidEmail(emailState)) {
                    val res =
                        viewModel.Register(
                            firstName = firstNameState,
                            middleName = middleNameState,
                            lastName = lastNameState,
                            userName = usernameState,
                            phone = phoneState,
                            address = addressState,
                            email = emailState,
                            password = passwdState,
                            activity = activity
                        )
                    if (res != -1) {
                        navigate()
                    }
                } else {
                    Toast.makeText(
                        activity,
                        "Invalid email. Try again.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            })
            {
                Text("Register")
            }
        }
    }
}

/***
 * Checks if string is a valid email.
 *
 * Reference: https://www.codevscolor.com/android-kotlin-validate-email
 */
fun isValidEmail(email : String) : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun NameField(
    value : String,
    onValueChange : (String) -> Unit,
    text : String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text) },
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Icon(Icons.Rounded.Info, contentDescription = "Name Icon") }
    )
}

@Composable
fun UsernameField(
    value : String,
    onValueChange : (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Username") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = "Username Icon") }
    )
}

@Composable
fun PhoneField(
    value : String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Phone") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = "Phone Icon") }
    )
}

@Composable
fun AddressField(
    value : String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Address") },
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Icon(Icons.Rounded.Home, contentDescription = "Address Icon") }
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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