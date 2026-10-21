
package com.e.ca2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.e.ca2.ui.theme.Ca2Theme
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Ca2Theme {
                TravelApp()
            }
        }
    }
}

@Composable
fun TravelApp() {

    val coroutineScope = rememberCoroutineScope()
    var log by remember { mutableStateOf("") }

    val exception = CoroutineExceptionHandler { _, throwable ->
        log += "\nMessage: ${throwable.message}"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Travel App",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Button(
                onClick = {
                    coroutineScope.launch(exception) {
                        try {
                            log += travel()
                        } catch (e: Exception) {
                            log += "\n${e.message}"
                        } finally {
                            log += "\nFinally search completed"
                        }
                    }
                }
            ) {
                Text("Book Travel")
            }

            Text(text = log)
        }
    }
}

suspend fun travel(): String = supervisorScope {

    val hostels = async(Dispatchers.IO) {
        gethostels()
    }

    val flights = async(Dispatchers.IO) {
        getflights()
    }

    val carRentals = async(Dispatchers.IO) {
        getCarRentals()
    }

    val hostelsAwait = hostels.await()
    val flightsAwait = flights.await()
    val carRentalsAwait = carRentals.await()

    "$hostelsAwait\n$flightsAwait\n$carRentalsAwait"
}

suspend fun gethostels(): String {
    delay(1000)
    return "All hostels detail returned"
}

suspend fun getflights(): String {
    delay(1500)
    return "All flights detail returned"
}

suspend fun getCarRentals(): String {
    delay(1200)
    return "All carRents detail returned"
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Ca2Theme {
        TravelApp()
    }
}
