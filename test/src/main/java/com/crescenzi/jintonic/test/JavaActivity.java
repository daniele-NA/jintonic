package com.crescenzi.jintonic.test;


import static com.crescenzi.jintonic.test.KotlinActivityKt.LOG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.crescenzi.jintonic.project.internet.RequireInternet;

@SuppressLint("SetTextI18n")
public class JavaActivity extends AppCompatActivity {

    @RequireInternet
    void javaApiCall(){
        LOG("Java Api Call done successfully");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);

        TextView activityTitle = findViewById(R.id.activityTitle);
        Button apiButton = findViewById(R.id.apiButton);
        Button activityButton = findViewById(R.id.activityButton);
        activityTitle.setText("JAVA Activity");
        var context = this;

        apiButton.setOnClickListener(v -> {
            try {
                javaApiCall();
                Toast.makeText(context, "Post api call", Toast.LENGTH_SHORT).show();

                activityTitle.setText("Alright");
                activityTitle.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
            } catch (Exception e) {
                activityTitle.setText("Exception");
                activityTitle.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
                Toast.makeText(context, "Inner catch", Toast.LENGTH_SHORT).show();
            }
        });

        activityButton.setOnClickListener(v -> {
            startActivity(new Intent(context, KotlinActivity.class));
            context.finish();
        });

    }
}