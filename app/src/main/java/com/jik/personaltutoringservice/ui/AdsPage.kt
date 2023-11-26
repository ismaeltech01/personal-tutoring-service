package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun AdsPage(
    viewModel: MainViewModel,
    onExit : () -> Unit
) {
    var msg by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }

        Column{

            Column(horizontalAlignment = CenterHorizontally) {
                Text("Create an Advertisement")
            }


            Row(verticalAlignment = CenterVertically) {
                RadioButton(
                    selected = selectedOption == "Banner",
                    onClick = { selectedOption = "Banner" }
                )
                Text("Banner")

                RadioButton(
                    selected = selectedOption == "Email",
                    onClick = { selectedOption = "Email" }
                )
                Text("Email")
            }

            Column {
                TextField(
                    value = msg,
                    onValueChange = { newText -> msg= newText },
                    label = { Text("What do you want your advertisement to say") }
                )
//                Text("Hello, ${msg.value}!")
            }




            Button(onClick = {
                viewModel.createAnAdd(msg, selectedOption.toString());
                onExit();
            }
            ){
                Text("Submit",fontSize = 30.sp)
            }
        }
// TODO: Create a function to add the data too the database
//    viewModel.createAnAdd(msg.toString(), selectedOption.toString())
    }


@Preview(
    showBackground = true,
)
@Composable
fun testcase(){
//    AdsPage(viewModel: MainViewModel)
}