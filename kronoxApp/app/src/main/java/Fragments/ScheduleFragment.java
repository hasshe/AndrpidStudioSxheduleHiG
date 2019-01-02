package Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.barankazan.kronoxapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Parser.ICalDataParser;
import Parser.ScheduleInfo;

/**
 * Fragment som hanterar vad för data läggs till i vilken view-element
 */
public class ScheduleFragment extends Fragment {
    private ListView scheduleList;
    private ArrayList<ScheduleInfo> lectures;
    private ListModellingAdapter adapter;
    private ArrayList<ScheduleInfo> list;
    private SimpleDateFormat dateFormat;
    private ICalDataParser parser;

    /**
     * Körs när fragment blir framkallad första gången.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule, container, false);

        initItems();
        scheduleList = mView.findViewById(R.id.schedule_list);

        scheduleList.setAdapter(adapter);
        getLectures();
        adapter.notifyDataSetChanged();
        return mView;
    }

    /**
     * Hämtar de lektioner som finns bokade
     */
    public void getLectures() {
        lectures.clear();
        for(ScheduleInfo info : list) {
            lectures.add(info);

        }
    }

    /**
     * Hämtar en lista från parsern med data från ICAL filen
     */
    public void setList() {

        list.clear();
        list.addAll(parser.getInfoList());
    }

    /**
     * Initierar hanteringen av data
     */
    public void initItems() {
        parser = new ICalDataParser();
        parser.parseICS();

        list = new ArrayList<>();

        setList();
        lectures = new ArrayList<>();
        getLectures();

        adapter = new ListModellingAdapter();
    }


    /**
     * Ändrar hur varje element i listan ser ut och vad som kan finnas i ett element i listan
     */

    class ListModellingAdapter extends BaseAdapter {
        /**
         *
         * @return antalet lektioner
         */

        @Override
        public int getCount() {

            return lectures.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        /**
         *
         * @param position var de oliks views ligger
         * @param view vad som läggs till var i listans element
         * @param parent vilken lista som ska modelleras
         * @return data som lagts till i listan
         */
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder viewHolder;
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.style_list, parent, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.startTime.setText(lectures.get(position).getStartTime());
            viewHolder.stopTime.setText(lectures.get(position).getStopTime());
            viewHolder.courseName.setText(lectures.get(position).getCourseCode());
            viewHolder.roomNr.setText(lectures.get(position).getRoomNr());
            viewHolder.teacherSignature.setText(lectures.get(position).getTeacherSignature());
            viewHolder.teacherSignature.setText(lectures.get(position).getTeacherSignature());
            viewHolder.moment.setText(lectures.get(position).getLectureDetailedInfo());
            viewHolder.date.setText(lectures.get(position).getDate());

            return view;
        }

        /**
         * hanterar vilka views som finns via dess ID
         */
        private class ViewHolder {
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
    }
}
