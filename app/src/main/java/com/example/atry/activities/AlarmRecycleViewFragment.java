package com.example.atry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.R;
import com.samsung.android.sdk.internal.healthdata.HealthResultReceiver;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AlarmRecycleViewFragment extends Fragment {
    private List<alarmData> list = new ArrayList<>();
    AlarmHolder adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alarm_recycle, container, false);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String s1 = sh.getString("email", "");
//        String s2 = sh.getString("data", "");
//        Log.d("TAG", "onCreateView: " + s2);
//        if (s2 != "") {

//        for (String result : s2.split("&")) {
//            String[] add_to_list = result.split(",");
//            if (add_to_list.length == 1 ){
//                continue;
//            }
//            setData(add_to_list[0],add_to_list[1],add_to_list[2],add_to_list[3]);
//        }

        RecyclerView recyclerView = view.findViewById(R.id.alarms);

        Executor executor = Executors.newSingleThreadExecutor();
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
        CronetEngine cronetEngine = myBuilder.build();

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "http://" + getString(R.string.ip) + ":5000/get_all_alarms?" +
                        "email=" + s1,
                new MyUrlRequestCallback(), executor);

        UrlRequest request = requestBuilder.build();
        request.start();


//        list = getData();

        adapter = new AlarmHolder(list, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        }






        return view;
    }

    private List<alarmData> getData()
    {
        List<alarmData> list = new ArrayList<>();
        list.add(new alarmData("First Exam",
                "May 23, 2015",
                "Best Of Luck",
                "Asdasd"));


        return list;
    }

    private List<alarmData> setData(String day, String action, String hour, String date)
    {
        list.add(new alarmData(day,
                action,
                hour,
                date));


        return list;
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

            int pos = 0;
            for (String result : res.split("&")) {
                Log.d("tag", result);

                String[] tmp = result.split(",");
                if (tmp.length == 1) {
                    continue;
                }
                list.add(pos,new alarmData(tmp[0],
                        tmp[1],
                        tmp[2],
                        tmp[3]));
                pos++;
            }

            for(int i = 0; i<pos; i++) {
                final int j =i;
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        adapter.notifyItemInserted(0);

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
