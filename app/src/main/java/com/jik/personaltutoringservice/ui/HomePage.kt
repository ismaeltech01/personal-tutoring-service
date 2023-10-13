//NOTE: Test file. Anyone can delete this file if needed. Or keep if yall like it
package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

val HomePageModifier = Modifier.fillMaxHeight(.85f).fillMaxWidth()

@Composable
fun HomePage(modifier: Modifier) {
    Column (modifier = modifier) {
        Text(text = "Current Tutors")
        Text(text = "Suggested")
    }
}
