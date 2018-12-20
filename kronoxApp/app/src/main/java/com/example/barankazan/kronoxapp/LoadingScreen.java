package com.example.barankazan.kronoxapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import Fragments.*;

/**
 * Loading screen som körs i 2 sekunder för att schemat ska hinna laddas ner.
 */

public class LoadingScreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToSchedule();
            }
        }, 2000);


    }


    public void goToSchedule() {
        Intent intent = new Intent(LoadingScreen.this, ScheduleActivity.class);
        startActivity(intent);
        this.finish();
    }
}
