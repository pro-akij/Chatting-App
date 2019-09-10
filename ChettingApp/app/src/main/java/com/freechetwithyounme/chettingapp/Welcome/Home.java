package com.freechetwithyounme.chettingapp.Welcome;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.freechetwithyounme.chettingapp.MainActivity;
import com.freechetwithyounme.chettingapp.R;

public class Home extends AppCompatActivity {

    private static int SPLASH_TIME= 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(Home.this, MainActivity.class);
                startActivity(intent);
            }
        },SPLASH_TIME);
    }
}
