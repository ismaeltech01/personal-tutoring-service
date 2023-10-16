package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI

//Page that contains all of the links for the "Other" option in the navbar
@Composable
fun OtherPage(
    onCalendarClick : () -> Unit,
    onCoursesClick : () -> Unit,
    onPaymentsClick : () -> Unit,
    onAdClick : () -> Unit,
    onSettingsClick : () -> Unit,
    onReportClick : () -> Unit,
    onSigninClick : () -> Unit,
    onSignOutClick : () -> Unit,
    userSignedIn : Boolean
) {
    val spacerModifier = Modifier.height(3.dp)

    if (userSignedIn)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = spacerModifier.weight(.05f))
            PageButton("Calendar", onCalendarClick, modifier = Modifier.weight(.1f))
            Spacer(modifier = spacerModifier.weight(.02f))
            PageButton("Courses", onCoursesClick, modifier = Modifier.weight(.1f))
            Spacer(modifier = spacerModifier.weight(.02f))
            PageButton("Payments", onPaymentsClick, modifier = Modifier.weight(.1f))
            Spacer(modifier = spacerModifier.weight(.02f))
            PageButton("Advertisement", onAdClick, modifier = Modifier.weight(.1f))
            Spacer(modifier = spacerModifier.weight(.02f))
            PageButton("Settings", onSettingsClick, modifier = Modifier.weight(.1f))
            Spacer(modifier = spacerModifier.weight(.02f))
            PageButton("Reporting", onReportClick, modifier = Modifier.weight(.1f))
            Spacer(modifier = spacerModifier.weight(.02f))
            PageButton("Sign Out", onSignOutClick, modifier = Modifier.weight(.1f))
            Spacer(modifier = spacerModifier.weight(.05f))
        }
    else 
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text("Please Signin to view more options")
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = onSigninClick
            ) {
                Text("Signin")
            }
        }
}

//Rectangular buttons used for Other page (can be used for other things as well)
@Composable
fun PageButton(
    text : String,
    onBtnClick : () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = onBtnClick,
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        modifier = modifier
            .border(2.dp, Color.Black, shape = RectangleShape)
            .fillMaxWidth(.9f)
            .padding(0.dp)
    ) {
        Text(text = text,
            color = Color.Black)
    }
}

@Preview
@Composable
fun OtherPagePreview() {
    /*
    OtherPage(
        onCalendarClick = { /*TODO*/ },
        onCoursesClick = { /*TODO*/ },
        onPaymentsClick = { /*TODO*/ },
        onAdClick = { /*TODO*/ },
        onSettingsClick = { /*TODO*/ },
        onReportClick = { })
     */
}

@Preview
@Composable
fun PageButtonPreview() {
    PageButton(text = "HELLO", {}, Modifier)
}