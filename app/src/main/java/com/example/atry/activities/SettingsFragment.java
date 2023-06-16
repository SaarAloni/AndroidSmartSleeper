package com.example.atry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;


import com.example.atry.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private SwitchCompat nightModeSwitch;
    private boolean nightMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "This is a debug log message.");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        nightModeSwitch = view.findViewById(R.id.nightModeSwitch);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        sharedPreferences = getActivity().getSharedPreferences("Mode", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        Button edit = view.findViewById(R.id.edit);

        if (nightMode) {
            nightModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        RadioButton rd = view.findViewById(R.id.radio_music1);
        RadioButton rd2 = view.findViewById(R.id.radio_music2);


        String music = sh.getString("music","music");

        if (music.equals("music")) {
            rd.setChecked(true);
        }
        if (music.equals("music2")) {
            rd2.setChecked(true);
        }



        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sh.edit();
                editor.putString("music", "music");
                editor.apply();
            }
        });
        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sh.edit();
                editor.putString("music", "music2");
                editor.apply();
            }
        });

        String s1 = sh.getString("music", "");
        if (s1 == "") {
            editor = sh.edit();
            editor.putString("music", "music");
            editor.apply();
        }

        String email = sh.getString("email", "");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executor executor = Executors.newSingleThreadExecutor();
                CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
                CronetEngine cronetEngine = myBuilder.build();
                EditText bd = getView().findViewById(R.id.birthday);
                EditText ge = getView().findViewById(R.id.gender);
                EditText we = getView().findViewById(R.id.weight);
                EditText he = getView().findViewById(R.id.height);

                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://" + getString(R.string.ip) + ":5000/set_setting?" +
                                "email=" + email +
                                "&gender=" + ge.getText() +
                                "&birthday=" + bd.getText() +
                                "&weight=" + we.getText() +
                                "&height=" + he.getText(),
                        new MyUrlRequestCallback(), executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });

        Executor executor = Executors.newSingleThreadExecutor();
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
        CronetEngine cronetEngine = myBuilder.build();

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "http://" + getString(R.string.ip) + ":5000/get_setting?" +
                        "email=" + email,
                new MyUrlRequestCallback(), executor);

        UrlRequest request = requestBuilder.build();
        request.start();


        nightModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();


            }
        });
        return view;
    }

    public class MyUrlRequestCallback extends UrlRequest.Callback {
        private static final String TAG = "MyUrlRequestCallback";
        private Context context = null;

        @Override
        public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
            Log.i(TAG, "onRedirectReceived method called.");
            // You should call the request.followRedirect() method to continue
            // processing the request.
            request.followRedirect();
        }

        @Override
        public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onResponseStarted method called.");
            // You should call the request.read() method before the request can be
            // further processed. The following instruction provides a ByteBuffer object
            // with a capacity of 102400 bytes for the read() method. The same buffer
            // with data is passed to the onReadCompleted() method.
            request.read(ByteBuffer.allocateDirect(102400));
        }

        @Override
        public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
            Log.i(TAG, "onReadCompleted method called.");
            // You should keep reading the request until there's no more data.
            byteBuffer.clear();
            request.read(byteBuffer);
            String res = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            Log.d("tag", res);
            String tmp = res.split("[,]", 0)[0];
            Log.d(TAG, "onReadCompleted: " + tmp);
            if (res.contains("&")) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        EditText bd = getView().findViewById(R.id.birthday);
                        EditText ge = getView().findViewById(R.id.gender);
                        EditText we = getView().findViewById(R.id.weight);
                        EditText he = getView().findViewById(R.id.height);
                        String [] resu = res.split("&");
                        bd.setText(resu[0]);
                        ge.setText(resu[1]);
                        we.setText(resu[2]);
                        he.setText(resu[3]);

                    }
                });
            }
        }

        @Override
        public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
            Log.i(TAG, "onSucceeded method called.");
        }

        @Override
        public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
            // The request has failed. If possible, handle the error.
            Log.e(TAG, "The request failed.", error);
        }

    }
}