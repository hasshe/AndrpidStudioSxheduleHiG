package com.example.barankazan.kronoxapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barankazan.kronoxapp.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout parentLayout;
        public TextView nameText;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.txtName);
            parentLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    public RecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        final String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        final long id = mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.COLUMN_ID));

        holder.nameText.setText(name);
        holder.itemView.setTag(id);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

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

