package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ReportingPage(
    tutors : Map<String, Map<String, String>>
) {
    var confirmState by remember {
        mutableStateOf(false)
    }

    var reportUIDState by remember {
        mutableStateOf("")
    }

    var reportPicState by remember {
        mutableStateOf("")
    }

    var reportFullNameState by remember {
        mutableStateOf("")
    }

    var reportUserNameState by remember {
        mutableStateOf("")
    }

    if (tutors.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("You have not hired or messaged any tutors yet.")
        }
    } else if (!confirmState) {
        Column {
            for (pair in tutors) {
                val fullName = ParseFullName(pair.value["fullName"].toString())
                val userName = ParseFullName(pair.value["userName"].toString())

                UserCard(
                    fullName = fullName,
                    userName = userName,
                    onReportClick = {
                        confirmState = true
                        reportUIDState = pair.key
                        reportFullNameState = fullName
                        reportUserNameState = userName
                    }
                )
            }
        }
    } else {
        ReportConfirmPage (
            uid = reportUIDState,
            picture = reportPicState,
            userName = reportUserNameState,
            fullName = reportFullNameState
        )
    }
}

@Composable
fun UserCard(
    picture : String = "",
    fullName : String,
    userName : String,
    onReportClick : () -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (picture == "") {
            Image(Icons.Rounded.AccountCircle, "$fullName's profile picture")
        } else {
            //TODO
        }

        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(fullName)
            Text("username: $userName")
        }

        Button(onClick = onReportClick) {
            Text("Report")
        }
    }
}

@Composable
fun ReportConfirmPage(
    picture: String,
    uid : String,
    fullName : String,
    userName : String
) {
    var textState by remember {
        mutableStateOf("")
    }

    var reportState by remember {
        mutableStateOf(false)
    }

    Column {
        Text("The following user will be reported: ")
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            minLines = 4,
            placeholder = { Text(text = "Reasons for reporting...")}
        )
        Button(
            modifier = Modifier.background(if (reportState) Color.Red else Color.Transparent),
            onClick = {
                ReportUser(uid)
                reportState = true
            },
            enabled = !reportState
        ) {
            Text(if (!reportState) "Report" else "Reported")
        }
    }
}
fun ReportUser(
    uid : String
) {
    //TODO: Maybe make simple react console for reporting
}

//@Preview
//@Composable
//fun ReportingPagePreview() {
//    ReportingPage()
//}