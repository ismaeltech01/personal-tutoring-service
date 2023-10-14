package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

//Page that contains all of the links for the "Other" option in the navbar
@Composable
fun OtherPage() {
    Column(modifier = Modifier.fillMaxSize()) {
        PageButton("Calendar")
        PageButton("Courses")
        PageButton("Payments")
        PageButton("Advertisement")
        PageButton("Settings")
        PageButton("Reporting")
    }
}

//Rectangular buttons used for Other page (can be used for other things as well)
@Composable
fun PageButton(text : String) {
    Button(onClick = { /*TODO*/ }) {
        Text(text = text)
    }
}