package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//Page that contains all of the links for the "Other" option in the navbar
@Composable
fun OtherPage(onCalendarClick : () -> Unit, onCoursesClick : () -> Unit, onPaymentsClick : () -> Unit, onAdClick : () -> Unit, onSettingsClick : () -> Unit, onReportClick : () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        PageButton("Calendar", onCalendarClick)
        PageButton("Courses", onCoursesClick)
        PageButton("Payments", onPaymentsClick)
        PageButton("Advertisement", onAdClick)
        PageButton("Settings", onSettingsClick)
        PageButton("Reporting", onReportClick)
    }
}

//Rectangular buttons used for Other page (can be used for other things as well)
@Composable
fun PageButton(text : String, onBtnClick : () -> Unit) {
    Button(onClick = onBtnClick) {
        Text(text = text)
    }
}