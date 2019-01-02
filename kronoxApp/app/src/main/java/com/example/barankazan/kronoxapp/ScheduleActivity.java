package com.example.barankazan.kronoxapp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Aktivitet som endast består av schemats utseende
 */
public class ScheduleActivity extends Activity {

    /**
     * Körs när activity blir andropad
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
    }
}
