package com.jik.personaltutoringservice.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SearchPage(
    mainVM: MainViewModel,
    searchVM: SearchViewModel = viewModel(),
    currentEmail: String
) {
    val tutors = searchVM.tutors
    var searchIn by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    //Filtering
    var priceFilter by remember { mutableIntStateOf(120) }
    var distanceFilter by remember { mutableIntStateOf(100) }
    var ratingFilter by remember { mutableIntStateOf(1) }
    var availableFilter by remember { mutableStateOf(true) }

    //Sorting
    var priceSort by remember { mutableStateOf(false) }
    var distanceSort by remember { mutableStateOf(false) }
    var ratingSort by remember { mutableStateOf(false) }
    var showTutor by remember { mutableStateOf(false) }
    var tFullName by remember { mutableStateOf("") }
    var tUserName by remember { mutableStateOf("") }
    var tUID by remember { mutableStateOf("") }
    var tEmail by remember { mutableStateOf("") }
    var tPhone by remember { mutableStateOf("") }
    var tAddress by remember { mutableStateOf("") }
    var tImgUrl by remember { mutableStateOf("") }
    var tPrice by remember { mutableStateOf("") }
    var tRating by remember { mutableStateOf("") }
    var tAvailable by remember { mutableStateOf(false) }


    if (showDialog)
        SortFilterDialog(
            onDismiss = { showDialog = false },
            priceFilter = priceFilter.toFloat(),
            updatePriceFilter = { value -> priceFilter = value.toInt() },
            distanceFilter = distanceFilter.toFloat(),
            updateDistanceFilter = { value -> distanceFilter = value.toInt() },
            ratingFilter = ratingFilter.toFloat(),
            updateRatingFilter = { value -> ratingFilter = value.toInt() },
            availableFilter = availableFilter,
            toggleAvailableFilter = { availableFilter = !availableFilter },
            priceSortSet = priceSort,
            distanceSortSet = distanceSort,
            ratingSortSet = ratingSort,
            togglePriceSort = { priceSort = !priceSort },
            toggleDistanceSort = { distanceSort = !distanceSort },
            toggleRatingSort = { ratingSort = !ratingSort }
        )

    if (!showTutor) {
        LazyColumn (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                //Search, Filter, & Sort in top
                Column {
                    OutlinedTextField(
                        value = searchIn,
                        onValueChange = { searchIn = it },
                        label = { Text("Search") },
                        keyboardOptions = KeyboardOptions.Default,
                        trailingIcon = {

                            IconButton(onClick = {
                                searchVM.searchTutors(
                                    name = searchIn,
                                    price = priceFilter.toDouble(),
                                    rating = ratingFilter.toDouble(),
                                    available = availableFilter,
                                    currentEmail = currentEmail
                                )
                            }) {
                                Icon(Icons.Rounded.Search, "Search Icon")
                            }
                        }
                    )

                    Button(onClick = { showDialog = true }) {
                        Text(text = "Sort & Filter")
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))
            }
            item {
                //Tutors
                for (pair in tutors) {
                    val fullName = pair.value["fullName"].toString()
                    val userName = pair.value["userName"].toString()
                    val price = pair.value["price"].toString()
                    val rating = pair.value["rating"].toString()
                    val available = pair.value["available"] as Boolean
                    val imageUrl = pair.value["imageUrl"].toString()
                    val phone = pair.value["phone"].toString()
                    val email = pair.value["email"].toString()
                    val address = pair.value["address"].toString()
                    val uId = pair.key
                    Log.d(TAG, "Displaying user: $fullName")

                    Spacer(modifier = Modifier.height(10.dp))

                    UserCard(
                        fullName = fullName,
                        userName = userName,
                        imageUrl = imageUrl,
                        rate = price,
                        onClick = {
                            //onClick
                            tFullName = fullName
                            tUserName = userName
                            tPrice = price
                            tRating = rating
                            tAvailable = available
                            tImgUrl = imageUrl
                            tPhone = phone
                            tEmail = email
                            tAddress = address
                            showTutor = true
                            tUID = uId
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    } else {
        ExitBar { showTutor = false }
        ProfilePage(
            modifier = Modifier.fillMaxWidth(),
            onLoginClick = { /*TODO*/ },
            onRegisterClick = { /*TODO*/ },
            onTutorClick = { /*TODO*/ },
            loggedIn = true,
            fullName = tFullName,
            userName = tUserName,
            email = tEmail,
            phone = tPhone,
            address = tAddress,
            isTutor = true,
            viewMode = true,
            searched = true,
            imageUrl = tImgUrl,
            viewModel = mainVM,
            onHire = { showTutor = false },
            uId = tUID
        )
    }
}

@Composable
fun SortFilterDialog(
    onDismiss: () -> Unit,
    priceFilter: Float,
    updatePriceFilter: (Float) -> Unit,
    distanceFilter: Float,
    updateDistanceFilter: (Float) -> Unit,
    ratingFilter: Float,
    updateRatingFilter: (Float) -> Unit,
    availableFilter: Boolean,
    toggleAvailableFilter: () -> Unit,
    priceSortSet: Boolean,
    distanceSortSet: Boolean,
    ratingSortSet: Boolean,
    togglePriceSort: () -> Unit,
    toggleDistanceSort: () -> Unit,
    toggleRatingSort: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(.95f)
                .padding(20.dp)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                item {
                    Text(text = "Filter", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(10.dp))

                    Column {
                        Text(text = "Price: $ ${priceFilter.toInt()} >=")
                        Slider(
                            value = priceFilter,
                            onValueChange = { updatePriceFilter(it) },
                            valueRange = 1f..120f,
                            steps = 119
                        )

                        Text(text = "Distance: ${distanceFilter.toInt()} miles >=")
                        Slider(
                            value = distanceFilter,
                            onValueChange = { updateDistanceFilter(it) },
                            valueRange = 1f..100f,
                            steps = 99
                        )

                        Text(text = "Rating: ${ratingFilter.toInt()} =<")
                        Slider(
                            value = ratingFilter,
                            onValueChange = { updateRatingFilter(it) },
                            valueRange = 1f..10f,
                            steps = 8
                        )

                        Button(onClick = toggleAvailableFilter) {
                            Row {
                                Text(text = "Availability")
                                if (availableFilter) Icon(Icons.Rounded.CheckCircle, "Check mark")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(text = "Sort", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row() {
                        Button(onClick = togglePriceSort) {
                            Row {
                                Text(text = "Price")
                                if (priceSortSet) Icon(Icons.Rounded.CheckCircle, "Check mark")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(onClick = toggleDistanceSort) {
                            Row {
                                Text(text = "Distance")
                                if (distanceSortSet) Icon(Icons.Rounded.CheckCircle, "Check mark")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(onClick = toggleRatingSort) {
                            Row {
                                Text(text = "Rating")
                                if (ratingSortSet) Icon(Icons.Rounded.CheckCircle, "Check mark")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = onDismiss) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}