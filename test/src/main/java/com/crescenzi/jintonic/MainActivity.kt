package com.crescenzi.jintonic

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


internal fun LOG(value: String?) {
    Log.e("MY-LOG", value.toString())
}

class MainActivity : ComponentActivity() {

    @RequireNetwork
    fun apiCall() {
        LOG("Api call done successfully")
    }


    @DebugLog
    fun hello() {
        LOG("hello")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Hello From Jintonic Test", color = Color.Green)


                        Button(onClick = {
                            try {
                                apiCall()
                            } catch (e: Exception) {
                                LOG("Exception by aspect caused internet ${e.message.toString()}")
                            }
                        }) {
                            Text("Click for Api Call ")


                        }
                    }
                }
            }
        }


    }
}
