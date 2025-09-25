@file:SuppressLint("SetTextI18n")
@file:Suppress("FunctionName")

package com.crescenzi.jintonic.test

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.crescenzi.jintonic.domain.exceptions.JintonicException
import com.crescenzi.jintonic.project.extra.battery.MinBattery
import com.crescenzi.jintonic.project.internet.status.RequireInternet
import com.crescenzi.jintonic.project.internet.vpn.RequireVpn
import com.crescenzi.jintonic.project.profiling.time.Timed
import com.crescenzi.jintonic.project.security.root.WithRoot
import com.crescenzi.jintonic.project.security.screen.SecureWindow
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal fun LOG(value: String?) {
    Log.e("MY-LOG", value.toString())
}

@SecureWindow
class KotlinActivity : AppCompatActivity() {



    @RequireInternet(mustThrow = true)
    @MinBattery(minOrEqualValue = 60)
    fun kotlinApiCall() = "Kotlin Api call done successfully"

    @Timed
    fun hello() = runBlocking {
        delay(5000)
    }

    @RequireVpn
    fun vpnMethod() {
        LOG("Executed Vpn Method")
    }

    @WithRoot(forRoot = false)
    fun rootMethod() {
        LOG("Dentro root method")
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
                //rootMethod()
                 LOG("Risultato della apiCall() => ${kotlinApiCall()}")
                activityTitle.text = "Alright"
                activityTitle.setTextColor(getColor(android.R.color.holo_green_dark))

            } catch (je: JintonicException) {
                activityTitle.text = "Exception"
                activityTitle.setTextColor(getColor(android.R.color.holo_red_dark))
                LOG("Exception ${je.message.toString()}")
            }
        }

        activityButton.setOnClickListener {
            startActivity(Intent(this@KotlinActivity, JavaActivity::class.java))
            this@KotlinActivity.finish()
        }


    }
}
