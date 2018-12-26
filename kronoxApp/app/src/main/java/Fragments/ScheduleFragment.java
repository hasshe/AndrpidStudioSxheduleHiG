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
import java.util.Calendar;

import Controllers.ICalParser;
import Controllers.InfoHandler;

public class ScheduleFragment extends Fragment {
    private ListView scheduleList;
    private ArrayList<InfoHandler> lecturesOfTheDay;
    private MyAdapter adapter;
    private ArrayList<InfoHandler> list;
    private Calendar date;
    private SimpleDateFormat sdf;
    private ICalParser parser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_schedule, container, false);

        initItems();
        scheduleList = mView.findViewById(R.id.schedule_list);

        setAdapter();
        scheduleList.setAdapter(adapter);
        getTodaysLectures();
        adapter.notifyDataSetChanged();
        return mView;
    }

    /**
     * getTodaysLectures() tar de lektioner som matchar det valda datumet hos kalendern och lägger
     * in dom i lecturesOfTheDay-listan.
     */
    public void getTodaysLectures() {
        lecturesOfTheDay.clear();
        for(InfoHandler info : list) {
            lecturesOfTheDay.add(info);

        }
    }

    /**
     * setList() hämtar en lista med informationen som kommer från ICAL-filen, ifrån ICalParsern.
     */
    public void setList() {
        list.clear();
        list.addAll(parser.getInfoList());
    }

    public void initItems() {
        parser = new ICalParser();
        parser.parseICS();

        sdf = new SimpleDateFormat("yyyyMMdd");
        date = Calendar.getInstance();
        list = new ArrayList<>();

        setList();
        lecturesOfTheDay = new ArrayList<>();
        getTodaysLectures();
    }

    public void setAdapter() {

        adapter = new MyAdapter();
    }

    /**
     * MyAdapter ändrar så att listan ser ut som den gör, istället för att se ut som en vanlig lista.
     */

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return lecturesOfTheDay.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

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

            viewHolder.startTime.setText(lecturesOfTheDay.get(position).getStartTime());
            viewHolder.stopTime.setText(lecturesOfTheDay.get(position).getStopTime());
            viewHolder.courseName.setText(lecturesOfTheDay.get(position).getCourseCode());
            viewHolder.roomNr.setText(lecturesOfTheDay.get(position).getRoomNr());
            viewHolder.teacherSignature.setText(lecturesOfTheDay.get(position).getTeacherSignature());
            viewHolder.teacherSignature.setText(lecturesOfTheDay.get(position).getTeacherSignature());
            viewHolder.moment.setText(lecturesOfTheDay.get(position).getLectureMoment());
            viewHolder.date.setText(lecturesOfTheDay.get(position).getDate());
            return view;
        }

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
