package com.example.atry.activities;

import static android.app.PendingIntent.FLAG_MUTABLE;
import static android.content.Context.ALARM_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.atry.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlayMusic extends BroadcastReceiver {
    public MediaPlayer mPlayer = null;

    List<BluetoothGattService> services;
    private BluetoothGatt mGatt;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("start".equals(intent.getAction())) {
            Blueconnection blueconnection = Blueconnection.getInstance();

            mGatt = blueconnection.getmGatt();
            services = blueconnection.getservices();
//                    Intent rateIntent = new Intent(context, LoginActivity.class);
//                    rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("rise", "rise");
//                    context.startActivity(rateIntent);
            MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.music);
            mPlayer.start();
            blueconnection.setPlayer(mPlayer);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            String value = intent.getStringExtra("time");
            int v = Integer.parseInt(value);
            Handler mHandler = new Handler();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i <= 9; i++) {
                        final int j = i;
                        try {
                            if (j == 9) {
                                Thread.sleep(2000);
                            }
                            Thread.sleep(v / 9);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //Update the value background thread to UI thread
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                        if (j == 9) {
                                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
//                                            mGatt.disconnect();
                                        }
                                        if (j == 8) {
                                            //Toast.makeText(context, "stopped", Toast.LENGTH_SHORT).show();
                                            for (BluetoothGattService service : services) {
                                                BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(0);
                                                byte x = 0;
                                                characteristic.setValue(String.valueOf(x));
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    ActivityCompat#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for ActivityCompat#requestPermissions for more details.
                                                    return;
                                                }
                                                boolean success = mGatt.writeCharacteristic(characteristic);

                                            }
//                                            mPlayer.stop();
//                                            blueconnection.getPlayer().stop();
                                        }
                                        //Toast.makeText(context, String.valueOf(currentProgressCount), Toast.LENGTH_SHORT).show();
                                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                                        for (BluetoothGattService service : services) {
                                            BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(0);
                                            byte x = (byte) (j+1);
                                            characteristic.setValue(String.valueOf(x));
                                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
                                            boolean success = mGatt.writeCharacteristic(characteristic);
                                        }
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

