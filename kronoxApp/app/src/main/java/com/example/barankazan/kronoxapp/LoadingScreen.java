package com.example.barankazan.kronoxapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import Fragments.*;

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
        }, 1500);


    }
    public void goToSchedule() {
        Intent intent = new Intent(LoadingScreen.this, ScheduleActivity.class);
        startActivity(intent);
        this.finish();
    }
}
