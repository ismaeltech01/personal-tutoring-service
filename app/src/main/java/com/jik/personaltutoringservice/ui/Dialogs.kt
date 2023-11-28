package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.Currency

@Composable
fun InvalidCardDialog(
    onDismiss : () -> Unit,
    onConfirm : () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(vertical = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Rounded.WarningAmber,
                    contentDescription = "Card invalid warning icon",
                    modifier = Modifier
                        .size(80.dp),
                    tint = Color.Yellow
                )

                Text(
                    text = "Invalid Credit Card on file. Please enter a valid credit card to continue.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(20.dp)
                )

                Button(onClick = onConfirm) {
                    Text("Edit Card Info")
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun InvalidCardDialogPreview() {
    InvalidCardDialog(onDismiss = {}) {}
}

@Composable
fun ConfirmTransactionDialog(
    onDismiss : () -> Unit,
    onConfirm : () -> Unit,
    minutes : Int,
    rate : BigDecimal
) {
    val rawHours = BigDecimal(minutes).divide(BigDecimal(60), 2, RoundingMode.HALF_UP)
    val rawTotal = rate.multiply(rawHours, MathContext(2))
    val hours = rawHours.setScale(2).toString()
    val total = rawTotal.setScale(2).toString()
    val fontSize = 10.sp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(vertical = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.ReceiptLong,
                    contentDescription = "Confirm transaction icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 10.dp),
                    tint = Color.Yellow
                )

                Text(
                    text = "Confirm Transaction",
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
//                        fontSize = 10.sp
                    )

                    Text(
                        text = "$ $rate",
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
//                        fontSize = 10.sp
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
//                        fontSize = 10.sp
                    )

                    Text(
                        text = "x $hours",
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
//                        fontSize = 10.sp
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
//                        fontSize = 10.sp
                    )

                    Text(
                        text = "$ $total",
                        textAlign = TextAlign.Right,
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
//                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(onClick = onConfirm) {
                    Text("Submit")
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ConfirmTransactionDialogPreview() {
    val total = BigDecimal("20.10")
    ConfirmTransactionDialog(
        onDismiss = {},
        onConfirm = {},
        minutes = 60,
        rate = BigDecimal("20.10")
    )
}
@Composable
fun MonitoringWarningDialog(
    onDismiss : () -> Unit,
    onConfirm : () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(vertical = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.WarningAmber,
                    contentDescription = "Banned word warning icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 10.dp),
                    tint = Color.Red
                )

                Text(
                    text = "Banned Language Detected",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Our Detection Systems have detected unethical, illegal, or banned language in your text input. If you think this is a mistake, contact the development team.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(15.dp)
                )

                Button(onClick = onConfirm) {
                    Text("Ok")
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun MonitoringWarningPreview() {
    val total = BigDecimal("20.10")
    MonitoringWarningDialog(
        onDismiss = {},
        onConfirm = {}
    )
}

@Composable
fun ResetPasswordDialog(
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(vertical = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Rounded.WarningAmber,
                    contentDescription = "Too many login icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 10.dp),
                    tint = Color.Red
                )

                Text(
                    text = "Too Many Failed Attempts",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Please reset your password.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(15.dp)
                )

                Button(onClick = onConfirm) {
                    Text("Ok")
                }
            }
        }
    }
}

@Composable
fun FireDialog(
    fullName: String,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(vertical = 15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Rounded.WarningAmber,
                    contentDescription = "Too many login icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 10.dp),
                    tint = Color.Yellow
                )

                Text(
                    text = "Are you sure you want to fire $fullName?",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(15.dp)
                )

                Button(onClick = onConfirm) {
                    Text("Ok")
                }
            }
        }
    }
}
