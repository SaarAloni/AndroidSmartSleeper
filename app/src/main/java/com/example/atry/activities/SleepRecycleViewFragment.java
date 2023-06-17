package com.example.atry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SleepRecycleViewFragment extends Fragment {
    private List<SleepData> list = new ArrayList<>();
    SleepHolder adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sleep_recycle, container, false);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String s1 = sh.getString("email", "");

        RecyclerView recyclerView = view.findViewById(R.id.sleeps);

        Executor executor = Executors.newSingleThreadExecutor();
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
        CronetEngine cronetEngine = myBuilder.build();

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "http://" + getString(R.string.ip) + ":5000/get_all_sleeps?" +
                        "email=" + s1,
                new MyUrlRequestCallback(), executor);

        UrlRequest request = requestBuilder.build();
        request.start();


//        list = getData();

        adapter = new SleepHolder(list, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        }






        return view;
    }


    private List<SleepData> setData(String sleep_date, String start, String end, String quality)
    {
        list.add(new SleepData(sleep_date,
                start,
                end,
                quality));


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
            String[] s = res.split("&");
            String first = s[0];
            s = Arrays.copyOfRange(s, 1, s.length);
            for (String result : s) {
                Log.d("tag", result);

                String[] tmp = result.split(",");
                if (tmp.length == 1) {
                    continue;
                }
                list.add(pos,new SleepData(tmp[0],
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

                        TextView average_hours_of_sleep = getView().findViewById(R.id.average_hours_of_sleep);
                        TextView average_sleep_quality = getView().findViewById(R.id.average_sleep_quality);
                        TextView average_hours_of_sleep_last_7 = getView().findViewById(R.id.average_hours_of_sleep_last_7);
                        TextView average_sleep_quality_last_7 = getView().findViewById(R.id.average_sleep_quality_last_7);

                        String [] tmp = first.split(",");
                        average_sleep_quality.setText(tmp[0]);
                        average_hours_of_sleep.setText(tmp[1]);
                        average_sleep_quality_last_7.setText(tmp[2]);
                        average_hours_of_sleep_last_7.setText(tmp[3]);

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
