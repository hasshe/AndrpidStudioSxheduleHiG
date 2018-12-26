package com.pratap.calendarview.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by pratap.kesaboyina on 28-04-2016.
 */
public class Utils {


    public static String getDateString(Calendar calendar) {
        //   Calendar c = Calendar.getInstance(Locale.getDefault());

        // MMM d yyyy HH:mm
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        String formattedDate = df.format(calendar.getTime());

        Log.i("Date :", formattedDate);

        return formattedDate;

    }
}
