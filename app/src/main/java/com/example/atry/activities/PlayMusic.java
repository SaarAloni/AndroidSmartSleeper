package com.example.atry.activities;

import static android.app.PendingIntent.FLAG_MUTABLE;
import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.atry.R;

import java.util.Timer;
import java.util.TimerTask;


public class PlayMusic extends BroadcastReceiver {
    public MediaPlayer mPlayer = null;
    @Override
    public void onReceive(Context context, Intent intent) {
                if("start".equals(intent.getAction())) {
//                    Intent rateIntent = new Intent(context, LoginActivity.class);
//                    rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("rise", "rise");
//                    context.startActivity(rateIntent);
                    MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.music);
                    mPlayer.start();
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                    String value = intent.getStringExtra("time");
                    int v = Integer.parseInt(value);
                    Handler mHandler=new Handler();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <= 10; i++) {
                                final int j = i;
                                try {
                                    Thread.sleep(v/10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //Update the value background thread to UI thread
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(j == 10) {
                                            //Toast.makeText(context, "stopped", Toast.LENGTH_SHORT).show();
                                            mPlayer.stop();
                                        }
                                        //Toast.makeText(context, String.valueOf(currentProgressCount), Toast.LENGTH_SHORT).show();
                                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                                    }
                                });
                            }
                        }
                    }).start();
                }

    }

//    Date dat = new Date();
//    Calendar cal_alarm = Calendar.getInstance();
//        cal_alarm.setTime(dat);
//        cal_alarm.set(Calendar.HOUR_OF_DAY,19);
//        cal_alarm.set(Calendar.MINUTE,37);
//        cal_alarm.set(Calendar.SECOND,0);
//    setAlarm(this, cal_alarm);
//
//    public void setAlarm(Context context, Calendar cal_alarm) {
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent myIntent = new Intent(context, PlayMusic.class);
//        myIntent.setAction("start");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
//
//        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
//    }

}

