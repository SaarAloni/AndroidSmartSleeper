package com.example.atry.activities;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;


public class StopAlarm extends BroadcastReceiver {
//    List<BluetoothGattService> services;
//    private BluetoothGatt mGatt;

//    public StartLight(BluetoothGatt mGatt, List<BluetoothGattService> services) {
//        this.mGatt = mGatt;
//        this.services = services;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("stop".equals(intent.getAction())) {
            Log.d("TAG", "onReceive: stooooooooop");
            Blueconnection.getInstance().getPlayer().stop();
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
            for (BluetoothGattService service : Blueconnection.getInstance().getservices()) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(0);
                byte x = 0;
                characteristic.setValue(String.valueOf(x));
                boolean success = Blueconnection.getInstance().getmGatt().writeCharacteristic(characteristic);
            }
            Blueconnection.getInstance().getmGatt().disconnect();
        }

//        Blueconnection blueconnection = Blueconnection.getInstance();
//        if (blueconnection.getmGatt() != null) {
//            Log.d("TAG", "onReceive: start ");
//        }
//        mGatt = blueconnection.getmGatt();
//        services = blueconnection.getservices();
//
//        String value = intent.getStringExtra("time");
//        int v = Integer.parseInt(value);
//        Handler mHandler = new Handler();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 1; i <= 9; i++) {
//                    final int j = i;
//                    try {
//                        if (j == 9) {
//                            Thread.sleep(2000);
//                        }
//                        Thread.sleep(v / 9);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    //Update the value background thread to UI thread
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (j == 9) {
//                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                                    // TODO: Consider calling
//                                    //    ActivityCompat#requestPermissions
//                                    // here to request the missing permissions, and then overriding
//                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                    //                                          int[] grantResults)
//                                    // to handle the case where the user grants the permission. See the documentation
//                                    // for ActivityCompat#requestPermissions for more details.
//                                    return;
//                                }
//                                mGatt.disconnect();
//                            }
//                            if (j == 8) {
//                                //Toast.makeText(context, "stopped", Toast.LENGTH_SHORT).show();
//                                for (BluetoothGattService service : services) {
//                                    BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(0);
//                                    byte x = 0;
//                                    characteristic.setValue(String.valueOf(x));
//                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                                        // TODO: Consider calling
//                                        //    ActivityCompat#requestPermissions
//                                        // here to request the missing permissions, and then overriding
//                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                        //                                          int[] grantResults)
//                                        // to handle the case where the user grants the permission. See the documentation
//                                        // for ActivityCompat#requestPermissions for more details.
//                                        return;
//                                    }
//                                    boolean success = mGatt.writeCharacteristic(characteristic);
//
//                                }
//                            }
//                            //Toast.makeText(context, String.valueOf(currentProgressCount), Toast.LENGTH_SHORT).show();
//                            for (BluetoothGattService service : services) {
//                                BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(0);
//                                byte x = (byte) (j+1);
//                                characteristic.setValue(String.valueOf(x));
//                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                                    // TODO: Consider calling
//                                    //    ActivityCompat#requestPermissions
//                                    // here to request the missing permissions, and then overriding
//                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                    //                                          int[] grantResults)
//                                    // to handle the case where the user grants the permission. See the documentation
//                                    // for ActivityCompat#requestPermissions for more details.
//                                    return;
//                                }
//                                boolean success = mGatt.writeCharacteristic(characteristic);
//                            }
//                        }
//                    });
//                }
//            }
//        }).start();



//        if ("start".equals(intent.getAction())) {
////                    Intent rateIntent = new Intent(context, LoginActivity.class);
////                    rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    intent.putExtra("rise", "rise");
////                    context.startActivity(rateIntent);
//
////                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
//            String value = intent.getStringExtra("time");
//            int v = Integer.parseInt(value);
//            Handler mHandler = new Handler();
//            Log.d("TAG", "onReceive: start " + v);
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (int i = 1; i <= 8; i++) {
//                        final int j = i;
//                        try {
//                            Thread.sleep(v / 10);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //Update the value background thread to UI thread
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (j == 8) {
//                                    //Toast.makeText(context, "stopped", Toast.LENGTH_SHORT).show();
//
//                                }
//                                //Toast.makeText(context, String.valueOf(currentProgressCount), Toast.LENGTH_SHORT).show();
//                                for (BluetoothGattService service : services) {
//                                    BluetoothGattCharacteristic characteristic = service.getCharacteristics().get(0);
//                                    byte x = 0;
//                                    characteristic.setValue(String.valueOf(x));
//                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                                        // TODO: Consider calling
//                                        //    ActivityCompat#requestPermissions
//                                        // here to request the missing permissions, and then overriding
//                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                        //                                          int[] grantResults)
//                                        // to handle the case where the user grants the permission. See the documentation
//                                        // for ActivityCompat#requestPermissions for more details.
//                                        return;
//                                    }
//                                    boolean success = mGatt.writeCharacteristic(characteristic);
//                                }
//                            }
//                        });
//                    }
//                }
//            }).start();
//        }

    }
}

