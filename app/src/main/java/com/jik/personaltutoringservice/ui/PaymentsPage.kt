package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import com.jik.personaltutoringservice.MainActivity

@Composable
fun PaymentsPage(
    cardNum : String,
    expDate : String,
    secCode : String,
    viewModel: MainViewModel,
    activity : Activity,
    navigate : () -> Unit
) {
    var cardNumState by remember { mutableStateOf(if (cardNum == "null") "" else cardNum) }
    var expDateState by remember { mutableStateOf(if (expDate == "null") "" else expDate) }
    var secCodeState by remember { mutableStateOf(if (secCode == "null") "" else secCode) }

    var editCardNum by remember { mutableStateOf(false) }
    var editDateExp by remember { mutableStateOf(false) }
    var editSecCode by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = cardNumState,
            onValueChange = {
                if (it.length <= 16 && !Regex("\\D").containsMatchIn(it))
                    cardNumState = it
            },
            label = { Text("Credit/Debit Card Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(Icons.Rounded.CreditCard, contentDescription = "CardNum Icon") },
            trailingIcon = {
                val icon = if (!editCardNum) Icons.Filled.Edit else Icons.Filled.EditOff
                val description = if (!editCardNum) "Edit On" else "Edit Off"

                IconButton(onClick = { editCardNum = !editCardNum }) {
                    Icon(icon, description)
                }
            },
            enabled = editCardNum
        )

        OutlinedTextField(
            value = expDateState,
            onValueChange = {
                if (it.length <= 4 && !Regex("\\D").containsMatchIn(it))
                    expDateState = it
            },
            label = { Text("Expiration Date") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(Icons.Rounded.CalendarToday, contentDescription = "Expiration Date Icon") },
            trailingIcon = {
                val icon = if (!editDateExp) Icons.Filled.Edit else Icons.Filled.EditOff
                val description = if (!editDateExp) "Edit On" else "Edit Off"

                IconButton(onClick = { editDateExp = !editDateExp }) {
                    Icon(icon, description)
                }
            },
            enabled = editDateExp
        )

        OutlinedTextField(
            value = secCodeState,
            onValueChange = {
                if (it.length <= 3 && !Regex("\\D").containsMatchIn(it))
                    secCodeState = it
            },
            label = { Text("Security Code") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(Icons.Rounded.Security, contentDescription = "Security Code Icon") },
            trailingIcon = {
                val icon = if (!editSecCode) Icons.Filled.Edit else Icons.Filled.EditOff
                val description = if (!editSecCode) "Edit On" else "Edit Off"

                IconButton(onClick = { editSecCode = !editSecCode }) {
                    Icon(icon, description)
                }
            },
            enabled = editSecCode
        )

        Button(onClick = {
            val validNum = cardNumState.length == 16
            val validExp = ValidExpiration(expDateState)
            val validSec = secCodeState.length == 3

            if (validNum && validExp && validSec) {
                editCardNum = false
                editDateExp = false
                editSecCode = false

                viewModel.UpdateCardInfo(
                    if (cardNumState != cardNum) cardNumState else "",
                    if (expDateState != expDate) expDateState else "",
                    if (secCodeState != secCode) secCodeState else ""
                )

                Toast.makeText(
                    activity,
                    "Changes Saved.",
                    Toast.LENGTH_LONG,
                ).show()
                navigate()
            } else {
                val invalidStr = if (!validNum)
                    "Card Number"
                else if (!validExp)
                    "Expiration Date"
                else
                    "Security Code"

                Toast.makeText(
                    activity,
                    "Invalid $invalidStr. Try again.",
                    Toast.LENGTH_LONG,
                ).show()
            }
        }) {
            Text("Save Changes")
        }
    }
}

fun ValidExpiration(expDate : String) : Boolean {
    var month: Int
    var year: Int

    try {
        month = expDate.substring(0, 2).toInt()
        year = expDate.substring(2, 4).toInt()
    } catch (e : Exception) {
        return false;
    }
    return month in 1..12 && year <= 99
}