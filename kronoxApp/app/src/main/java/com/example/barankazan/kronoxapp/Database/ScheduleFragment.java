package com.example.barankazan.kronoxapp.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barankazan.kronoxapp.R;

public class ScheduleFragment extends Fragment {

    static SQLiteDatabase mDatabase;
    RecyclerView recyclerView;
    static RecyclerViewAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;

    /**
     * Metoden körs när fragment blir framkallad. Hämtar själva layout den behöver, skapar ny instans av
     * DatabaseHelper och RecyclerViewAdapter har inbyggd metod onSwipe som körs när användaren
     * swipar CardView till vänster eller höger.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved_schedule, container, false);

        DatabaseHelper DatabaseHelper = new DatabaseHelper(getActivity());
        mDatabase = DatabaseHelper.getWritableDatabase();

        recyclerView = v.findViewById(R.id.recyclerView);
        mAdapter = new RecyclerViewAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {

                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        coordinatorLayout = v.findViewById(R.id.coordinatorLayout);
        return v;
    }

    /**
     * Skapar ny CardViw och lägger in ny data i databasen.
     */
    public static void addItem(String name, String URL){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, name);
        cv.put(DatabaseHelper.COLUMN_URL, URL);
        Log.d("Hello2", name);
        Log.d("Hello2", URL);

        mDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, cv);
        mAdapter.swapCursor(getAllItems());
    }

    /**
     * Metoden körs när användaren swipar CardView, då ska metoden ta bort data från databasen.
     * @param id
     */
    private void removeItem(long id){
        mDatabase.delete(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.COLUMN_ID +"="+ id, null);
        mAdapter.swapCursor(getAllItems());
    }

    /**
     * Hämtar all innehåll från databasen.
     * @return
     */
    public static Cursor getAllItems(){
        return mDatabase.query(
                DatabaseHelper.DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_NAME + " ASC"
        );
    }
}
