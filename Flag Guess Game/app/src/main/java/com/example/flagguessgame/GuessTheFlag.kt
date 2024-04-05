package com.example.flagguessgame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flagguessgame.ui.theme.FlagGuessGameTheme

var selectedImageId = 0 // image code for selected image
var displayNextButton = mutableStateOf(false) // nextButton state changer
var alertBoxOpenForGuessTheFlag = mutableStateOf(false) // alert box open close state holder
var answerCondition = mutableStateOf(false) // answer correct or not
var enableButtons = mutableStateOf(true) // disable buttons after pressing one button
var timerStateForGuessTheFlag = mutableStateOf(false) // timer on off condition
var oneTimeRunForGuessTheFlag = false // one time change value value only change when directing from the mainActivity
class GuessTheFlag : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        oneTimeRunForGuessTheFlag = intent.getBooleanExtra("methodRun",false) //
        displayNextButton.value = false                                                        // setting the variable value to default
        alertBoxOpenForGuessTheFlag.value = false                                              //
        answerCondition.value = false                                                          //
        enableButtons.value = true                                                             //

        if (oneTimeRunForGuessTheFlag){
            timerStateForGuessTheFlag = mutableStateOf(intent.getBooleanExtra("timerState",false))
            oneTimeRunForGuessTheFlag = false
        }

        val flagIdAndResIdMap = Countries().ThreeRandomFlags()

        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CountdownTimer(timerStateForGuessTheFlag,10) { timeZeroFUnctionsForGuessTheFlag()}
                        displayCountryName(flagIdAndResIdMap)
                        displayCountryFlags(flagIdAndResIdMap)
                        nextButton(this@GuessTheFlag)
                    }
                }
            }
        }
    }
}

// method that run when the timer hit zero
fun timeZeroFUnctionsForGuessTheFlag() {

    alertBoxOpenForGuessTheFlag.value = true
    displayNextButton.value = true

}


// display the name of the country
@Composable
fun displayCountryName(flagIdAndResIdMap : Map<String,Int>) {

    val randomKey = flagIdAndResIdMap.keys.random()
    selectedImageId = flagIdAndResIdMap[randomKey]!!

    val countryName by rememberSaveable{ mutableStateOf(Countries().countriesMap[randomKey.uppercase()].toString()) }
    Card(
        modifier = Modifier
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = countryName,
            modifier =Modifier.align(Alignment.CenterHorizontally)
        )
    }

}

// display three image buttons
@Composable
fun displayCountryFlags(flagIdAndResIdMap : Map<String, Int>){
    val list by rememberSaveable{ mutableStateOf(flagIdAndResIdMap.values.toList()) }
    Column (modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            modifier = Modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            TextButton(
                shape = RectangleShape,
                enabled = enableButtons.value,
                onClick = {
                    if(list[0] == selectedImageId){
                        answerCondition.value = true
                    }
                    alertBoxOpenForGuessTheFlag.value = true
                    displayNextButton.value = true
                }
            ) {
                Image(
                    painter = painterResource(id = list[0]),
                    contentDescription = "Image",
                    modifier = Modifier.size(width = 400.dp, height = 160.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            TextButton(
                shape = RectangleShape,
                enabled = enableButtons.value,
                onClick = {
                    if(list[1] == selectedImageId){
                        answerCondition.value = true
                    }
                    alertBoxOpenForGuessTheFlag.value = true
                    displayNextButton.value = true
                }
            ) {
                Image(
                    painter = painterResource(id = list[1]),
                    contentDescription = "Image",
                    modifier = Modifier.size(width = 400.dp, height = 160.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            TextButton(
                shape = RectangleShape,
                enabled = enableButtons.value,
                onClick = {
                    if(list[2] == selectedImageId){
                        answerCondition.value = true
                    }
                    alertBoxOpenForGuessTheFlag.value = true
                    displayNextButton.value =true
                }
            ) {
                Image(
                    painter = painterResource(id = list[2]),
                    contentDescription = "Image",
                    modifier = Modifier.size(width = 400.dp, height = 160.dp)
                )
            }
        }
    }
}



// display next button when the image is clicked
@Composable
fun nextButton(context: Context){
    if(alertBoxOpenForGuessTheFlag.value){
        AlertDialogBox(answerCondition.value,"", onClose = { alertBoxOpenForGuessTheFlag.value = false })
        enableButtons.value = false
    }
    if(displayNextButton.value) {

        Button(
            onClick = {
                (context as Activity).finish()
                val intent = Intent(context, GuessTheFlag::class.java)
                context.startActivity(intent)
            }
        ){
            Text(text = "Next")
        }
    }
}
