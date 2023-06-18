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
import android.widget.TextView;


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


public class GetWakeUpTime extends Fragment {
    private SwitchCompat nightModeSwitch;
    private boolean nightMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "This is a debug log message.");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmant_wake_time, container, false);


        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        Button wake1 = view.findViewById(R.id.getWakeTime);
        EditText time_to_sleep = getActivity().findViewById(R.id.editTextSleepTime);
        TextView result_text = getActivity().findViewById(R.id.textResultTime);

        Button rate = view.findViewById(R.id.rateButton);

        String email = sh.getString("email", "");

        wake1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executor executor = Executors.newSingleThreadExecutor();
                CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
                CronetEngine cronetEngine = myBuilder.build();

                EditText time_to_sleep = getActivity().findViewById(R.id.editTextSleepTime);


                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://" + getString(R.string.ip) + ":5000/get_when_to_wake_up?" +
                                "email=" + email +
                                "&sleep_time=" + time_to_sleep.getText(),
                        new MyUrlRequestCallback(), executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SleepRatingActivity.class);
                startActivity(intent);
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
            if (res.contains(",")) {

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        TextView result_time = getView().findViewById(R.id.textResultTime);
                        result_time.setText(tmp);

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