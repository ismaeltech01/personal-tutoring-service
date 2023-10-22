package com.jik.personaltutoringservice.ui

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
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PaymentsPage(
    cardNum : String,
    expDate : String,
    secCode : String
) {
    var cardNumState by remember { mutableStateOf(if (cardNum == "null") "" else cardNum) }
    var expState by remember { mutableStateOf(if (expDate == "null") "" else expDate) }
    var secCodeState by remember { mutableStateOf(if (secCode == "null") "" else secCode) }

    var editCardNum by remember { mutableStateOf(false) }
    var editExp by remember { mutableStateOf(false) }
    var editSecCode by remember { mutableStateOf(false) }

    Column {
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
            value = expState,
            onValueChange = {
                if (it.length <= 4 && !Regex("\\D").containsMatchIn(it))
                    expState = it
            },
            label = { Text("Expiration Date") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(Icons.Rounded.CalendarToday, contentDescription = "Expiration Date Icon") },
            trailingIcon = {
                val icon = if (!editExp) Icons.Filled.Edit else Icons.Filled.EditOff
                val description = if (!editExp) "Edit On" else "Edit Off"

                IconButton(onClick = { editExp = !editExp }) {
                    Icon(icon, description)
                }
            },
            enabled = editExp
        )

        OutlinedTextField(
            value = secCodeState,
            onValueChange = { secCodeState = it },
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

        }) {
            Text("Save Changes")
        }

    }
}

fun ValidExpiration(expDate : String) {


}