package com.example.atry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.atry.R;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

        Executor executor = Executors.newSingleThreadExecutor();
        Button setAlarm = view.findViewById(R.id.setAlarmButton);

        CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
        CronetEngine cronetEngine = myBuilder.build();
        TextView time = view.findViewById(R.id.editTextTime);

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://192.168.1.206:5000/set_alarm?" +
                                "email=" +  s1 +
                                "&wake_time=" + time.getText().toString(),
                        new MyUrlRequestCallback(), executor);

                UrlRequest request = requestBuilder.build();
                request.start();

            }
        });

//        Date dat = new Date();
//        Calendar cal_alarm = Calendar.getInstance();
//        cal_alarm.setTime(dat);
//        cal_alarm.set(Calendar.HOUR_OF_DAY, 15);
//        cal_alarm.set(Calendar.MINUTE, 35);
//        cal_alarm.set(Calendar.SECOND, 0);
//        setAlarm(getActivity(), cal_alarm);

        return view;
    }

//    public void setAlarm(Context context, Calendar cal_alarm) {
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent myIntent = new Intent(context, PlayMusic.class);
//        myIntent.setAction("start");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
//
//        manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);
//    }

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
            if (StandardCharsets.UTF_8.decode(byteBuffer).toString().contains("ok")){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
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