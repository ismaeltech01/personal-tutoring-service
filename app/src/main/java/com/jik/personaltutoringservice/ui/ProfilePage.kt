package com.jik.personaltutoringservice.ui

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ContentPasteGo
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@Composable
fun ProfilePage(
    modifier : Modifier,
    onLoginClick : () -> Unit,
    onRegisterClick : () -> Unit,
    onTutorClick : () -> Unit,
    loggedIn : Boolean,
    fullName : String,
    userName : String,
    email : String,
    phone : String,
    address : String,
    isTutor : Boolean,
    viewMode: Boolean,
    searched: Boolean,
    onHire: () -> Unit = {},
    imageUrl : String,
    uId: String = "",
    viewModel: MainViewModel
) {
    var clickedImage by remember { mutableStateOf(false) }
    var urlInput by remember { mutableStateOf(imageUrl) }

    if (!clickedImage) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            if (loggedIn) {
                ImageFrame(imageUrl = imageUrl, clickable = true, onClick = { clickedImage = true }, size = 128.dp)
            } else {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = "Guest picture",
                    modifier = Modifier.size(100.dp)
                )
            }

            NameDisplay(fullName = fullName)
            Spacer(modifier = Modifier.height(3.dp))

            if (!loggedIn) {
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(.5f)
                ) {
                    Text(text = "Login", color = Color.White)
                }

                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth(.5f)
                ) {
                    Text(text = "Register", color = Color.White)
                }
            } else {
                Row {
                    Icon(Icons.Rounded.Person, "Username Icon")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = userName)
                }

                Row {
                    Icon(Icons.Rounded.Email, "Email Icon")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(email)
                }

                Row {
                    Icon(Icons.Rounded.Phone, "Phone Icon")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(phone)
                }

                Row {
                    Icon(Icons.Rounded.Home, "Home Icon")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(address)
                }

                Spacer(modifier = Modifier.height(5.dp))

                if (!isTutor) {
                    Button(
                        onClick = onTutorClick
                    ) {
                        Text("Become a Tutor")
                    }
                } else if (!searched) {
                    Spacer(modifier = Modifier.height(5.dp))

                    Row {
                        Icon(Icons.Rounded.School, "School icon", tint = Color.Green)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("You are a Tutor!")
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    
                    Button(onClick = { viewModel.StopBeingTutor() }) {
                        Text(text = "Stop being a Tutor")
                    }
                }

                if (searched) {
                    Button(onClick = {
                        viewModel.HireTutor(
                            hireEmail = email,
                            tutorId = uId
                        )
                        onHire()
                    }
                    ) {
                        Text("Hire Tutor")
                    }
                }
            }
        }
    } else {
        ExitBar { clickedImage = false }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            Text(text = "Change Profile Image", fontSize = 15.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            ImageFrame(imageUrl = urlInput)

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = urlInput,
                onValueChange = { urlInput = it },
                label = { Text("Image Url") },
                keyboardOptions = KeyboardOptions.Default,
                trailingIcon = {
                    val clipboard = LocalClipboardManager.current

                    IconButton(onClick = { urlInput = clipboard.getText()?.text.toString() }) {
                        Icon(Icons.Rounded.ContentPasteGo, "Remove & Paste icon")
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                viewModel.UpdateUserData(imageUrl = urlInput)
                clickedImage = false
            }) {
                Text(text = "Save Changes")
            }
        }
    }
}

/**
 * @param To modify the size of the ImageFrame,
 * DO NOT modify the hardcoded value (size : Int = 100.dp) in the function.
 * Pass in the size you need for size as a parameter.
 * */
@Composable
fun ImageFrame(
    imageUrl: String,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
    size: Dp = 100.dp
) {
    val modifier = if (clickable) Modifier.clickable(onClick = onClick) else Modifier

    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = "Circle Image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(5.dp, Color.Gray, CircleShape)
    )
}

@Composable
fun NameDisplay(fullName: String) {
    val name = ParseFullName(fullName)

    Text("Hello, $name!", fontSize = 20.sp)
}

/**
 * Expects a fullName string and returns the appropriate string to display in the app.
 * */
fun ParseFullName(fullName : String) : String {
    var name = ""

    try {
        val names = fullName.split(" ")
        name = if (names[1].contains("*")) "${names[0]} ${names[2]}" else fullName
    } catch (e : Exception) {
        Log.e(TAG, "NameDisplay:failure -> fullName is likely too short", e)
    }
    return name
}

@Preview
@Composable
fun ImagePreview() {

}