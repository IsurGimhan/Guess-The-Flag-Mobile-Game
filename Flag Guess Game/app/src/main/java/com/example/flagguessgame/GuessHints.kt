package com.example.flagguessgame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagguessgame.ui.theme.FlagGuessGameTheme

var selectedChar: Char = '1' // this variable is used to store the character entered to the textField
var outputName = mutableStateOf("") // used to display the dash line or updated name
var countryNameCharList = mutableListOf<Char>() // list of characters in selected country name
var guessHintImageResId = mutableIntStateOf(0) // image resource id used to display the image
var textFieldTxt = mutableStateOf("")
var incorrectLettersEntered = mutableIntStateOf(0) // incorrect letter entered counted
var timerStateForGuessHints = mutableStateOf(false) // timer on off  sate value
var guessHintAnswerCondition = mutableStateOf(false) // answer correct or not
var guessHintButtonCondition = mutableStateOf(false) // submit and next button condition
var guessHintAlertDialogBox = mutableStateOf(false) // alertbox on off condition
var countryName = mutableStateOf("") // selected country name
var oneTimeRunForGuessHints = false // one time change value only can be change when staring from the home screen

class GuessHints : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        oneTimeRunForGuessHints = intent.getBooleanExtra("methodRun",false)
        selectedChar = ' '                                   //
        outputName = mutableStateOf("")                //
        countryNameCharList = mutableListOf()                //
        guessHintImageResId = mutableIntStateOf(0)      //  restore original values 
        textFieldTxt = mutableStateOf("")               //
        incorrectLettersEntered = mutableIntStateOf(0)    //
        guessHintAnswerCondition = mutableStateOf(false)  //
        guessHintButtonCondition = mutableStateOf(false)  //
        guessHintAlertDialogBox = mutableStateOf(false)   //
        countryName = mutableStateOf("")

        if (oneTimeRunForGuessHints){
            timerStateForGuessHints = mutableStateOf(intent.getBooleanExtra("timerState",false))
            oneTimeRunForGuessHints = false
        }
        initOutputNameAndImageResId()

        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()

                    ){
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            CountdownTimer(timerStateForGuessHints,30) { timeZeroFUnctionsForGuessHints()}
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            CountryImageForHint()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            DisplayText()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            SingleCharacterInputField()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (
                            modifier =  Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            submitAndNextButtonForGuessHint(this@GuessHints)
                        }
                    }
                }
            }
        }
    }

}

// this method is used to handle the function after timer set to zero
fun timeZeroFUnctionsForGuessHints() {
    if(countryNameCharList.all{it == '*'}){
        guessHintAnswerCondition.value = true
        guessHintButtonCondition.value = true
        guessHintAlertDialogBox.value = true
    }else{
        guessHintButtonCondition.value = true
        guessHintAlertDialogBox.value = true
    }
}


//display random image for the guessing
@Composable
fun CountryImageForHint(){

    val randomImageId by rememberSaveable{ mutableStateOf(guessHintImageResId) }

    Card(
        modifier = Modifier
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        shape = RoundedCornerShape(16.dp), // Set the shape of the card (rounded corners)
    ) {
        Image(
            painter = painterResource(id = randomImageId.intValue),
            contentDescription = "Image",
            modifier = Modifier
                .size(width = 346.dp, height = 250.dp)
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

// this display the dashed line line can be updated live
@Composable
fun DisplayText(){

    var output by rememberSaveable { mutableStateOf(outputName) }

    OutlinedButton(
        shape = RectangleShape,
        onClick = {},
        enabled = false,

        ) {
        Text(text = output.value, color = Color.Black, fontSize = 20.sp)

    }
}


// this input field get one char at a time from the user
@Composable
fun SingleCharacterInputField() {
    var text by rememberSaveable{ mutableStateOf(textFieldTxt) }
    Column {
        OutlinedTextField(
            value = text.value,
            onValueChange = { newValue ->
                if (newValue.length <= 1) {
                    text.value = newValue
                    selectedChar = newValue.lowercase().toCharArray()[0]
                }
            },
            label = {Text("Enter a letter")},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            maxLines = 1
        )


    }
}

// this method handle the submitButton and nextButton
@Composable
fun submitAndNextButtonForGuessHint(context: Context){
    val statement by rememberSaveable { mutableStateOf(guessHintButtonCondition) }
    val incorrectLettersEnteredTimes by rememberSaveable{ mutableStateOf(incorrectLettersEntered) }
    val dialogBoxOpen by rememberSaveable{ mutableStateOf(guessHintAlertDialogBox) }
    val answersCondition by rememberSaveable{ mutableStateOf(guessHintAnswerCondition) }


    if(!statement.value){
        Button(
            onClick = {
                listHandler()
                if(countryNameCharList.all{it == '*'}){
                    answersCondition.value = true
                    statement.value = true
                    dialogBoxOpen.value = true
                }else if(incorrectLettersEnteredTimes.intValue == 3) {
                    statement.value = true
                    dialogBoxOpen.value = true
                }
            }
        ) {
            Text(text = "Submit")
        }
    }else{
        Button(
            onClick = {(context as Activity).finish()
                val intent = Intent(context, GuessHints::class.java)
                context.startActivity(intent)
            }
        ) {
            Text(text = "Next")
        }
    }
    if(dialogBoxOpen.value){
        if(statement.value) {
            AlertDialogBox(answersCondition.value, countryName.value, onClose = { dialogBoxOpen.value = false })
        }
    }
}

// this method is called in submitAndNextButtonForGuessHint method
//this method is used to check if character is in charList
fun listHandler() {
    if(countryNameCharList.contains(selectedChar)) {
        val index = countryNameCharList.indexOf(selectedChar)
        val outputName2 = outputName.value.substring(0, index) + selectedChar.uppercase() + outputName.value.substring(index + 1)
        countryNameCharList[index] = '*'
        outputName.value = outputName2
        Log.d("text1", outputName.toString())
        textFieldTxt.value = ""
    }else{
        textFieldTxt.value = ""
        incorrectLettersEntered.intValue ++
    }
}

// this method is run when the onCrate() state crated
fun initOutputNameAndImageResId(){

    val countryIdAndResId = Countries().randomImageID()
    val flagId = countryIdAndResId[0].toString()
    val flagResId = countryIdAndResId[1].toString().toInt()

    val countriesMap = Countries().countriesMap
    var countryNameDashLine  = ""
    for(i in countriesMap.keys){
        if(i.lowercase() == flagId){
            for(char in countriesMap.getValue(i).lowercase().toCharArray()){
                countryNameCharList.add(char)
                countryNameDashLine += "_"
            }
            countryName.value = countriesMap.getValue(i)
        }
    }
    guessHintImageResId.intValue = flagResId
    outputName.value = countryNameDashLine
}