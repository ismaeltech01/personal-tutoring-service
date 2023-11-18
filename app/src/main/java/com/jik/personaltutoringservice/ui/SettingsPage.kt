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
    fullName : String,
    userName : String,
    phone : String,
    address: String,
    email: String,
    password: String,
    viewModel: MainViewModel,
    activity: Activity,
    navigate: () -> Unit
) {
    val names = GetNames(fullName = fullName)
    val initUserName = userName
    val initPhone = phone
    val initAddress = address
    val initEmail = email
    val initPassword = password

    var firstNameState by remember { mutableStateOf(names[0]) }
    var middleNameState by remember { mutableStateOf(if (names[1] == "*") "" else names[1]) }
    var lastNameState by remember { mutableStateOf(names[2]) }
    var usernameState by remember { mutableStateOf(userName) }
    var phoneState by remember { mutableStateOf(phone) }
    var addressState by remember { mutableStateOf(address) }
    var emailState by remember { mutableStateOf(email) }
    var passwdState by remember { mutableStateOf(password) }
    var confirmState by remember { mutableStateOf(password) }


    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 15.dp)
    )
    {
        item {
            NameField(
                value = firstNameState,
                onValueChange = { firstNameState = it },
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
                onValueChange = { passwdState = it },
                isConfirm = false
            )

            PasswordField(
                value = passwdState,
                onValueChange = { passwdState = it },
                isConfirm = true
            )

            Button(onClick = {
                val allValid = ValidateInputs(
                    firstName = firstNameState,
                    middleName = middleNameState,
                    lastName = lastNameState,
                    email = emailState,
                    password = passwdState,
                    confirm = confirmState,
                    address = addressState,
                    activity = activity
                )

                //TODO: Add validation that user data is not the same as previous data in UpdateUserData()
                //Use the initial variables to check for this ^
                if (allValid) {
                    viewModel.UpdateUserData()
                    navigate()
                }
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
    confirm : String = "",
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

    if (!ValidatePassword(password, confirm, activity)) {
        ShowToast(activity, "password")
        invalidCount++
    }

    if (!ValidateAddress(address)) {
        ShowToast(activity, "address")
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
    } else if (type == "password") {
        toastStr = "Password must be at least 6 characters long."
    } else {
        toastStr = "Address is invalid."
    }

    Toast.makeText(
        activity,
        toastStr,
        Toast.LENGTH_LONG,
    ).show()
}

/**
 * @return A List of strings representing each name in order.
 *          firstName index == 0
 *          middelName index == 1
 *          lastName index == 2
 * */
fun GetNames(fullName : String) : List<String> {
    return fullName.split(" ")
}