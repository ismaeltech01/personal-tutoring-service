package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun ReportingPage(
    tutors : Map<String, Map<String, String>>
) {
    if (tutors.isNotEmpty()) {
        Column {

            for (pair in tutors) {
                Row {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(pair.value["fullName"].toString())
                        Text("username: ${tutors["userName"]}")
                    }
                    Button(onClick = {}) {
                        Text("Report")
                    }
                }
            }
        }
    } else {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("You have not hired or messaged any tutors yet.")
        }
    }
}

//@Preview
//@Composable
//fun ReportingPagePreview() {
//    ReportingPage()
//}