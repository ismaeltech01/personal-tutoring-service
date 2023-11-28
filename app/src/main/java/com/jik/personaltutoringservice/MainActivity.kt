package com.jik.personaltutoringservice

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jik.personaltutoringservice.ui.AdsPage
import com.jik.personaltutoringservice.ui.BecomeTutorPage
import com.jik.personaltutoringservice.ui.CoursesPage
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            val viewModel = viewModel<MainViewModel>()
            //Get Auth data & Database data
            val navController = rememberNavController()
            val loggedIn by viewModel.loggedInState.collectAsState()
            val fullName by viewModel.fullNameState.collectAsState()
            val userName by viewModel.userNameState.collectAsState()
            val email by viewModel.emailState.collectAsState()
            val phone by viewModel.phoneState.collectAsState()
            val address by viewModel.addressState.collectAsState()
            val imageUrl by viewModel.imageUrl.collectAsState()
            val cardNum by viewModel.cardNumState.collectAsState()
            val expDate by viewModel.expDateState.collectAsState()
            val secCode by viewModel.secCodeState.collectAsState()
            val isTutor by viewModel.isTutorState.collectAsState()
            val tutors = viewModel.tutorsState.toMap()
            val clients = viewModel.clientsState.toMap()
            val messages = viewModel.messages.toList()

            if (loggedIn) {
                viewModel.UpdateAuthData()
                viewModel.FetchUserData()
                viewModel.FetchUserBankingInfo()
                viewModel.FetchRelations()
            }

            //Modifier applied to all pages of the app
            val pageModifier = Modifier
                .fillMaxWidth()

            PersonalTutoringServiceTheme {
                Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    //NavHost used to hold navigation page (current page that the user is looking at)
                    NavHost(navController, startDestination = "home", modifier = Modifier.weight(.87f)) {
                        //Each composable under the NavHost is a path/destination to travel to
                        // and the respective component to load for such destination
                        composable("home") {
                            HomePage(
                                modifier = pageModifier,
                                onEditCard = { navController.navigate("payments") },
                                viewModel = viewModel,
                                activity = this@MainActivity,
                                onSigninClick = { navController.navigate("profile") },
                                loggedIn = loggedIn
                            )
                        }
                        composable("search") {
                            SearchPage(
                                mainVM = viewModel,
                                currentEmail = email
                            )
                        }
                        composable("profile") {
                            ProfilePage(
                                modifier = pageModifier,
                                onLoginClick = { navController.navigate("login") },
                                onRegisterClick = { navController.navigate("register") },
                                onTutorClick = { navController.navigate("become-tutor") },
                                loggedIn = loggedIn,
                                fullName = fullName,
                                userName = userName,
                                email = email,
                                phone = phone,
                                address = address,
                                isTutor = isTutor,
                                viewMode = false,
                                searched = false,
                                imageUrl = imageUrl,
                                viewModel = viewModel
                            )
                        }
                        composable("messaging") {
                            MessagingPage(
                                userName = userName,
                                email = email,
                                tutors = tutors,
                                viewModel = viewModel,
                                modifier = pageModifier,
                                onToSearch = { navController.navigate("search") },
                                context = this@MainActivity,
                                messages = messages
                            )
                        }
                        composable("other") {
                            OtherPage(
                                onCalendarClick = { navController.navigate("calendar") },
                                onCoursesClick = { navController.navigate("courses") },
                                onPaymentsClick = { navController.navigate("payments") },
                                onAdClick = { navController.navigate("ads") },
                                onSettingsClick = { navController.navigate("settings") },
                                onReportClick = { navController.navigate("reporting") },
                                onSigninClick = { navController.navigate("profile") },
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
                            CoursesPage(
                                viewModel = viewModel,
                                onSaveChange = {navController.navigate("search")})
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
                            AdsPage(
                                viewModel = viewModel,
                                onExit = {navController.navigate("profile")},
                            )
                        }
                        composable("settings") {
                            SettingsPage(
                                fullName = fullName,
                                userName = userName,
                                email = email,
                                password = "",
                                address = address,
                                phone = phone,
                                viewModel = viewModel,
                                activity = this@MainActivity
                            ) {
                                navController.navigate("other")
                            }
                        }
                        composable("reporting") {
                            ReportingPage(
                                tutors = tutors,
                                clients = clients,
                                isTutor = isTutor,
                                viewModel = viewModel
                            )
                        }
                        //Login & Registration pages
                        composable("login") {
                            LoginPage(viewModel,
                                this@MainActivity,
                                navigateSuccess = { navController.navigate("profile") },
                                onResetPassword = {
                                    navController.navigate("login")

                                    Toast.makeText(
                                        this@MainActivity,
                                        "",
                                        Toast.LENGTH_LONG,
                                    ).show()
                                },
                                onExit = {navController.navigate("profile")},
                                onSecQExit = { navController.navigate("login") }
                            )
                        }
                        composable("register") {
                            RegisterPage(
                                viewModel,
                                this@MainActivity,
                                onRegisterNavigate = { navController.navigate("profile") },
                                onExit = { navController.navigate("profile") }
                            )
                        }
                        composable("become-tutor") {
                            BecomeTutorPage(
                                viewModel = viewModel,
                                onSubmit = { navController.navigate("profile") }
                            )
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