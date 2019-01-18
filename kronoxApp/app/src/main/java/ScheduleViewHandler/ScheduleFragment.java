package ScheduleViewHandler;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.barankazan.kronoxapp.R;

import java.util.ArrayList;

import ParserAndModel.ICalDataParser;
import ParserAndModel.ScheduleInfo;

/**
 * Fragment som hanterar vad för data läggs till i vilket view-element
 */
public class ScheduleFragment extends Fragment {
    private ListView scheduleList;
    private ArrayList<ScheduleInfo> lectures;
    private ListModellingAdapter adapter;
    private ArrayList<ScheduleInfo> list;
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
        scheduleList = mView.findViewById(R.id.listSchedule);

        scheduleList.setAdapter(adapter);
        getLectures();
        adapter.notifyDataSetChanged();
        return mView;
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
    }
    /**
     * Hämtar en lista från parsern med data från ICAL filen
     */
    public void setList() {

        list.clear();
        list.addAll(parser.getScheduleInfoList());
    }
    /**
     * Hämtar de lektioner som finns bokade
     */
    public void getLectures() {
        lectures.clear();
        for(ScheduleInfo lectures : list) {
            this.lectures.add(lectures);

        }
    }
    /**
     * Initierar tolkning av data i parser klassen
     */
    public void initItems() {
        parser = new ICalDataParser();
        parser.parseICS();
        initList();
    }

    /**
     * initierar en ny arraylist med data från parsern
     */
    public void initList() {
    list = new ArrayList<>();

    setList();
    initLectures();
}

    /**
     * initierar en arraylist av de lektioner som hittades
     */
    public void initLectures() {

        lectures = new ArrayList<>();
        getLectures();
        initAdapter();
    }

    /**
     * initierar modelleringen av listan
     */
    public void initAdapter() {
        adapter = new ListModellingAdapter();
    }
}
