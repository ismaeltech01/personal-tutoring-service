//This is just default file for reference
package com.jik.personaltutoringservice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jik.personaltutoringservice.ui.HomePage
import com.jik.personaltutoringservice.ui.MessagingPage
import com.jik.personaltutoringservice.ui.Navbar
import com.jik.personaltutoringservice.ui.OtherPage
import com.jik.personaltutoringservice.ui.ProfilePage
import com.jik.personaltutoringservice.ui.SearchPage
import com.jik.personaltutoringservice.ui.theme.PersonalTutoringServiceTheme

//MainActivity is were main app is loaded
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            //Modifier applied to all pages of the app
            val pageModifier = Modifier
                .background(color = Color.White)
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
                            ProfilePage(modifier = pageModifier)
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
                                { navController.navigate("reporting") })
                        }
                        //Below here are routes relating to the OtherPage links
                        composable("calendar") {

                        }
                        composable("courses") {

                        }
                        composable("payments") {

                        }
                        composable("ads") {

                        }
                        composable("settings") {

                        }
                        composable("reporting") {

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

@Composable
fun Titlebar(modifier : Modifier) {
    Text(text = "Personal Tutoring Service", modifier = modifier)
}