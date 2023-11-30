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
import androidx.compose.ui.unit.sp

@Composable
fun AdsPage(
    viewModel: MainViewModel,
    onExit : () -> Unit,
    uID : String
) {
    var msg by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var course by remember { mutableStateOf(("")) }

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

                Row(verticalAlignment = CenterVertically) {
                    RadioButton(
                        selected = course == "Math",
                        onClick = { course = "Math" }
                    )
                    Text("Math")

                    RadioButton(
                        selected = course == "Piano",
                        onClick = { course = "Piano" }
                    )
                    Text("Piano")

                    RadioButton(
                        selected = course == "French",
                        onClick = { course = "French" }
                    )
                    Text("French")

                }
//                Text("Hello, ${msg.value}!")
            }

            Column(horizontalAlignment = CenterHorizontally) {

                Row(verticalAlignment = CenterVertically) {


                    RadioButton(
                        selected = course == "Coding",
                        onClick = { course = "Coding" }
                    )
                    Text("Coding")

                    RadioButton(
                        selected = course == "Tennis",
                        onClick = { course = "Tennis" }
                    )
                    Text("Tennis")
                }
            }




            Button(onClick = {
                viewModel.createAnAdd(msg, selectedOption.toString(), uID, course.toString());
                onExit();
            }
            ){
                Text("Submit",fontSize = 30.sp)
            }
        }
    }
