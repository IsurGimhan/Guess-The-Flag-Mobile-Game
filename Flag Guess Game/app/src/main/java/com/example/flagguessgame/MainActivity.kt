package com.example.flagguessgame

import android.content.Context
import android.content.Intent
import android.graphics.Paint.Align
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flagguessgame.ui.theme.FlagGuessGameTheme

var timerStateHolder = false
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlagGuessGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                        ){
                        homepageImage()
                        TimerSwitch()
                        HomePageFourButtons(this@MainActivity)
                    }

                }
            }
        }
    }
}

@Composable
fun homepageImage(){
    Image(
        painter = painterResource(id = R.drawable.home_page),
        contentDescription = "homePageImage")
}
@Composable
fun TimerSwitch() {
    var checked = rememberSaveable{ mutableStateOf(timerStateHolder) }

    Switch(
        checked = checked.value,
        onCheckedChange = {
            checked.value = it
            timerStateHolder = it
        }
    )
}

@Composable
fun HomePageFourButtons(context: Context) {

    Column {

        Button(
            onClick = {val intent = Intent(context, GuessTheCountry::class.java)
                intent.putExtra("timerState", timerStateHolder);
                intent.putExtra("methodRun", true);
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(3.dp)
                .size(165.dp, 40.dp)
        ) {
            Text("Guess the Country")
        }
        Button(
            onClick = {val intent = Intent(context, GuessHints::class.java)
                intent.putExtra("timerState", timerStateHolder);
                intent.putExtra("methodRun", true);
                context.startActivity(intent)},
            modifier = Modifier
                .padding(3.dp)
                .size(165.dp, 40.dp)
        ) {
            Text("Guess-Hints")
        }
        Button(
            onClick = {val intent = Intent(context, GuessTheFlag::class.java)
                intent.putExtra("timerState", timerStateHolder);
                intent.putExtra("methodRun", true);
                context.startActivity(intent)},
            modifier = Modifier
                .padding(3.dp)
                .size(165.dp, 40.dp)
        ) {
            Text("Guess the Flag")
        }
        Button(
            onClick = {val intent = Intent(context, AdvancedLevel::class.java)
                intent.putExtra("timerState", timerStateHolder);
                intent.putExtra("methodRun", true);
                context.startActivity(intent)},
            modifier = Modifier
                .padding(3.dp)
                .size(165.dp, 40.dp)
        ) {
            Text(" Advanced Level")
        }
    }
}