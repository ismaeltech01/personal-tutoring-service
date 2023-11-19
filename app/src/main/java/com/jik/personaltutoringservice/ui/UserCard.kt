package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PriceChange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserCard(
    fullName: String,
    userName: String,
    imageUrl: String,
    location: String = "",
    rate: String,
    isHome: Boolean = false,
    onClick: () -> Unit = {},
    onFire: () -> Unit = {},
    onPay: () -> Unit = {}
) {
    val modifier = if (onClick != {}) Modifier.clickable(onClick = onClick) else Modifier

    Card (
        modifier = modifier.fillMaxWidth(if (isHome) .95f else .85f)
    ) {
        Row {
            ImageFrame(imageUrl = imageUrl)

            Column {

                Column {
                    Text(text = ParseFullName(fullName))

                    Row {
                        Row {
                            Icon(Icons.Rounded.Person, "Username icon")
                            Text(text = userName)
                        }

                        if (location != "") {
                            Row {
                                Icon(Icons.Rounded.LocationOn, "Location icon")
                                Text(text = location)
                            }
                        }
                    }
                }

                if (rate != "") {
                    Row {
                        Icon(Icons.Rounded.PriceChange, "Money icon")
                        Text(rate)
                    }
                }

                if (isHome) {
                    Row {
                        Button(onClick = onFire) {
                            Text(text = "Fire")
                        }
                        
                        Spacer(modifier = Modifier.width(10.dp))

                        Button(onClick = onPay) {
                            Text(text = "Pay")
                        }
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun UserCardPreview() {
    UserCard(fullName = "Ismael * Tovar", userName = "ismaeltovar", imageUrl = "https://avatars.githubusercontent.com/u/73203513?v=4", rate = "20.00", location = "UTA", onFire = {}, onPay = {}, onClick = {})
}
