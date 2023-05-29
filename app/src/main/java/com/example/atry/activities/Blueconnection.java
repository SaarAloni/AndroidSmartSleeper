package com.example.atry.activities;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.media.MediaPlayer;

import java.util.List;

public class Blueconnection {
    private BluetoothGatt mGatt;
    private List<BluetoothGattService> services;
    private MediaPlayer mPlayer;
    private static final Blueconnection instance = new Blueconnection();

    // private constructor to avoid client applications using the constructor
    private Blueconnection(){}

    public static Blueconnection getInstance() {
        return instance;
    }

    public void setmGatt(BluetoothGatt mGatt) {
        this.mGatt = mGatt;
    }

    public void setmservice(List<BluetoothGattService> services) {
        this.services = services;
    }

    public void setPlayer(MediaPlayer mPlayer) {
        this.mPlayer = mPlayer;
    }

    public MediaPlayer getPlayer() {
        return  this.mPlayer;
    }

    public BluetoothGatt getmGatt() {
        return  this.mGatt;
    }

    public List<BluetoothGattService> getservices( ) {
        return this.services ;
    }
}
