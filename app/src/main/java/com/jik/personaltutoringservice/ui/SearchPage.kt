package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun SearchPage(
    viewModel: MainViewModel
) {
    val dummyVal : Map<String, Map<String, String>> = mapOf()

    var searchIn by remember { mutableStateOf("") }
    //NOTE: searchedTutors might cause issues since a map might not be observable
    var searchedTutors by remember { mutableStateOf(dummyVal) }

    Column {
        OutlinedTextField(
            value = searchIn,
            onValueChange = { searchIn = it },
            label = { Text("Search") },
            keyboardOptions = KeyboardOptions.Default,
            trailingIcon = {

                IconButton(onClick = {
                    searchedTutors = viewModel.SearchTutors()
                }) {
                    Icon(Icons.Rounded.Search, "Search Icon")
                }
            }
        )
    }
}
