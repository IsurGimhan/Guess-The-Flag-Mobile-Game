package com.example.flagguessgame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguessgame.ui.theme.FlagGuessGameTheme

var answerList = mutableListOf<String>() // all the correct answers
var answerCheckList = mutableStateListOf<Boolean>() // answer correct or not boolean value
var answerColor = mutableStateListOf<Color>() // color of the answer
var answerVisibility = mutableStateListOf<Boolean>() // make visible the correct answer for the wrong input
var submitPressedTimes = mutableIntStateOf(0) // submit pressed count
var totalQuestions = mutableIntStateOf(3) // total question in score bord
var correctAnswers = mutableIntStateOf(0) // correct answers in the score board
var timerStateForAdvanceLevel = mutableStateOf(false) // timer on off value
var advanceLevelButtonCondition = mutableStateOf(false)  // submit and next button condition
var advanceLevelAnswerIsCorrect = mutableStateOf(false) // checking the answer correct or not and saving the boolean value
var advanceLevelAlertBoxCondition = mutableStateOf(false)  // alertBox on off condition
var oneTimeRunForAdvanceLevel = false

class AdvancedLevel : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val countryNameAndResIdMap = flagNameAndResId() // getting the country name and image resId
        oneTimeRunForAdvanceLevel = intent.getBooleanExtra("methodRun",false)
        answerList = mutableListOf("Q1", "Q2", "Q3")                              //
        answerCheckList = mutableStateListOf(false, false, false)                 //
        answerColor = mutableStateListOf(Color.Black, Color.Black, Color.Black)   //
        answerVisibility = mutableStateListOf(false, false, false)                //
        submitPressedTimes = mutableIntStateOf(0)                           //  setting the values to default
        advanceLevelButtonCondition = mutableStateOf(false)                 //
        advanceLevelAnswerIsCorrect = mutableStateOf(false)                 //
        advanceLevelAlertBoxCondition = mutableStateOf(false)               //
        if(intent.getBooleanExtra("methodRun",false)){           //
            totalQuestions = mutableIntStateOf(3)
            correctAnswers = mutableIntStateOf(0)
        }

        if (oneTimeRunForAdvanceLevel){
            timerStateForAdvanceLevel = mutableStateOf(intent.getBooleanExtra("timerState",false))
            oneTimeRunForAdvanceLevel = false
        }


        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End) {
                            ScoreBoard()
                        }
                        CountdownTimer(timerStateForAdvanceLevel,30) { timeZeroFUnctionsForAdvanceLevel()}

                        imageAndTextBox1(countryNameAndResIdMap)
                        imageAndTextBox2(countryNameAndResIdMap)
                        imageAndTextBox3(countryNameAndResIdMap)
                        submitAndNextButtonForAdvanceLevel(this@AdvancedLevel, countryNameAndResIdMap)
                    }
                }
            }
        }
    }
}

// this method used when the timer hit zero
fun timeZeroFUnctionsForAdvanceLevel(){
    advanceLevelButtonCondition.value = true
    advanceLevelAnswerIsCorrect.value =  answerCheckList.all { it }
    advanceLevelAlertBoxCondition.value = true
    submitPressedTimes.intValue = 3
}

// flag id and res id of the country
fun flagNameAndResId(): Map<String,Int>{
    val countryMap = Countries().countriesMap
    val flagMap = Countries().ThreeRandomFlags()
    val countryNameAndResIdList = mutableMapOf<String,Int>()
    for(i in flagMap.keys.toList()){
        countryNameAndResIdList[countryMap[i.uppercase()].toString()] = Integer.parseInt(flagMap[i].toString())
    }
    return countryNameAndResIdList
}


// score board for the correct answers
@Composable
fun ScoreBoard(){
    Card(
        modifier = Modifier
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        shape = RoundedCornerShape(16.dp),
    ){
        Text("${correctAnswers.intValue} / ${totalQuestions.intValue}", fontSize = 20.sp)
    }

}


// 1st image and text field
@Composable
fun imageAndTextBox1(countryNameAndResId : Map<String,Int>){

    val imageId by rememberSaveable{ mutableIntStateOf(countryNameAndResId.values.toList()[0]) }
    val countryName by rememberSaveable{ mutableStateOf(countryNameAndResId.keys.toList()[0]) }
    var text by rememberSaveable{ mutableStateOf("") }

    Column (
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Image(
                painter = painterResource(imageId),
                contentDescription = "Image",
                modifier = Modifier.size(width = 200.dp, height = 100.dp)

            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                answerList[0] = text.lowercase()
            },
            textStyle = TextStyle(answerColor[0]),
            label = { Text("enter the country name") },
            enabled = !answerCheckList[0]

        )
        if (!answerCheckList[0] && submitPressedTimes.intValue == 3) {
            Text(text = countryName, color = Color.Blue)
        }
    }
}

