package com.example.atry.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import com.example.atry.R;


public class PlayMusic extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
                if("start".equals(intent.getAction())) {
                    MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.music);
                    mPlayer.start();
                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                        }
                    }, 10000);
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

