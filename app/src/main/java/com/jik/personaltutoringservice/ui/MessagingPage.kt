package com.jik.personaltutoringservice.ui

//import android.content.IntentSender
//Thew flag for unused
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jik.personaltutoringservice.R

data class Message(
    var messageText: String? = null,
    var receiverID: String? = null,
    var senderID: String? = null,
    var timestamp: java.util.Date? = null)




@Composable
fun MessagingPage(
    userName: String,
    email: String,
    tutors: Map<String, Map<String, String>>,
    viewModel: MainViewModel,
    modifier: Modifier,
    onToSearch: () -> Unit,
    context: Context
) {
    var receiver by remember { mutableStateOf("") }
    var showChatroom by remember { mutableStateOf(false) }

    if (tutors.isNotEmpty()) {
        if (!showChatroom) {
            LazyColumn(
                modifier = modifier.padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    for (entry in tutors) {
                        val tutorId = entry.key
                        val values = entry.value
                        val userName = entry.value["userName"].toString()
                        val imgUrl = entry.value["imageUrl"].toString()
                        val fullName = entry.value["fullName"].toString()

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ImageFrame(imageUrl = imgUrl)
                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Column {
                                    Text(text = ParseFullName(fullName), fontSize = 20.sp)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row {
                                        Icon(Icons.Rounded.Person, "Person icon")
                                        Text(userName)
                                    }
                                }

                                Button(onClick = {
                                    receiver = userName
                                    showChatroom = true
                                }) {
                                    Row {
                                        Icon(Icons.Rounded.MailOutline, "Message Icon")
                                        Text(text = "Message")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            ChatRoom(
                sender = email,
                receiver = receiver,
                viewModel = viewModel,
                context = context
            )
        }
    } else {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "You have not hired any tutors.")
            Button(onClick = onToSearch) {
                Text("Search Tutors")
            }
        }
    }
}

@Composable
fun ChatRoom(
    sender: String,
    receiver: String,
    viewModel: MainViewModel,
    context: Context
) {
    var messageIn by remember { mutableStateOf("") }
    var displayMonitorDialog by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .weight(.7f)
        ) {
//            crashes app when trying to display
            MessagingThread(messageList = viewModel.fetchMessages(viewModel.generateConversationId(sender,receiver)))

        }

        Column (
            modifier = Modifier
                .fillMaxWidth(.95f)
                .weight(.1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = messageIn,
                onValueChange = { messageIn = it },
                label = { Text("Message") },
                keyboardOptions = KeyboardOptions.Default,
                trailingIcon = {

                    IconButton(onClick = {
                        ScanText(
                            text = messageIn,
                            onBannedDetect = {
                                displayMonitorDialog = true
                            },
                            onValidDetect = {
                                viewModel.sendMessage(messageIn, sender, receiver)
                            }
                        )
                    }) {
                        Icon(Icons.Rounded.Send, "Send Icon")
                    }
                }
            )
        }
    }

    if (displayMonitorDialog) {
        MonitoringWarningDialog(onDismiss = {}) { displayMonitorDialog = false }
    }
}
@Composable
        /*
        Still needs to limit preview message, Gonna see if a toggle is a better option

        * */
fun MessagingCard(msg: Message) {
    Row {
        Image(painter = painterResource(id = R.drawable.profileimage),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Column {
            Text(text = msg.senderID.toString(),
                style = MaterialTheme.typography.titleSmall)

            Spacer(modifier = Modifier.height(2.dp))

            Text(text = msg.messageText.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(all = 3.dp))
        }

    }
}

@Composable
//Makes a list of messages, we need to ensure data entry follows this format
//want to add image handling, we can use this same option for the conversation as well
fun MessagingThread(messageList : List<Message>){
    LazyColumn{
        items(messageList){
                message -> MessagingCard(message)
        }
    }

}

@Preview(
    showBackground = true
)
@Composable
fun PreviewMC() {

}