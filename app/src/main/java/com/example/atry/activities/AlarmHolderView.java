package com.example.atry.activities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.R;

public class AlarmHolderView extends RecyclerView.ViewHolder {
    TextView day;
    TextView action;
    TextView hour;
    TextView date;
    View view;


    public AlarmHolderView(View itemView) {
        super(itemView);
        day = (TextView)itemView.findViewById(R.id.day);
        hour = (TextView)itemView.findViewById(R.id.hour);
        action = (TextView)itemView.findViewById(R.id.action);
        date = (TextView)itemView.findViewById(R.id.date);
        view  = itemView;
    }
}
