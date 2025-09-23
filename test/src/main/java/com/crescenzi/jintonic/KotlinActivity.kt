@file:SuppressLint("SetTextI18n")
@file:Suppress("FunctionName")

package com.crescenzi.jintonic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


fun LOG(value: String?) {
    Log.e("MY-LOG", value.toString())
}

class KotlinActivity : AppCompatActivity() {


    companion object {
        @JvmStatic
        @RequireNetwork
        fun kotlinApiCall() {
            LOG("Kotlin Api call done successfully")
        }
    }

    @DebugLog
    fun hello() {
        LOG("hello")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.common_activity)

        val activityTitle = findViewById<TextView>(R.id.activityTitle)
        val apiButton = findViewById<Button>(R.id.apiButton)
        val activityButton = findViewById<Button>(R.id.activityButton)
        activityTitle.text = "KOTLIN Activity"
        apiButton.setOnClickListener {
            try {
                kotlinApiCall()
                Toast.makeText(this, "Post api call", Toast.LENGTH_SHORT).show()
                activityTitle.text = "Alright"
                activityTitle.setTextColor(getColor(android.R.color.holo_green_dark))
            } catch (e: Exception) {
                activityTitle.text = "Exception"
                activityTitle.setTextColor(getColor(android.R.color.holo_red_dark))
                Toast.makeText(this, "Inner catch", Toast.LENGTH_SHORT).show()
            }
        }

        activityButton.setOnClickListener {
            startActivity(Intent(this@KotlinActivity, JavaActivity::class.java))
            this@KotlinActivity.finish()
        }


    }
}
