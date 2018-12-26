package com.pratap.calendarview.views;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.pratap.calendarview.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pratap.kesaboyina on 11-06-2015.
 */
public class TimePickerView extends TextView implements TimePickerDialog.OnTimeSetListener {


    private Calendar calendar;

    private Date date;


    public TimePickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes();
    }

    public TimePickerView(Context context) {
        super(context);
        setAttributes();
    }

    private void setAttributes() {
        setBackgroundResource(R.drawable.rounded_edittext);
        setHint("Select Time");
        setFocusable(false);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                if (date != null) {
                     calendar.setTime(date);
               }


                TimePickerDialog timePicker = new TimePickerDialog(TimePickerView.this.getContext(),
                        TimePickerView.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);


                timePicker.setCancelable(false);
                //   timePicker.setCanceledOnTouchOutside(true);


                timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            /*if (prevSelectedDate != null)
                                setTime(prevSelectedDate);*/
                        }
                    }
                });

              /*  timePicker.setButton(DialogInterface.BUTTON_NEUTRAL, "CLEAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEUTRAL) {
                            dialog.dismiss();
                            setText("");
                        }
                    }
                });*/
                timePicker.show();
            }
        });
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {



        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);



        setTime(calendar.getTime());

    }


    public void setTime(Date date) {
        if (date != null) {
            this.date = date;
            SimpleDateFormat newformat = new SimpleDateFormat("hh:mm a");
            String formattedDate = newformat.format(date);
            setText(formattedDate);

        } else {

            setText("");
        }
    }


    public Calendar getModifiedTime()
    {
        return calendar;
    }


}