// 2nd image and text field
@Composable
fun imageAndTextBox2(countryNameAndResId : Map<String,Int>){

    val imageId by rememberSaveable{ mutableIntStateOf(countryNameAndResId.values.toList()[1]) }
    val countryName by rememberSaveable{ mutableStateOf(countryNameAndResId.keys.toList()[1]) }
    var text by rememberSaveable{ mutableStateOf("") }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Image(
                painter = painterResource(imageId),
                contentDescription = "Image",
                modifier = Modifier.size(width = 200.dp, height = 100.dp)

            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                answerList[1] = newValue.lowercase()
            },
            label = { Text("enter the country name") },
            enabled = !answerCheckList[1],
            textStyle = TextStyle(answerColor[1])

        )

        if (!answerCheckList[1] && submitPressedTimes.intValue == 3) {
            Text(text = countryName, color = Color.Blue)
        }
    }

}

// 3d image and textField
@Composable
fun imageAndTextBox3(countryNameAndResId : Map<String,Int>){

    val imageId by rememberSaveable{ mutableIntStateOf(countryNameAndResId.values.toList()[2]) }
    val countryName by rememberSaveable{ mutableStateOf(countryNameAndResId.keys.toList()[2]) }
    var text by rememberSaveable{ mutableStateOf("") }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Image(
                painter = painterResource(imageId),
                contentDescription = "Image",
                modifier = Modifier.size(width = 200.dp, height = 100.dp)

            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                if(!answerCheckList[2]){
                    text = newValue
                    answerList[2] = newValue.lowercase() }
            },

            label = { Text("enter the country name") },
            enabled = !answerCheckList[2],
            textStyle = TextStyle(answerColor[2])
        )

        if (!answerCheckList[2] && submitPressedTimes.intValue == 3) {
            Text(text = countryName, color = Color.Blue)
        }
    }

}


// submit and next button for advance level
@Composable
fun submitAndNextButtonForAdvanceLevel(context: Context, countryNameAndResIdMap : Map<String,Int>){
    val statement by rememberSaveable { mutableStateOf(advanceLevelButtonCondition) }

    var answersCondition by rememberSaveable{ mutableStateOf(advanceLevelAnswerIsCorrect) }
    var dialogBoxOpen by rememberSaveable{ mutableStateOf(advanceLevelAlertBoxCondition) }


    if(!statement.value){
        Button(
            onClick = {
                advanceLevelAnswersCheck(countryNameAndResIdMap)
                answersCondition.value = answerCheckList.all { it }

                if(submitPressedTimes.intValue == 2 || answersCondition.value){
                    statement.value = true
                    dialogBoxOpen.value = true
                }

                submitPressedTimes.intValue ++
            }
        ) {
            Text(text = "Submit")
        }
    }else{
        Button(
            onClick = {
                for(i in answerCheckList){
                    if(i){
                        correctAnswers.intValue ++
                    }
                }
                totalQuestions.intValue += 3
                (context as Activity).finish()
                val intent = Intent(context, AdvancedLevel::class.java)
                context.startActivity(intent)
            }
        ) {
            Text(text = "Next")
        }
    }

    if(dialogBoxOpen.value){
        if(answersCondition.value){
            AlertDialogBox(true,"", onClose = {dialogBoxOpen.value = false})
        }else if(statement.value && answersCondition.value){
            AlertDialogBox(true,"", onClose = {dialogBoxOpen.value = false})
        }else if(statement.value){
            AlertDialogBox(false,"", onClose = {dialogBoxOpen.value = false})
        }
    }

}

// checking the all three answers correct or not
fun advanceLevelAnswersCheck(countryNameAndResIdMap : Map<String,Int>) {

    for (i in 0..2) {
        if (answerList[i] != "Q$i") {
            if (answerList[i] == countryNameAndResIdMap.keys.toList()[i].lowercase()) {
                answerCheckList[i] = true
                answerColor[i] = Color.Green
            }else{
                answerColor[i] = Color.Red
            }
        }else{
            answerColor[i] = Color.Red
        }
    }
}