package com.jik.personaltutoringservice.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jik.personaltutoringservice.MainActivity
import com.jik.personaltutoringservice.R
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.regex.Pattern

@Composable
fun ReportingPage(
    tutors : Map<String, Map<String, String>>,
    clients : Map<String, Map<String, String>>,
    isTutor : Boolean,
    viewModel: MainViewModel
) {
    var confirmState by remember { mutableStateOf(false) }
    var reportUIDState by remember { mutableStateOf("") }
    var reportPicState by remember { mutableStateOf("") }
    var reportFullNameState by remember { mutableStateOf("") }
    var reportUserNameState by remember { mutableStateOf("") }
    var reportEmailState by remember { mutableStateOf("") }

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
                    val email = pair.value["email"].toString()
                    val imageUrl = pair.value["imageUrl"].toString()

                    ReportUserCard(
                        picture = imageUrl,
                        fullName = fullName,
                        userName = userName,
                        enableReport = true,
                        onReportClick = {
                            confirmState = true
                            reportUIDState = pair.key
                            reportFullNameState = fullName
                            reportUserNameState = userName
                            reportEmailState = email
                            reportPicState = imageUrl
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
                        val email = pair.value["email"].toString()
                        val uId = pair.value["uId"].toString()
                        val imageUrl = pair.value["imageUrl"].toString()

                        ReportUserCard(
                            picture = imageUrl,
                            fullName = fullName,
                            userName = userName,
                            enableReport = true,
                            onReportClick = {
                                confirmState = true
                                reportUIDState = pair.key
                                reportFullNameState = fullName
                                reportUserNameState = userName
                                reportEmailState = email
                                reportPicState = imageUrl
                            }
                        )
                    }
                }
            }
        }
    } else {
        ReportConfirmPage(
            uid = reportUIDState,
            imageUrl = reportPicState,
            userName = reportUserNameState,
            fullName = reportFullNameState,
            email = reportEmailState,
            onBackClick = { confirmState = false },
            viewModel = viewModel
        )
    }
}

@Composable
fun ReportUserCard(
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
            Row (
                horizontalArrangement = if (enableReport) Arrangement.Start else Arrangement.Center,
                verticalAlignment = CenterVertically,
                modifier = Modifier.weight(if (enableReport) 3 / 4f else 1f)
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (picture == "") {
                        Image(Icons.Rounded.AccountCircle, "$fullName's profile picture")
                    } else {
                        ImageFrame(imageUrl = picture)
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
                modifier = Modifier.weight(1 / 4f)
            ) {
                Button(onClick = onReportClick) {
                    Text("Report")
                }
            }
        }
    }
}

@Composable
fun ReportConfirmPage(
    imageUrl: String,
    uid : String,
    fullName : String,
    userName : String,
    email: String,
    onBackClick : () -> Unit,
    viewModel: MainViewModel
) {
    var textState by remember { mutableStateOf("") }
    var reportState by remember { mutableStateOf(false) }

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

        Spacer(modifier = Modifier.height(10.dp))

        ReportUserCard(
            picture = imageUrl,
            fullName = fullName,
            userName = userName,
            enableReport = false
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            minLines = 4,
            placeholder = { Text(text = "Reasons for reporting...")}
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier
                .background(if (reportState) Color.Red else Color.Transparent)
                .clip(CircleShape),
            onClick = {
                viewModel.ReportUser(
                    fullName = fullName,
                    userName = userName,
                    email = email,
                    reason = textState,
                    onSuccess = { reportState = true }
                )
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
    ReportConfirmPage(
        imageUrl = "",
        uid = "",
        fullName = "Patrick Spam",
        userName = "spamspam",
        email = "email@email.com",
        viewModel = MainViewModel(),
        onBackClick = {}
    )
}

/**
 * Scans text to see if there is any banned words in it.
 *
 * @return true if the input text is valid (no banned words detected), false otherwise
 * */
fun ScanText(
    text : String,
    onBannedDetect: () -> Unit,
    onValidDetect: () -> Unit
) {
    val lowerCased = text.lowercase()
    val bannedWords = Data().bannedWords

    for (i in bannedWords.indices) {
        val bannedWord = bannedWords[i]
        var wordList = lowerCased.split(" ").toMutableList()

        for (i in wordList.indices) {
            val word = wordList[i]
            wordList[i] = word
                .replace("!", "")
                .replace("?", "")
                .replace(".", "")
        }

        if (wordList.contains(bannedWord)) {
            Log.w(TAG, "Banned Word detected: $bannedWord")
            onBannedDetect()
            return
        }
    }

    onValidDetect()
}
