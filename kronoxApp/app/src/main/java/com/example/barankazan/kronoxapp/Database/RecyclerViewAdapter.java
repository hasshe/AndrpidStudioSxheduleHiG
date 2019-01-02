package com.example.barankazan.kronoxapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.barankazan.kronoxapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Klass som hanterar båda RecyclerView och CardView.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    DatabaseFragment mDatabaseFragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout parentLayout;
        public TextView nameText, todayDate;

        /**
         * Konstruktor som hittar båda TextView och RelativeLayout för att spara som global variabel.
         * @param itemView
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.txtName);
            parentLayout = itemView.findViewById(R.id.relativeLayout);
            todayDate = itemView.findViewById(R.id.dateToday);
        }
    }

    /**
     * Konstruktor som tar emot alla tre parameter och sparar som global variabel.
     * @param context
     * @param cursor
     * @param databaseFragment
     */
    public RecyclerViewAdapter(Context context, Cursor cursor, DatabaseFragment databaseFragment) {
        mContext = context;
        mCursor = cursor;
        mDatabaseFragment = databaseFragment;
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
     * Ställer in namn och ID för CardView, det innehåller även onClick metod som öppnar själva schemat när man
     * klickar på CardView.
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

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        holder.itemView.setTag(id);
        holder.nameText.setText(name);
        holder.todayDate.setText(timeStamp);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                mDatabaseFragment.openSchedule(URL);
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
