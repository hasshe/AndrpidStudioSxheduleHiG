package com.pratap.calendarview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pratap.calendarview.Utils.Utils;
import com.pratap.calendarview.models.CalendarViewEvent;
import com.pratap.calendarview.views.DatePickerView;
import com.pratap.calendarview.views.TimePickerView;

/**
 * Created by pratap.kesaboyina on 28-04-2016.
 */
public class AddNewEventActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnSave;
    private EditText title;
    private DatePickerView startDate;
    private TimePickerView startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewevent);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        //  actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24px);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Setting default toolbar title to empty
        actionBar.setTitle("Add New Event");


        initControls();

    }

    private void initControls() {

        title = (EditText) findViewById(R.id.title);
        startDate = (DatePickerView) findViewById(R.id.startDate);
        startTime = (TimePickerView) findViewById(R.id.startTime);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarViewEvent singleEvent = new CalendarViewEvent();
                singleEvent.setName(title.getText().toString());
                singleEvent.setStartTime(startDate.getModifiedDate());
                singleEvent.setId(startTime.getModifiedTime().getTimeInMillis());

                Log.i("event", singleEvent.toString());
                Utils.getDateString(startDate.getModifiedDate());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Event", singleEvent);
                setResult(RESULT_OK, resultIntent);
                finish();



            }
        });
    }
}
