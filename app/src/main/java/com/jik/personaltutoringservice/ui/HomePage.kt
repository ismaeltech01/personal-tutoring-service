package com.jik.personaltutoringservice.ui


import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.firebase.ui.auth.data.model.User
import com.jik.personaltutoringservice.R
import com.jik.personaltutoringservice.ui.UserCard
import java.math.BigDecimal

val HomePageModifier = Modifier
    .fillMaxHeight(.85f)
    .fillMaxWidth()

@Composable
fun HomePage(
    modifier: Modifier,
    onEditCard: () -> Unit,
    viewModel: MainViewModel,
    activity: Activity,
    loggedIn: Boolean,
    onSigninClick: () -> Unit
) {
    var showPayPage by remember { mutableStateOf(false) }
    var tutorRate by remember { mutableStateOf("") }
    var tutorEmail by remember { mutableStateOf("") }
    var tutorName by remember { mutableStateOf("") }
    var displayFireDialog by remember { mutableStateOf(false) }

    if (!loggedIn) {
        Column (
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Please Signin to hire tutors")
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = onSigninClick
            ) {
                Text("Signin")
            }
        }
    } else if (!showPayPage) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Current Tutors",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.CenterHorizontally)
            )

            for (entry in viewModel.tutors) {
                val fullname = entry.value["fullName"].toString()
                val userName = entry.value["userName"].toString()
                val email = entry.value["email"].toString()
                val raTe = entry.value["price"].toString()
                val profilePic = entry.value["imageUrl"].toString()
                val loc = entry.value["address"].toString()
                val rating = entry.value["rating"].toString()
                Log.d(TAG, "Displaying tutor in home: $fullname")

                Column {
                    UserCard(
                        fullName = fullname,
                        userName = userName,
                        rate = raTe,
                        imageUrl = profilePic,
                        isHome = true,
                        location = loc,
                        onFire = {
                            tutorName = ParseFullName(fullname)
                            displayFireDialog = true
                            tutorEmail = email
                        },
                        onPay = {
                            showPayPage = true
                            tutorRate = raTe
                            tutorEmail = email
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Text(
                text = "Suggested",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(15.dp)
                    .align((Alignment.CenterHorizontally))
            )
//            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
//                Column(modifier = Modifier.align(Alignment.CenterVertically)) {
//                    Image(
//                        painter = painterResource(id = R.drawable.profileimage),
//                        contentDescription = "Profile Image",
//                        modifier = Modifier
//                            .size(75.dp)
//                            .clip(CircleShape)
//                            .border(5.dp, Color.White, CircleShape)
//                    )
//                }
//            }
        }
    } else {
        PayTutorPage(
            rate = BigDecimal(tutorRate),
            userEmail = viewModel.email,
            tutorEmail = tutorEmail,
            invalidCard = !viewModel.ConfirmBankingInfo(),
            onExit = { showPayPage = false },
            onEditCard = onEditCard,
            viewModel = viewModel,
            activity = activity
        )
    }

    if (displayFireDialog) {
        FireDialog(
            fullName = tutorName
        ) {
            viewModel.FireTutor(tutorEmail)
            displayFireDialog = false
        }
    }
}
