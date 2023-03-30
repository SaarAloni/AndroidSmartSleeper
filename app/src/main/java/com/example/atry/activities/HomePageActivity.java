package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import androidx.fragment.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.atry.R;
import com.example.atry.databinding.ActivityHomePageBinding;
import com.example.atry.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {
    ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.bottomNavigationView.setOnItemSelectedListener(item ->  {
            Log.d("TAG2", "This is a debug log message.");

            switch (item.getItemId()) {
                case R.id.settings_nav:
                    replaceFragment(new SettingsFragment());
                    break;
                case R.id.homePage:
                    replaceFragment(new HomePageFragment());
                    break;
            }
            return true;
        });
    }

    public void setAlarm(Context context, Calendar cal_alarm) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, PlayMusic.class);
        myIntent.setAction("start");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFrame, fragment);
        fragmentTransaction.commit();
    }

}