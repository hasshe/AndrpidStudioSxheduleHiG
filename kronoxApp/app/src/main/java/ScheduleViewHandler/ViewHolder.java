package ScheduleViewHandler;

import android.view.View;
import android.widget.TextView;

import com.example.barankazan.kronoxapp.R;

/**
 * hanterar vilka views som finns via dess ID
 */
public class ViewHolder {
    TextView startTime, stopTime, courseName, roomNr, teacherSignature, detailedInfo, date;

    public ViewHolder(View view) {
        startTime =  view.findViewById(R.id.start);
        stopTime =  view.findViewById(R.id.stop);
        courseName =  view.findViewById(R.id.courseName);
        roomNr =  view.findViewById(R.id.room);
        teacherSignature =  view.findViewById(R.id.teacher);
        detailedInfo = view.findViewById(R.id.detailData);
        date = view.findViewById(R.id.dateField);
    }
}
