package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

//******NOTE: This page is just a test. Can be removed or kept if desired
val HomePageModifier = Modifier.fillMaxHeight(.85f).fillMaxWidth()

@Composable
fun HomePage(modifier: Modifier) {
    Column (modifier = modifier) {
        Text(text = "Current Tutors")
        Text(text = "Suggested")
    }
}
