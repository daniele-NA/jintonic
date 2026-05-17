package com.crescenzi.jintonic.test;


import static com.crescenzi.jintonic.test.KotlinActivityKt.LOG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.crescenzi.jintonic.project.internet.status.RequireInternet;
import com.crescenzi.jintonic.project.security.screen.SecureWindow;

@SecureWindow
@SuppressLint("SetTextI18n")
public class JavaActivity extends AppCompatActivity {

    @RequireInternet
    public void javaApiCall(){
        LOG("Java Api Call done successfully");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);

        TextView activityTitle = findViewById(R.id.activityTitle);
        Button activityButton = findViewById(R.id.activityButton);
        activityTitle.setText("JAVA Activity");
        var context = this;
        activityButton.setOnClickListener(v -> {
            startActivity(new Intent(context, KotlinActivity.class));
            context.finish();
        });

    }
}