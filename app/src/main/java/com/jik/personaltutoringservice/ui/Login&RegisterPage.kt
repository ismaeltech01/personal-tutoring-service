package com.jik.personaltutoringservice.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import java.io.File
import java.time.Duration

@Composable
fun LoginPage(
    viewModel: MainViewModel,
    activity: Activity,
    navigateSuccess: () -> Unit,
    onResetPassword: () -> Unit,
    onExit : () -> Unit,
    onSecQExit: () -> Unit
) {
    var emailState by remember {
        mutableStateOf("")
    }

    var passwdState by remember {
        mutableStateOf("")
    }

    var passwordReset by remember { mutableStateOf(false) }

    var loginAttempts by remember { mutableIntStateOf(0) }

    ExitBar { onExit() }

    if (!passwordReset && loginAttempts < 3) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        )
        {
            //Spacer()

            Image(
                Icons.Rounded.AccountBox,
                contentDescription = "Login icon",
                modifier = Modifier.size(100.dp)
            )

            EmailField(
                value = emailState,
                onValueChange = { emailState = it }
            )

            PasswordField(
                value = passwdState,
                onValueChange = { passwdState = it },
                isConfirm = false
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row {
                Button(onClick = onExit) {
                    Text("Cancel")
                }
                
                Spacer(modifier = Modifier.width(20.dp))

                Button(onClick = {
                    if (!isValidEmail(emailState)) {
                        Toast.makeText(
                            activity,
                            "Invalid email. Try again.",
                            Toast.LENGTH_LONG,
                        ).show()
                    } else if (passwdState.length < 6) {
                        Toast.makeText(
                            activity,
                            "Password must be at least 6 characters long.",
                            Toast.LENGTH_LONG,
                        ).show()
                    } else {
                        viewModel.LogIn(
                            email = emailState,
                            password = passwdState,
                            activity = activity,
                            onSuccess = { navigateSuccess() },
                            onFailure = { loginAttempts += 1 })
                    }
                })
                {
                    Text("Login")
                }
            }

            Text(
                "Forgot Password?",
                modifier = Modifier
                    .clickable(onClick = {passwordReset = true})
            )
        }
    } else if (!passwordReset) {
        ResetPasswordDialog { passwordReset = true }
    } else {
        /** If user clicks the forgot password option */
        SeqQPassResetPage(
            viewModel = viewModel,
            activity = activity,
            onResetPassword = onResetPassword,
            onCancel = onExit,
            onExit = { passwordReset = false },
            loginAttempts = loginAttempts,
            emailIn = emailState
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(
    viewModel: MainViewModel,
    activity: Activity,
    onRegisterNavigate: () -> Unit,
    onExit: () -> Unit
) {
    var firstNameState by remember { mutableStateOf("") }
    var middleNameState by remember { mutableStateOf("") }
    var lastNameState by remember { mutableStateOf("") }
    var userNameState by remember { mutableStateOf("") }
    var phoneState by remember { mutableStateOf("") }
    var addressState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var confirmState by remember { mutableStateOf("") }
    var toSecQuestion by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 15.dp)
    )
    {
        item {
            if (!toSecQuestion) {
                ExitBar { onExit() }

                Image(
                    Icons.Rounded.AccountBox,
                    contentDescription = "Register icon",
                    modifier = Modifier.size(100.dp)
                )

                NameField(
                    value = firstNameState,
                    onValueChange = { firstNameState = it },
                    text = "First Name"
                )

                NameField(
                    value = middleNameState,
                    onValueChange = { middleNameState = it },
                    text = "Middle Name (Optional)"
                )

                NameField(
                    value = lastNameState,
                    onValueChange = { lastNameState = it },
                    text = "Last Name"
                )

                UsernameField(
                    value = userNameState,
                    onValueChange = { userNameState = it }
                )

                PhoneField(
                    value = phoneState,
                    onValueChange = {
                        //Used to restrict user input to numbers
                        if (it.length <= 10 && !Regex("[^0-9]").containsMatchIn(it)) phoneState = it
                    }
                )

                AddressField(
                    value = addressState,
                    onValueChange = { addressState = it }
                )

                EmailField(
                    value = emailState,
                    onValueChange = { emailState = it }
                )

                PasswordField(
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    isConfirm = false
                )

                PasswordField(
                    value = confirmState,
                    onValueChange = { confirmState = it },
                    isConfirm = true
                )

                Text(
                    text = "NOTE: Email & Password will be used to Login!",
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Row {
                    Button(onClick = onExit) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Button(onClick = {
                        onContinueClick(
                            firstName = firstNameState,
                            middleName = middleNameState,
                            lastName = lastNameState,
                            email = emailState,
                            password = passwordState,
                            confirm = confirmState,
                            address = addressState,
                            phone = phoneState,
                            activity = activity,
                            onSuccess = { toSecQuestion = true }
                        )
                    })
                    {
                        Text("Continue")
                    }
                }
            } else {
                var expanded by remember { mutableStateOf(false) }
                val options = getSecQuestions()
                var selectedOption by remember { mutableStateOf(options[0]) }
                var answer by remember { mutableStateOf("") }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ExitBar { toSecQuestion = false }

                    Text(
                        "Security Question",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize(.8f)
                            .wrapContentSize(Alignment.TopStart)
                            .border(2.dp, Color.Black)
                            .padding(horizontal = 10.dp),
                    ) {
                        Text(
                            text = selectedOption,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(onClick = { expanded = true })
                                .padding(vertical = 10.dp),
                            textAlign = TextAlign.Center
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedOption = option
                                        expanded = false
                                    },
                                    text = { Text(option, textAlign = TextAlign.Center) },
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text("Answer") },
                        keyboardOptions = KeyboardOptions.Default,
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.QuestionAnswer,
                                contentDescription = "Answer edit Icon"
                            )
                        },
                    )

                    Row {
                        Button(onClick = onExit) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Button(onClick = {
                            if (answer == "") {
                                Toast.makeText(
                                    activity,
                                    "Answer cannot be left blank.",
                                    Toast.LENGTH_LONG,
                                ).show()
                            } else if (selectedOption == "") {
                                Log.e(TAG, "Error: selectedOption is empty")
                            } else {
                                viewModel.Register(
                                    firstName = firstNameState,
                                    middleName = middleNameState,
                                    lastName = lastNameState,
                                    userName = userNameState,
                                    phone = phoneState,
                                    address = addressState,
                                    email = emailState,
                                    password = passwordState,
                                    activity = activity,
                                    selectedOption = selectedOption,
                                    answer = answer,
                                    onRegister = { onRegisterNavigate() }
                                )
                            }
                        }) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}


fun onRegisterClick(
    firstName : String,
    middleName : String,
    lastName : String,
    email : String,
    password : String,
    address : String,
    userName : String,
    phone : String,
    activity: Activity,
    viewModel: MainViewModel,
    onRegisterNavigate: () -> Unit
) {
}
fun onContinueClick(
    firstName : String,
    middleName : String,
    lastName : String,
    email : String,
    password : String,
    confirm : String,
    address: String,
    phone: String,
    activity: Activity,
    onSuccess : () -> Unit
) {
    val validFirstName = isValidName(firstName)
    val validMiddleName = isValidName(middleName)
    val validLastName = isValidName(lastName)

    if (!validFirstName || !validMiddleName || !validLastName) {
        val nameStr = if (!validFirstName) "First Name"
        else if (!validMiddleName) "Middle Name"
        else "Last Name"

        Toast.makeText(
            activity,
            "$nameStr cannot contain characters outside of A-Z, a-z, & -",
            Toast.LENGTH_LONG,
        ).show()
    } else if (!isValidEmail(email)) {
        Toast.makeText(
            activity,
            "Invalid email. Try again.",
            Toast.LENGTH_LONG,
        ).show()
    } else if (phone.length < 10) {
        Toast.makeText(
            activity,
            "Invalid phone. Too short.",
            Toast.LENGTH_LONG,
        ).show()
    } else if (!ValidatePassword(password = password, confirm = confirm, activity = activity)) {
        Log.d(TAG, "Invalid password.")
        //Do Nothing
//    } else if (!ValidateAddress(address = address)) {
//        Toast.makeText(
//            activity,
//            "Invalid Address Format. Correct format example: Arlington, TX",
//            Toast.LENGTH_LONG,
//        ).show()
    } else {
        Log.d(TAG, "Valid Register input.")
        onSuccess()
    }
}

/**
 * @return true if password is valid, false otherwise
 * */
fun ValidatePassword(
    password: String,
    confirm : String = "",
    activity: Activity
) : Boolean {
    var valid = false

    if (confirm != "" && password != confirm) {
        Toast.makeText(
            activity,
            "Passwords do not match.",
            Toast.LENGTH_LONG,
        ).show()
    } else if (password.length < 6) {
        Toast.makeText(
            activity,
            "Password must be at least 6 characters long.",
            Toast.LENGTH_LONG,
        ).show()
    } else {
        valid = true
    }

    return valid
}

/***
 * Checks if string is a valid email.
 *
 * Reference: https://www.codevscolor.com/android-kotlin-validate-email
 */
fun isValidEmail(email : String) : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

/**
 * Check if a given name is valid (does not contain characters outside of A-Z, a-z, & -)
 * If invalid, ValidName notifies user of such
 * */
fun isValidName(name : String) : Boolean {
    return !Regex("[^A-Za-z-]").containsMatchIn(name)
}

@Composable
fun NameField(
    value : String,
    onValueChange : (String) -> Unit,
    text : String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text) },
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Icon(Icons.Rounded.Info, contentDescription = "Name Icon") }
    )
}

