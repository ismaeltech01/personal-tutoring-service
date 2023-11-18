package com.jik.personaltutoringservice.ui


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

val HomePageModifier = Modifier
    .fillMaxHeight(.85f)
    .fillMaxWidth()

// TODO: I need to make the icons clickable and either take them to something
@Composable
fun HomePage(modifier: Modifier, tutors : Map<String, Map<String, String>>) {
    Column (modifier = modifier) {
        Text(
            text = "Current Tutors",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.CenterHorizontally)
        )

        for(entry in tutors) {
           val fullname = entry.value["fullName"].toString()
           val userName = entry.value["userName"].toString()
           val raTe = entry.value["rate"].toString()
           val profilePic = entry.value["imageUrl"].toString()
           val loc = entry.value["location"].toString()
            Log.d(TAG, "Displaying tutor in home: $fullname")

            Column {
                UserCard(
                    fullName = fullname,
                    userName = userName,
                    rate = raTe,
                    imageUrl = profilePic,
                    onClick = {},
                    location = loc
                )
            }
       }


//        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
//             for(entry in tutors) {
//                 val fullname = ParseFullName(entry.value["fullName"].toString())
//
//                 Column(modifier = Modifier.align(Alignment.CenterVertically)) {
//                     Image(
//                         painter = painterResource(id = R.drawable.profileimage),
//                         contentDescription = "Profile Image",
//                         modifier = Modifier
//                             .size(75.dp)
//                             .clip(CircleShape)
//                             .border(5.dp, Color.White, CircleShape)
//                     )
//
//                     Text(text = fullname)
//                 }
//                 Spacer(modifier = Modifier.width(5.dp))
//
//
//             }
//        }
        Text(
            text = "Suggested",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(15.dp)
                .align((Alignment.CenterHorizontally))
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
// TODO: Add functionally that integrated with advertisment to display tutors in catogories intreseted in
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Image(
                    painter = painterResource(id = R.drawable.profileimage),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(75.dp)
                        .clip(CircleShape)
                        .border(5.dp, Color.White, CircleShape)
                )
/*
this is going to be where the name of suggested tutor are displayed after advertisements are made
 */
//                Text(text = fullname)
            }
        }

    }
}
