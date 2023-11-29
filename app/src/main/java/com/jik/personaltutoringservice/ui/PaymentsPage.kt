package com.jik.personaltutoringservice.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jik.personaltutoringservice.MainActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

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

@Composable
fun PayTutorPage(
    rate: BigDecimal,
    invalidCard: Boolean,
    userEmail: String,
    tutorEmail: String,
    onExit: () -> Unit,
    onEditCard: () -> Unit,
    viewModel: MainViewModel,
    activity: Activity
) {
    var minutes by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var payComplete by remember { mutableStateOf(false) }

    ExitBar { onExit() }
    if (!payComplete) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Text(text = "Rate: ")
                Text(text = "$ ${rate.toString()}", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = minutes,
                onValueChange = { minutes = it },
                label = { Text("Minutes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = {
                    Icon(
                        Icons.Rounded.AccessTime,
                        contentDescription = "Minutes icon"
                    )
                }
            )

            Button(onClick = {
                if (minutes.contains(' ') || minutes == "" || minutes.toInt() <= 0) {
                    Toast.makeText(
                        activity,
                        "Invalid minutes input. Make sure it is not 0, empty, or has spaces.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    showDialog = true
                }
            }) {
                Text(text = "Pay")
            }
        }
    } else {
        PayCompletePage(
            rate = rate,
            minutes = minutes.toInt(),
            onExit = onExit
        )
    }

    if (showDialog) {
        if (invalidCard) {
            InvalidCardDialog(onDismiss = {}) {
                onEditCard()
            }
        } else {
            ConfirmTransactionDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    Log.d(TAG, "Tutor Email in Confirm: $tutorEmail")
                    val res = viewModel.InitTransaction(
                        payerEmail = userEmail,
                        payeeEmail = tutorEmail,
                        rate = rate,
                        hours = BigDecimal(minutes).setScale(2).divide(BigDecimal(60), 2, RoundingMode.HALF_UP)
                        )
                    if (res == 0) {
                        showDialog = false
                        payComplete = true
                    }
                },
                minutes = minutes.toInt(),
                rate = rate
            )
        }
    }
}

@Composable
fun PayCompletePage(
    rate: BigDecimal,
    minutes: Int,
    onExit: () -> Unit
) {
    val fontSize = 20.sp
    val subTxtFontSize = 15.sp
    val rawHours = BigDecimal(minutes).divide(BigDecimal(60), 2, RoundingMode.HALF_UP)
    val rawTotal = rate.multiply(rawHours).round(MathContext(2))
    val hours = rawHours.setScale(2).toString()
    val total = rawTotal.setScale(2).toString()
    val commission = rawTotal.multiply(BigDecimal(.20)).setScale(2, RoundingMode.HALF_UP)
    val tutorProfit = rawTotal.multiply(BigDecimal(.80)).setScale(2, RoundingMode.HALF_UP)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Rounded.CheckCircle,
            contentDescription = "Complete icon",
            modifier = Modifier
                .size(60.dp)
                .padding(top = 10.dp),
            tint = Color.Green
        )

        Text(
            text = "Transaction Complete",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tutor Rate (Hourly)",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                fontSize = fontSize
            )

            Text(
                text = "$ $rate",
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                fontSize = fontSize
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Hours",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                fontSize = fontSize
            )

            Text(
                text = "x $hours",
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                fontSize = fontSize
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Text(
            text = "Total",
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(horizontal = 10.dp),
            fontSize = fontSize
        )

        Text(
            text = "$ $total",
            textAlign = TextAlign.Right,
            modifier = Modifier
                .padding(horizontal = 10.dp),
            fontSize = fontSize
        )
    }

    //Tutor Profit
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Tutor (80%)",
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(horizontal = 10.dp),
            fontSize = subTxtFontSize
        )

        Text(
            text = "$ $tutorProfit",
            textAlign = TextAlign.Right,
            modifier = Modifier
                .padding(horizontal = 10.dp),
            fontSize = subTxtFontSize
        )
    }

    //Commission
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Commission (20%)",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                fontSize = subTxtFontSize
            )

            Text(
                text = "$ $commission",
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                fontSize = subTxtFontSize
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = onExit) {
            Text(text = "Awesome!")
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PayCompletePagePreview() {
    PayCompletePage(rate = BigDecimal(10), minutes = 100, onExit = {})
}