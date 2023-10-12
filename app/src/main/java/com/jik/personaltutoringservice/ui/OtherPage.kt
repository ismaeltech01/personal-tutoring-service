package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OtherPage() {
    Column(modifier = Modifier.fillMaxSize()) {
        
    }
}

@Composable
fun PageButton(text : String) {
    Button(onClick = { /*TODO*/ }) {
        Text(text = text)
    }
}