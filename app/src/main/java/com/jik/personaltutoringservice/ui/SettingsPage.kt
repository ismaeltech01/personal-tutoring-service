package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SettingsPage(
    viewModel: MainViewModel,
    activity: Activity,
    navigate: () -> Unit
) {
    var firstNameState by remember { mutableStateOf("") }

    var editFirstName by remember { mutableStateOf(false) }

    var middleNameState by remember { mutableStateOf("") }

    var editMiddleName by remember { mutableStateOf(false) }

    var lastNameState by remember { mutableStateOf("") }

    var editLastName by remember { mutableStateOf(false) }

    var usernameState by remember { mutableStateOf("") }

    var editUserNameState by remember { mutableStateOf(false) }

    var phoneState by remember { mutableStateOf("") }

    var editPhoneState by remember { mutableStateOf(false) }

    var addressState by remember { mutableStateOf("") }

    var editAddressState by remember { mutableStateOf(false) }

    var emailState by remember { mutableStateOf("") }

    var editEmailState by remember { mutableStateOf(false) }

    var passwdState by remember { mutableStateOf("") }

    var editPasswdState by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 15.dp)
    )
    {
        item {
            TextField(
                value = firstNameState,
                onValueChange = { firstNameState = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = { Icon(Icons.Rounded.Security, contentDescription = "Security Code Icon") },
                trailingIcon = {
                    val icon = if (!editFirstName) Icons.Filled.Edit else Icons.Filled.EditOff
                    val description = if (!editFirstName) "Edit On" else "Edit Off"

                    IconButton(onClick = { editFirstName = !editFirstName }) {
                        Icon(icon, description)
                    }
                },
                enabled = editFirstName
            )

//            NameField(
//                value = middleNameState,
//                onValueChange = { middleNameState = it },
//                text = "Middle Name (Optional)"
//            )
//
//            NameField(
//                value = lastNameState,
//                onValueChange = { lastNameState = it },
//                text = "Last Name"
//            )
//
//            UsernameField(
//                value = usernameState,
//                onValueChange = { usernameState = it }
//            )
//
//            PhoneField(
//                value = phoneState,
//                onValueChange = {
//                    //Used to restrict user input to numbers
//                    if (it.length <= 10 && !Regex("[^0-9]").containsMatchIn(it)) phoneState = it
//                }
//            )
//
//            AddressField(
//                value = addressState,
//                onValueChange = { addressState = it }
//            )
//
//            EmailField(
//                value = emailState,
//                onValueChange = { emailState = it }
//            )
//
//            PasswordField(
//                value = passwdState,
//                onValueChange = { passwdState = it }
//            )
//
//            Text(
//                text = "NOTE: Email & Password will be used to Login!",
//                modifier = Modifier.padding(vertical = 10.dp)
//            )

            Button(onClick = {
                val allValid = ValidateInputs(firstNameState, activity = activity)

                //TODO: Add validation that user data is not the same as previous data in UpdateUserData()
                if (allValid) { viewModel.UpdateUserData() }

            })
            {
                Text("Save Changes")
            }
        }
    }
}

fun ValidateInputs(
    firstName : String = "",
    middleName : String = "",
    lastName : String = "",
    email : String = "",
    password : String = "",
    address : String = "",
    activity: Activity
) : Boolean {
    var invalidCount = 0;

    if (firstName != "" && !isValidName(firstName)) {
        ShowToast(activity, "name", "First Name")
        invalidCount++
    }

    if (middleName != "" && !isValidName(middleName)) {
        ShowToast(activity, "name", "Middle Name")
        invalidCount++
    }

    if (lastName != "" && !isValidName(lastName)) {
        ShowToast(activity, "name", "Last Name")
        invalidCount++
    }

    if (email != "" && !isValidEmail(email)) {
        ShowToast(activity, "email")
        invalidCount++
    }

    if (password != "" && password.length < 6) {
        ShowToast(activity, "password")
        invalidCount++
    }

    return invalidCount == 0
}

fun ShowToast(activity: Activity, type : String, text : String = "") {
    val toastStr : String

    if (type == "name") {
        toastStr = "$text cannot contain characters outside of A-Z, a-z, & -"
    } else if (type == "email") {
        toastStr = "Invalid email. Try again."
    } else {
        toastStr = "Password must be at least 6 characters long."
    }

    Toast.makeText(
        activity,
        toastStr,
        Toast.LENGTH_LONG,
    ).show()
}