package com.example.atry.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.R;

import java.util.Collections;
import java.util.List;

public class AlarmHolder extends RecyclerView.Adapter<AlarmHolderView> {

    List<alarmData> list
            = Collections.emptyList();

    Context context;

    public AlarmHolder(List<alarmData> list,
                       Context context)
    {
        this.list = list;
        this.context = context;

    }

    @Override
    public AlarmHolderView onCreateViewHolder(ViewGroup parent,
                       int viewType)
    {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.alarm_view,
                        parent, false);

        AlarmHolderView viewHolder
                = new AlarmHolderView(photoView);
        return viewHolder;
    }

//    @NonNull
//    @Override
//    public AlarmHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }

//    @Override
    public void
    onBindViewHolder(final AlarmHolderView viewHolder,
                     final int position)
    {
//        final int index = viewHolder.getAdapterPosition();
        viewHolder.day.setText(list.get(position).day);
        viewHolder.date.setText(list.get(position).date);
        viewHolder.hour.setText(list.get(position).hour);
        viewHolder.action.setText(list.get(position).action);
//        viewHolder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                listiner.click(index);
//            }
//        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
