package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ReportingPage(
    tutors : Map<String, Map<String, String>>,
    clients : Map<String, Map<String, String>>,
    isTutor : Boolean
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
        LazyColumn {
            item {
                Row (
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Text("Tutors")
                }

                for (pair in tutors) {
                    val fullName = ParseFullName(pair.value["fullName"].toString())
                    val userName = pair.value["userName"].toString()

                    UserCard(
                        fullName = fullName,
                        userName = userName,
                        enableReport = true,
                        onReportClick = {
                            confirmState = true
                            reportUIDState = pair.key
                            reportFullNameState = fullName
                            reportUserNameState = userName
                        }
                    )
                }

                if (isTutor) {
                    Row (
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text("Students")
                    }

                    for (pair in clients) {
                        val fullName = ParseFullName(pair.value["fullName"].toString())
                        val userName = pair.value["userName"].toString()

                        UserCard(
                            fullName = fullName,
                            userName = userName,
                            enableReport = true,
                            onReportClick = {
                                confirmState = true
                                reportUIDState = pair.key
                                reportFullNameState = fullName
                                reportUserNameState = userName
                            }
                        )
                    }
                }
            }
        }
    } else {
        ReportConfirmPage(
            uid = reportUIDState,
            picture = reportPicState,
            userName = reportUserNameState,
            fullName = reportFullNameState,
            onBackClick = { confirmState = false }
        )
    }
}

@Composable
fun UserCard(
    picture : String = "",
    fullName : String,
    userName : String,
    enableReport : Boolean,
    onReportClick : () -> Unit = {}
) {
    Row (
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
//        if (enableReport) {
            Row (
                horizontalArrangement = if (enableReport) Arrangement.Start else Arrangement.Center,
                verticalAlignment = CenterVertically,
                modifier = Modifier.weight(if (enableReport) 2 / 3f else 1f)
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (picture == "") {
                        Image(Icons.Rounded.AccountCircle, "$fullName's profile picture")
                    } else {
                        //TODO
                    }
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(fullName)
                    Row {
                        Image(Icons.Rounded.Person, "UserName icon")
                        Text(userName)
                    }
                }
            }

        if (enableReport) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = CenterVertically,
                modifier = Modifier.weight(1 / 3f)
            ) {
                Button(onClick = onReportClick) {
                    Text("Report")
                }
            }
        }
//        } else {
//            Column (
//                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.Center
//            ) {
//                if (picture == "") {
//                    Image(Icons.Rounded.AccountCircle, "$fullName's profile picture")
//                } else {
//                    //TODO
//                }
//
//                Text(fullName)
//                Row {
//                    Image(Icons.Rounded.Person, "UserName icon")
//                    Text(userName)
//                }
//            }
    }
}

@Composable
fun ReportConfirmPage(
    picture: String,
    uid : String,
    fullName : String,
    userName : String,
    onBackClick : () -> Unit
) {
    var textState by remember {
        mutableStateOf("")
    }

    var reportState by remember {
        mutableStateOf(false)
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row (
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(Icons.Rounded.ArrowBack, "Back arrow", modifier = Modifier
                .clickable(onClick = onBackClick)
                .size(60.dp)
                .padding(15.dp))
        }

        Text("The following user will be reported: ")

        UserCard(
            fullName = fullName,
            userName = userName,
            enableReport = false
        )

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

@Preview
@Composable
fun ReportConfirmPagePreview() {
    ReportConfirmPage(picture = "", uid = "", fullName = "Patrick Spam", userName = "spamspam" ) {

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