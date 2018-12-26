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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barankazan.kronoxapp.R;

public class ScheduleFragment extends Fragment {

    SQLiteDatabase mDatabase;
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    CoordinatorLayout coordinatorLayout;

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

        addItem();

        return v;
    }

    private void addItem(){

        String name = "Database Item 1";
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, name);

        mDatabase.insert(DatabaseHelper.DATABASE_TABLE, null, cv);
        mAdapter.swapCursor(getAllItems());
    }

    private void removeItem(long id){
        mDatabase.delete(DatabaseHelper.DATABASE_TABLE, DatabaseHelper.COLUMN_ID +"="+ id, null);
        mAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems(){
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
