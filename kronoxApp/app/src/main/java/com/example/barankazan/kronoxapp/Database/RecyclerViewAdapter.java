package com.example.barankazan.kronoxapp.Database;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barankazan.kronoxapp.LoadingScreen;
import com.example.barankazan.kronoxapp.R;

import java.io.File;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    ScheduleFragment mScheduleFragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout parentLayout;
        public TextView nameText;

        /**
         * Konstruktor som hittar båda TextView och RelativeLayout för att spara som global variabel.
         * @param itemView
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.txtName);
            parentLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    /**
     * Konstruktor som tar emot alla tre parameter och sparar som global variabel.
     * @param context
     * @param cursor
     * @param scheduleFragment
     */
    public RecyclerViewAdapter(Context context, Cursor cursor, ScheduleFragment scheduleFragment) {
        mContext = context;
        mCursor = cursor;
        mScheduleFragment = scheduleFragment;
    }

    /**
     * Skapar ny CardView.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * Ställer in namn och ID för CardView, det innehåller även onClick metod som reagerar när en av
     * CardView är inklickad.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        final long id = mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
        final String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        final String URL = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_URL));

        holder.itemView.setTag(id);
        holder.nameText.setText(name);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                mScheduleFragment.openSchedule(URL);
            }
        });
    }

    /**
     * Hämtar antal förekomst det finns från databasen.
     * @return
     */
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Byter cursor så fort när ändringar har sket i databasen T.ex. ny data blir lagrad eller bortagen.
     * @param newCursor
     */
    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null){
            notifyDataSetChanged();
        }
    }
}
