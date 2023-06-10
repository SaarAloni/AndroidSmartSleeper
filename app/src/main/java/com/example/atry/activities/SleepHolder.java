package com.example.atry.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.R;

import java.util.Collections;
import java.util.List;

public class SleepHolder extends RecyclerView.Adapter<SleepViewHolder> {

    List<SleepData> list
            = Collections.emptyList();

    Context context;

    public SleepHolder(List<SleepData> list,
                       Context context)
    {
        this.list = list;
        this.context = context;

    }

    @Override
    public SleepViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType)
    {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.sleep_view,
                        parent, false);

        SleepViewHolder viewHolder
                = new SleepViewHolder(photoView);
        return viewHolder;
    }


    //    @Override
    public void
    onBindViewHolder(final SleepViewHolder viewHolder,
                     final int position)
    {
//        final int index = viewHolder.getAdapterPosition();
        viewHolder.sleep_date.setText(list.get(position).sleep_date);
        viewHolder.start.setText(list.get(position).start);
        viewHolder.end.setText(list.get(position).end);
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
