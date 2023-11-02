package com.jik.personaltutoringservice.ui

import android.graphics.drawable.Icon
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jik.personaltutoringservice.R
import com.jik.personaltutoringservice.ui.theme.PersonalTutoringServiceTheme
import org.w3c.dom.Text

//NOTE: This page is just a test. Can be removed or kept if desired
@Composable
fun CoursesPage(){

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Course",
            fontSize = 40.sp)
        Text("Page",
            fontSize = 40.sp)

        Button(onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),

            ){
            Text("Math")
        }
        Button(onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),

            ){
            Text("Piano")
        }
        Button(onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),

            ){
            Text("French")
        }
        Button(onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),

            ){
            Text("Coding")
        }
        Button(onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),

            ){
            Text("Tennis")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CoursePreview(){
    CoursesPage()
}





