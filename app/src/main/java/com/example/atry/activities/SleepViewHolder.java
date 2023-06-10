package com.example.atry.activities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.R;

public class SleepViewHolder extends RecyclerView.ViewHolder {
    TextView sleep_date;
    TextView start;
    TextView end;
    View view;


    public SleepViewHolder(View itemView) {
        super(itemView);
        sleep_date = (TextView)itemView.findViewById(R.id.sleep_date);
        start = (TextView)itemView.findViewById(R.id.start);
        end = (TextView)itemView.findViewById(R.id.end);
        view  = itemView;
    }
}
