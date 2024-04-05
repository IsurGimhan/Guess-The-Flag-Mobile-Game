package com.example.flagguessgame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flagguessgame.ui.theme.FlagGuessGameTheme
import kotlinx.coroutines.delay

var dropDownSelectedCountry = "" // user selected country from the drop down list
var imageDisplayId = ""  // country code of the displayed image
var timerStateForGuessTheCountry = mutableStateOf(false) // timer on off value
var guessTheCountryButtonCondition = mutableStateOf(false) // boolean condition for submit or next button
var guessTheCountryAnswerIsCorrect = mutableStateOf(false) // boolean value to store answers state
var guessTheCountryAlertDialogBox = mutableStateOf(false) // alert box open boolean condition
var oneTimeRunForGuessTheCountry = false // this variable is used to conform that if statement run only once

class GuessTheCountry : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        oneTimeRunForGuessTheCountry = intent.getBooleanExtra("methodRun",false) // getting the boolean value from the mainActivity
        guessTheCountryButtonCondition = mutableStateOf(false) // reverting
        guessTheCountryAnswerIsCorrect = mutableStateOf(false) //             to the
        guessTheCountryAlertDialogBox = mutableStateOf(false) //                     default values
        if(oneTimeRunForGuessTheCountry){
            //getting the boolean value from the mainActivity and running it only once
            timerStateForGuessTheCountry = mutableStateOf(intent.getBooleanExtra("timerState",false))
            oneTimeRunForGuessTheCountry = false
        }

        super.onCreate(savedInstanceState)

        setContent {
            FlagGuessGameTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())){
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            CountdownTimer(timerStateForGuessTheCountry,10) { timeZeroFUnctionsForGuessTheCountry() }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            ImageDisplay()

                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            dropDownList()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){

                            submitAndNextButton(this@GuessTheCountry)
                        }
                    }
                }
            }
        }
    }
}

//this function is used in all 4 activity to track the timer
@Composable
fun CountdownTimer(timerStateChange : MutableState<Boolean>, timeLimit : Int, timeZeroFunction : () ->Unit) {
    val timerState by rememberSaveable{ mutableStateOf(timerStateChange) }
    if(timerState.value) {
        var seconds by rememberSaveable { mutableIntStateOf(timeLimit) }

        LaunchedEffect(Unit) {
            while (seconds > 0) {
                delay(1000) // Delay for 1 second
                seconds--
            }
        }
        if (seconds == 0) {
            timeZeroFunction()
        }
        Text(text = "Time left: $seconds")
    }
}
// this function is used to control the flow after timer hit the zero
fun timeZeroFUnctionsForGuessTheCountry(){
    guessTheCountryAnswerIsCorrect.value = checkAnswer()
    guessTheCountryButtonCondition.value = true
    guessTheCountryAlertDialogBox.value = true
}


// display image to the user
@Composable
fun ImageDisplay(){
    val countryIdAndResId = Countries().randomImageID()
    val flagId by rememberSaveable { mutableStateOf(countryIdAndResId[0].toString()) }
    imageDisplayId = flagId // appending country key to the imageIdDisplay
    val flagResId = countryIdAndResId[1].toString().toInt()
    val randomImageId by rememberSaveable{ mutableIntStateOf(flagResId) }

    Card(
        modifier = Modifier
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        shape = RoundedCornerShape(16.dp), // Set the shape of the card (rounded corners)
    ) {
        Image(
            painter = painterResource(id = randomImageId),
            contentDescription = "Image",
            modifier = Modifier
                .size(width = 346.dp, height = 250.dp)
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

//display selectable dropdown list to the user
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropDownList(){
    val countriesMap = Countries()
        .countriesMap
        .entries
        .sortedBy { it.value }
        .associate { it.key to it.value }
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var country by rememberSaveable { mutableStateOf("") }


    Box(
        contentAlignment = Alignment.Center
    ){
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {isExpanded = it}) {
            TextField(
                enabled = false,
                value = country,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isExpanded = true
                        },
                        enabled = true
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .clickable {
                        isExpanded = !isExpanded

                    }
            )
            if (countriesMap != null) {
                ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded =false}) {
                    for (entry in countriesMap.entries) {
                        DropdownMenuItem(
                            text = { Text(countriesMap[entry.key].toString()) },
                            onClick = {
                                country = countriesMap[entry.key].toString()
                                isExpanded = false
                                dropDownSelectedCountry = country
                            }
                        )
                    }
                }
            }
        }
    }

}

// this function is used to provide user with submit button and next button
@Composable
fun submitAndNextButton(context: Context){
    // these 3 variables are used to control the button flow and alertBox
    val statement by rememberSaveable { mutableStateOf(guessTheCountryButtonCondition) }
    val answerState by rememberSaveable { mutableStateOf(guessTheCountryAnswerIsCorrect) }
    val dialogBoxOpen by rememberSaveable{ mutableStateOf(guessTheCountryAlertDialogBox)

    }

    if(!statement.value){ // checking the condition for submit button
        Button(
            onClick = {
                answerState.value  = checkAnswer()
                statement.value = true;
                dialogBoxOpen.value = true;
            }
        ) {
            Text(text = "Submit")
        }
    }else{
        Button(
            onClick = {(context as Activity).finish() // trigger the onDestroy method and use intent to reload the same page
                val intent = Intent(context, GuessTheCountry::class.java)
                intent.setData(Uri.parse(timerStateForGuessTheCountry.toString()))
                context.startActivity(intent)
            }
        ) {
            Text(text = "Next")
        }
    }
    if(dialogBoxOpen.value) {
        val country = Countries().countriesMap
        country[imageDisplayId.uppercase()]?.let {
            AlertDialogBox(condition = answerState.value,
                it, onClose = { dialogBoxOpen.value = false })
        }
    }
}

// this alert dialog box is used in all 4 activity's to notify the user with answer correct or not
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogBox(condition: Boolean,correctname : String ,onClose: () -> Unit) {
    val color = if (condition) Color.Green else Color.Red
    val message = if (condition) "CORRECT!" else "WRONG!"
     val correctName = if(condition) "" else correctname

    AlertDialog(
        onDismissRequest = { onClose()}
    ){
        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 150.dp)
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ){
            Column {
                Text(text = message,color = color, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = correctName,color = Color.Blue, textAlign = TextAlign.Center)
            }

        }

    }

}

//this method check the answer right or wrong and return a boolean value
// this method is called inside the submitAndNextButton method
fun checkAnswer(): Boolean{
    val countryList = Countries().countriesMap
    var answerState = false
    for ((key, value) in countryList) {
        if (value == dropDownSelectedCountry && key.lowercase() == imageDisplayId) {
            answerState = true

        }
    }
    return answerState
}
