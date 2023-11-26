package com.jik.personaltutoringservice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//NOTE: This page is just a test. Can be removed or kept if desired
@Composable
fun CoursesPage(


    math : CheckboxDefaults,
    coding : CheckboxDefaults,
    tennis : CheckboxDefaults,
    french : CheckboxDefaults,
    piano : CheckboxDefaults,
    viewModel : MainViewModel,
    onSaveChange: () -> Unit){

    var checkedStateMath by remember { mutableStateOf(false) }
    var checkedStatePiano by remember { mutableStateOf(false) }
    var checkedStateFrench by remember { mutableStateOf(false) }
    var checkedStateCoding by remember { mutableStateOf(false) }
    var checkedStateTennis by remember { mutableStateOf(false)}

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            Text("Course",
                fontSize = 50.sp)
            Text("Page",
                fontSize = 50.sp)
            Text("Select the courses you need the tutoring for.",
                fontSize = 15.sp)

            Row {
                Checkbox(checked = checkedStateMath,
                    modifier = Modifier.padding(16.dp),
                    onCheckedChange = { checkedStateMath = it })
                Text(text = "Math", modifier = Modifier.padding(16.dp),fontSize = 30.sp)
            }
            Row {
                Checkbox(checked = checkedStatePiano,
                    modifier = Modifier.padding(16.dp),
                    onCheckedChange = { checkedStatePiano = it })
                Text(text = "Piano", modifier = Modifier.padding(16.dp),fontSize = 30.sp)
            }
            Row {
                Checkbox(checked = checkedStateFrench,
                    modifier = Modifier.padding(16.dp),
                    onCheckedChange = { checkedStateFrench = it })
                Text(text = "French", modifier = Modifier.padding(16.dp),fontSize = 30.sp)
            }
            Row {
                Checkbox(checked = checkedStateCoding,
                    modifier = Modifier.padding(16.dp),
                    onCheckedChange = { checkedStateCoding = it })
                Text(text = "Coding", modifier = Modifier.padding(16.dp),fontSize = 30.sp)
            }
            Row {
                Checkbox(checked = checkedStateTennis,
                    modifier = Modifier.padding(16.dp),
                    onCheckedChange = { checkedStateTennis = it })
                Text(text = "Tennis", modifier = Modifier.padding(16.dp),fontSize = 30.sp)
            }

            Button(onClick = {
                viewModel.Course(
                    math = CheckboxDefaults,
                    coding = CheckboxDefaults,
                    tennis = CheckboxDefaults,
                    french = CheckboxDefaults,
                    piano = CheckboxDefaults,

            )
                             },
                modifier = Modifier
                    .fillMaxWidth()

                ){
                Text("Save Changes",fontSize = 30.sp)
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun CoursePreview(){
    CoursesPage(
        math = CheckboxDefaults,
        coding = CheckboxDefaults,
        tennis = CheckboxDefaults,
        french = CheckboxDefaults,
        piano = CheckboxDefaults,
        viewModel = MainViewModel(),
        onSaveChange = {})
}





