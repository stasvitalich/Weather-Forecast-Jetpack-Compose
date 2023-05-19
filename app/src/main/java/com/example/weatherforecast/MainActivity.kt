package com.example.weatherforecast

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecast.ui.theme.WeatherForecastTheme
import org.json.JSONObject

const val API_KEY = "98fe4fad6e8eb7aaf6b1942c9bc6615e"
val latitude = 52.235001
val longitude = 76.374451


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Pavlodar", this)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, context: Context) {

    val state = remember {
        mutableStateOf("Unknown")
    }

    val temperatureCel = remember {
        mutableStateOf(0.0)
    }

    val roundedTemperature = remember {
        mutableStateOf("0.0")
    }



    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .background(color = Color.Green),
            contentAlignment = Alignment.Center
        ) {

            Text(text = "Temperature in $name = ${roundedTemperature.value}")
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(color = Color.LightGray),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    getResult("Pavlodar", state, temperatureCel, roundedTemperature, context)
                }, modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Get the result")
            }

        }

    }
}

private fun getResult(
    city: String,
    state: MutableState<String>,
    temperatureCel: MutableState<Double>,
    roundedTemperature: MutableState<String>,
    context: Context
) {

    val url =
        "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$API_KEY"

    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        com.android.volley.Request.Method.GET,
        url,
        { response ->
            val obj = JSONObject(response)
            state.value = obj.getJSONObject("main").getString("temp")
            temperatureCel.value = state.value.toDouble() - 273.15
            roundedTemperature.value = "%.1f".format(temperatureCel.value)

        },
        { error ->
        }

    )
    queue.add(stringRequest)

}