@Composable
fun UsernameField(
    value : String,
    onValueChange : (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Username") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = "Username Icon") }
    )
}

@Composable
fun PhoneField(
    value : String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Phone") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = "Phone Icon") }
    )
}

@Composable
fun AddressField(
    value : String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Address") },
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Icon(Icons.Rounded.Home, contentDescription = "Address Icon") }
    )
}

@Composable
fun StateField(
    value : String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Address") },
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = { Icon(Icons.Rounded.Home, contentDescription = "Address Icon") }
    )
}

@Composable
fun EmailField(
    value : String,
    onValueChange : (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = { Icon(Icons.Rounded.Email, contentDescription = "Email Icon") }
    )
}

@Composable
fun PasswordField(
    value : String,
    onValueChange : (String) -> Unit,
    isConfirm : Boolean
) {
    var passwdVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { if (!isConfirm) Text("Password") else Text("Confirm Password")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = "Password Icon") },
        trailingIcon = {
            val icon = if (!passwdVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (!passwdVisible) "Show Password" else "Hide Password"

            IconButton(onClick = { passwdVisible = !passwdVisible }) {
                Icon(icon, description)
            }
        },
        visualTransformation = if (passwdVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun SecQuestionPage(
    activity: Activity,
    viewModel: MainViewModel,
    onRegister: () -> Unit,
    onExit : () -> Unit,
    onCancel : () -> Unit
) {
}

fun getSecQuestions() : List<String> {
    return listOf(
        "Do you think water is wet and why?",
        "What is your favorite topping on pizza? (Plz don't say pineapple)",
        "What was the name of your first crush in high school?",
        "What is the title and artist of your favorite song?",
        "Why did you pick your current career?",
        "What is the name of your great grandfather?",
        "Who was your favorite teacher in high school?"
    )
}

@Preview
@Composable
fun SecQuestionPagePreview() {
    SecQuestionPage(
        activity = Activity(),
        viewModel = MainViewModel(),
        onRegister = {},
        onExit = {},
        onCancel = {}
    )
}

@Composable
fun SeqQPassResetPage(
    viewModel: MainViewModel,
    activity: Activity,
    onResetPassword : () -> Unit,
    onExit : () -> Unit,
    onCancel : () -> Unit,
    loginAttempts : Int,
    emailIn: String
) {
    var expanded by remember { mutableStateOf(false) }
    val options = getSecQuestions()
    var selectedOption by remember { mutableStateOf(options[0]) }
    var answer by remember { mutableStateOf("") }
    var passwordReset by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf(emailIn) }

    if (loginAttempts < 3) ExitBar { onExit() }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(text = "Reset Password", fontSize = 15.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(10.dp))

        //Email text field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    Icons.Rounded.Email,
                    contentDescription = "Email icon"
                )
            },
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (!passwordReset) {
            Text(
                "Select your security question and write your answer to reset your password.",
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(10.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .wrapContentSize(Alignment.TopStart)
                    .border(2.dp, Color.Black)
                    .padding(horizontal = 10.dp),
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { expanded = true })
                        .padding(10.dp),
                    textAlign = TextAlign.Center
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption = option
                                expanded = false
                            },
                            text = { Text(option, textAlign = TextAlign.Center) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = answer,
                onValueChange = { answer = it },
                label = { Text("Answer") },
                keyboardOptions = KeyboardOptions.Default,
                leadingIcon = {
                    Icon(
                        Icons.Rounded.QuestionAnswer,
                        contentDescription = "Answer edit Icon"
                    )
                },
            )

            Row {
                Button(onClick = onCancel) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(20.dp))

                Button(onClick = {
                    if (answer == "") {
                        Toast.makeText(
                            activity,
                            "Answer cannot be left blank.",
                            Toast.LENGTH_LONG,
                        ).show()
                    } else {
                        viewModel.isCorrectSecQuestion(
                            question = selectedOption,
                            answer = answer,
                            email = email,
                            activity = activity,
                            onMatch = { passwordReset = true }
                        )
                    }
                }) {
                    Text("Submit")
                }
            }
        } else {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Click send to receive a password reset link in your email.",
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(onClick = {
                    viewModel.ResetPassword(email = email)
                }) {
                    Text(text = "Send Link")
                }
            }
