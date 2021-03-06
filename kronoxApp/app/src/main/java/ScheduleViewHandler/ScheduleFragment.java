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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import NavigationAndView.ScheduleActivity;
import ParserAndModel.ICalDataParser;
import ParserAndModel.ScheduleInfo;

/**
 * Fragment som hanterar vad för data läggs till i vilket view-objekt samt kopplar till en adapter
 */
public class ScheduleFragment extends Fragment {
    private ListView scheduleList;
    private ListModellingAdapter adapter;
    private ArrayList<ScheduleInfo> scheduleInfoList;
    private ICalDataParser parser;
    protected ScheduleActivity sa;
    private View mView;
    /**
     * Körs när fragment blir framkallad första gången.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_schedule, container, false);

        try {
            initiations();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scheduleList = mView.findViewById(R.id.listSchedule);

        scheduleList.setAdapter(adapter);
        setList();
        adapter.notifyDataSetChanged();
        return mView;
    }

    /**
     * Ändrar hur varje element i listan ser ut och vad som kan finnas i ett element i listan
     */

    protected class ListModellingAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ViewHolder viewHolder;
        /**
         *
         * @return antalet lektioner
         */

        @Override
        public int getCount() {

            return scheduleInfoList.size();
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
            if(view == null) {
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.style_list, parent, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.startTime.setText(scheduleInfoList.get(position).getStartTime());
            viewHolder.stopTime.setText(scheduleInfoList.get(position).getStopTime());
            viewHolder.courseName.setText(scheduleInfoList.get(position).getCourseCode());
            viewHolder.roomNr.setText(scheduleInfoList.get(position).getRoomNr());
            viewHolder.teacherSignature.setText(scheduleInfoList.get(position).getTeacherSignature());
            viewHolder.teacherSignature.setText(scheduleInfoList.get(position).getTeacherSignature());
            viewHolder.detailedInfo.setText(scheduleInfoList.get(position).getLectureDetailedInfo());
            viewHolder.date.setText(scheduleInfoList.get(position).getDate());

            return view;
        }
    }
    /**
     * Hämtar en lista från parsern med hanterat data från ICAL filen
     */
    public void setList() {
        scheduleInfoList.clear();
        scheduleInfoList.addAll(parser.getScheduleInfoList());
        if(scheduleInfoList.size() < 1) {
            Toast.makeText(getActivity(), "No Schedule Found", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Initierar hantering av data, listan med data, lektionerna och adaptern
     */
    public void initiations() throws ParseException, IOException {
        parser = new ICalDataParser();
        parser.parseICS();

        scheduleInfoList = new ArrayList<>();
        setList();
        adapter = new ListModellingAdapter();
    }
}
