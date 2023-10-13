//This is just default file for reference
package com.jik.personaltutoringservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.jik.personaltutoringservice.ui.HomePage
import com.jik.personaltutoringservice.ui.Navbar
import com.jik.personaltutoringservice.ui.theme.PersonalTutoringServiceTheme

//val MainModifier = Modifier.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalTutoringServiceTheme {
                Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Titlebar(modifier = Modifier.background(color = Color.Blue).fillMaxWidth().weight(.1f))
                        HomePage(modifier = Modifier.background(color = Color.White).fillMaxWidth().weight(.8f))
                        Navbar(modifier = Modifier.background(color = Color.Blue).fillMaxWidth().weight(.1f))
                }
            }
        }
    }
}

@Composable
fun Titlebar(modifier : Modifier) {
    Text(text = "Personal Tutoring Service", modifier = modifier)
}