package com.example.atry.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.atry.R;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {


    private TextView welcomeTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("TAG", "This is a debug log message.");
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        welcomeTextView = view.findViewById(R.id.textViewWelcome);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String s1 = sh.getString("email", "");
        welcomeTextView.setText("Welcome " + s1);

        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, 15);
        cal_alarm.set(Calendar.MINUTE, 35);
        cal_alarm.set(Calendar.SECOND, 0);
        setAlarm(getActivity(), cal_alarm);

        return view;
    }

    public void setAlarm(Context context, Calendar cal_alarm) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, PlayMusic.class);
        myIntent.setAction("start");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }
}