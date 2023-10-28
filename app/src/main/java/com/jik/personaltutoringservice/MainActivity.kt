package com.jik.personaltutoringservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jik.personaltutoringservice.ui.AgreementPage
import com.jik.personaltutoringservice.ui.HomePage
import com.jik.personaltutoringservice.ui.LoginPage
import com.jik.personaltutoringservice.ui.MainViewModel
import com.jik.personaltutoringservice.ui.MessagingPage
import com.jik.personaltutoringservice.ui.Navbar
import com.jik.personaltutoringservice.ui.OtherPage
import com.jik.personaltutoringservice.ui.PaymentsPage
import com.jik.personaltutoringservice.ui.ProfilePage
import com.jik.personaltutoringservice.ui.RegisterPage
import com.jik.personaltutoringservice.ui.ReportingPage
import com.jik.personaltutoringservice.ui.SearchPage
import com.jik.personaltutoringservice.ui.SettingsPage
import com.jik.personaltutoringservice.ui.theme.PersonalTutoringServiceTheme


//MainActivity is were main app is loaded
class MainActivity : ComponentActivity() {
//    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            //Get Auth data & Database data
            val navController = rememberNavController()
            val loggedIn by viewModel.loggedInState.collectAsState()
            val fullName by viewModel.fullNameState.collectAsState()
            val userName by viewModel.userName.collectAsState()
            val email by viewModel.emailState.collectAsState()
            val phone by viewModel.phoneState.collectAsState()
            val address by viewModel.addressState.collectAsState()
            val cardNum by viewModel.cardNumState.collectAsState()
            val expDate by viewModel.expDateState.collectAsState()
            val secCode by viewModel.secCodeState.collectAsState()
            val isTutor by viewModel.isTutorState.collectAsState()
            val tutors = viewModel.tutorsState.toMap()

            if (loggedIn) {
                viewModel.UpdateAuthData()
                viewModel.FetchUserData()
            }

            //Modifier applied to all pages of the app
            val pageModifier = Modifier
                .fillMaxWidth()

            PersonalTutoringServiceTheme {
                Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    /*
                    Titlebar used for testing, can be removed
                    Titlebar(modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .fillMaxWidth()
                        .weight(.1f))
                     */

                    //NavHost used to hold navigation page (current page that the user is looking at)
                    NavHost(navController, startDestination = "home", modifier = Modifier.weight(.87f)) {
                        //Each composable under the NavHost is a path/destination to travel to
                        // and the respective component to load for such destination
                        composable("home") {
                            HomePage(modifier = pageModifier)
                        }
                        composable("search") {
                            SearchPage()
                        }
                        composable("profile") {
                            ProfilePage(
                                modifier = pageModifier,
                                onLoginClick = { navController.navigate("login") },
                                onRegisterClick = { navController.navigate("register") },
                                onTutorClick = { navController.navigate("agreement") },
                                loggedIn = loggedIn,
                                fullName = fullName,
                                userName = userName,
                                email = email,
                                phone = phone,
                                address = address,
                                isTutor = isTutor
                            )
                        }
                        composable("agreement") {
                            AgreementPage()
                        }
                        composable("messaging") {
                            MessagingPage()
                        }
                        composable("other") {
                            OtherPage(
                                { navController.navigate("calendar") },
                                { navController.navigate("courses") },
                                { navController.navigate("payments") },
                                { navController.navigate("ads") },
                                { navController.navigate("settings") },
                                { navController.navigate("reporting") },
                                { navController.navigate("profile") },
                                onSignOutClick = {
                                    viewModel.SignOut(this@MainActivity)
                                    navController.navigate("home")
                                },
                                userSignedIn = loggedIn
                            )
                        }
                        //Below here are routes relating to the OtherPage links
                        composable("calendar") {

                        }
                        composable("courses") {

                        }
                        composable("payments") {
                            PaymentsPage(
                                cardNum = cardNum,
                                expDate = expDate,
                                secCode = secCode,
                                activity = this@MainActivity,
                                viewModel = viewModel,
                                navigate = { navController.navigate("other") }
                            )
                        }
                        composable("ads") {

                        }
                        composable("settings") {
                            SettingsPage()
                        }
                        composable("reporting") {
                            ReportingPage(tutors = tutors)
                        }
                        //Login & Registration pages
                        composable("login") {
                            LoginPage(viewModel, this@MainActivity) { navController.navigate("profile") }
                        }
                        composable("register") {
                            RegisterPage(viewModel, this@MainActivity) { navController.navigate("profile") }
                        }
                    }

                    //Navigation bar at the bottom
                    Navbar(modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth()
                        .weight(.13f),
                        { navController.navigate("home") },
                        { navController.navigate("search") },
                        { navController.navigate("profile") },
                        { navController.navigate("messaging") },
                        { navController.navigate("other") })
                }
            }
        }
    }
}