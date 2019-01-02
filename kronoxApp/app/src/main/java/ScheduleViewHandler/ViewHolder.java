package ScheduleViewHandler;

import android.view.View;
import android.widget.TextView;

import com.example.barankazan.kronoxapp.R;

/**
 * hanterar vilka views som finns via dess ID
 */
class ViewHolder {
    TextView startTime, stopTime, courseName, roomNr, teacherSignature, moment, date;

    public ViewHolder(View view) {
        startTime =  view.findViewById(R.id.start_time);
        stopTime =  view.findViewById(R.id.stop_time);
        courseName =  view.findViewById(R.id.course_name);
        roomNr =  view.findViewById(R.id.room);
        teacherSignature =  view.findViewById(R.id.teacher);
        moment = view.findViewById(R.id.moment);
        date = view.findViewById(R.id.dateField);
    }
}
