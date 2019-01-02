package com.example.barankazan.kronoxapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Laddningsskärm som anropas vid öppning av ett schema
 */
public class LoadingScreen extends AppCompatActivity {

    /**
     * Anropas när ett schema väljs. Laddningsskärmen påbörjar och skapar en delay på 2.5 sekunder
     * för att tillåta schemat att laddas färdigt
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                showSchedule();
            }
        }, 2500);
    }

    /**
     * Ny instans av intent som kör igång ScheduleActivity
     */
    public void showSchedule() {
        Intent intent = new Intent(LoadingScreen.this, ScheduleActivity.class);
        startActivity(intent);
        this.finish();
    }
}
