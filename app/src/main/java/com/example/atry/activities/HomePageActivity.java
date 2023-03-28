package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import com.example.atry.R;

import java.util.Calendar;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        TextView welcome = findViewById(R.id.textViewWelcome);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("email", "");
        welcome.setText("Welcome " + s1);
            Date dat = new Date();
            Calendar cal_alarm = Calendar.getInstance();
                cal_alarm.setTime(dat);
                cal_alarm.set(Calendar.HOUR_OF_DAY,15);
                cal_alarm.set(Calendar.MINUTE,35);
                cal_alarm.set(Calendar.SECOND,0);
            setAlarm(this, cal_alarm);
    }

    public void setAlarm(Context context, Calendar cal_alarm) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, PlayMusic.class);
        myIntent.setAction("start");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }

}