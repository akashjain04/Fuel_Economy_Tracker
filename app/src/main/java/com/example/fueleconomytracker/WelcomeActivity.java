package com.example.fueleconomytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/*
    This activity is a splash screen designed to display welcome for sometime and then directly go to Home page.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Hide Action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //To display the activity for 1 second and then go to Home page.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainWindow = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(mainWindow);
                finish();
            }
        }, 1000);
    }
}