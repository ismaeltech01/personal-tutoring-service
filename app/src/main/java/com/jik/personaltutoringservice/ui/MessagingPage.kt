package com.jik.personaltutoringservice.ui

//import android.content.IntentSender
//Thew flag for unused
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jik.personaltutoringservice.R

data class Message(val sender: String, val body: String)
val MessageList = listOf(
    Message("Joseph","Seems to be working wonderful so far"),
    Message("Joseph","Seems to be working wonderful so far"),
    Message("Joseph","Seems to be working wonderful so far"),
    Message("Joseph","Seems to be working wonderful so far")
)

@Composable
fun MessagingPage(
    userName: String,
    email: String,
    tutors: Map<String, Map<String, String>>,
    viewModel: MainViewModel
) {
    var receiver by remember { mutableStateOf("") }
    var showChatroom by remember { mutableStateOf(false) }

    if (!showChatroom) {
        LazyColumn() {
            item {
                for (entry in tutors) {
                    val tutorId = entry.key
                    val values = entry.value

                    Button(onClick = {
                        receiver = values["userName"].toString()
                    }) {

                        //Contains User card that on clicked, would take you to the relevant chatroom
                    }
                }
            }
        }
    } else {
        ChatRoom(sender = email, receiver = receiver, viewModel = viewModel)
    }
}

@Composable
fun ChatRoom(
    sender: String,
    receiver: String,
    viewModel: MainViewModel
) {
    var messageIn by remember { mutableStateOf("") }

    OutlinedTextField(
        value = messageIn,
        onValueChange = { messageIn = it },
        label = { Text("Message") },
        keyboardOptions = KeyboardOptions.Default,
        trailingIcon = {

            IconButton(onClick = {
                viewModel.SendMessage(message = messageIn, sender = "", receiver = "")
            }) {
                Icon(Icons.Rounded.Send, "Send Icon")
            }
        }
    )
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
            Text(text = msg.sender,
                style = MaterialTheme.typography.titleSmall)

            Spacer(modifier = Modifier.height(2.dp))

            Text(text = msg.body,
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
    MessagingThread(messageList = MessageList)

}