//            ResetPasswordPage(email = email, viewModel = viewModel, activity = activity) { onResetPassword() }
        }
    }
}

@Preview
@Composable
fun SeqQPassResetPagePreview() {
    SeqQPassResetPage(
        viewModel = MainViewModel(),
        activity = Activity(),
        onCancel = {},
        onExit = {},
        onResetPassword = {},
        loginAttempts = 0,
        emailIn = ""
    )
}

@Composable
fun ResetPasswordPage(
    email: String,
    viewModel : MainViewModel,
    activity : Activity,
    onResetPassword: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    PasswordField(
        value = password,
        onValueChange = { password = it },
        isConfirm = false
    )

    PasswordField(
        value = confirm,
        onValueChange = { confirm = it },
        isConfirm = true
    )

    Button(onClick = {
        if (ValidatePassword(password = password, confirm = confirm, activity = activity)) {
            Log.d(TAG, "Updating Password....")
//            viewModel.UpdateUserData(password = password)
            onResetPassword()
        }
    }) {
        Text("Submit")
    }
}

@Composable
fun ExitBar(
    onExit : () -> Unit
) {
    Row (
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(Icons.Rounded.ArrowBack, "Back arrow", modifier = Modifier
            .clickable(onClick = onExit)
            .size(60.dp)
            .padding(15.dp))
    }
}

/**
 * @return True if the address is valid, False otherwise
 * */
fun ValidateAddress(address: String) : Boolean {
    var isValid = false
    val addressPattern = Regex("([a-zA-Z]+), ([A-Z]{2})$")
    val file = File("/java/com/jik/personaltutoringservice/ui/data/bannedWords.txt")

    file.forEachLine {
        val dataPattern = Regex("([a-zA-Z ]+): ([A-Z]{2})$")
        val dataMatch = dataPattern.find(it)
        val addressMatch = addressPattern.find(address)

        if (dataMatch != null && addressMatch != null) {
            val state = dataMatch.groups[2]?.value
            val addressState = addressMatch.groups[2]?.value
            if (state == addressState) isValid = true
        }
    }

    return isValid
}