package ScheduleViewHandler;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.barankazan.kronoxapp.R;

import java.util.ArrayList;

import NavigationAndView.ScheduleActivity;
import ParserAndModel.ICalDataParser;
import ParserAndModel.ScheduleInfo;

/**
 * Fragment som hanterar vad för data läggs till i vilket view-element
 */
public class ScheduleFragment extends Fragment {
    private ListView scheduleList;
    private ArrayList<ScheduleInfo> scheduleLectures;
    private ListModellingAdapter adapter;
    private ArrayList<ScheduleInfo> scheduleInfoList;
    private ICalDataParser parser;
    protected ScheduleActivity sa;
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

        initiations();
        scheduleList = mView.findViewById(R.id.listSchedule);

        scheduleList.setAdapter(adapter);
        setLectures();
        adapter.notifyDataSetChanged();
        return mView;
    }

    /**
     * Ändrar hur varje element i listan ser ut och vad som kan finnas i ett element i listan
     */

    public class ListModellingAdapter extends BaseAdapter {
        /**
         *
         * @return antalet lektioner
         */

        @Override
        public int getCount() {

            return scheduleLectures.size();
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

            viewHolder.startTime.setText(scheduleLectures.get(position).getStartTime());
            viewHolder.stopTime.setText(scheduleLectures.get(position).getStopTime());
            viewHolder.courseName.setText(scheduleLectures.get(position).getCourseCode());
            viewHolder.roomNr.setText(scheduleLectures.get(position).getRoomNr());
            viewHolder.teacherSignature.setText(scheduleLectures.get(position).getTeacherSignature());
            viewHolder.teacherSignature.setText(scheduleLectures.get(position).getTeacherSignature());
            viewHolder.detailedInfo.setText(scheduleLectures.get(position).getLectureDetailedInfo());
            viewHolder.date.setText(scheduleLectures.get(position).getDate());

            return view;
        }
    }
    /**
     * Hämtar en lista från parsern med data från ICAL filen
     */
    public void setList() {

        scheduleInfoList.clear();
        scheduleInfoList.addAll(parser.getScheduleInfoList());
    }
    /**
     * Hämtar de lektioner som finns bokade och skickar ut en toast om inget hittas
     */
    public void setLectures() {
        scheduleLectures.clear();
        for(ScheduleInfo lectures : scheduleInfoList) {
            scheduleLectures.add(lectures);
        }
        if(scheduleLectures.size() < 1) {
            Toast.makeText(getActivity(), "No Schedule Found", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Initierar hantering av data, listan med data, lektionerna och adaptern
     */
    public void initiations() {
        parser = new ICalDataParser();
        parser.parseICS();

        scheduleInfoList = new ArrayList<>();
        setList();
        scheduleLectures = new ArrayList<>();
        setLectures();
        adapter = new ListModellingAdapter();
    }
}
