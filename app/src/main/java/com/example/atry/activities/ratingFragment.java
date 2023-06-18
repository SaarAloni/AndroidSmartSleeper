package com.example.atry.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atry.R;

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

public class ratingFragment extends Fragment {
    private List<SleepData> list = new ArrayList<>();
    SleepHolder adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sleep_rating, container, false);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String s1 = sh.getString("email", "");


        Executor executor = Executors.newSingleThreadExecutor();
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(getContext());
        CronetEngine cronetEngine = myBuilder.build();

        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "http://" + getString(R.string.ip) + ":5000/get_last_sleep?" +
                        "email=" + s1,
                new MyUrlRequestCallback(), executor);

        UrlRequest request = requestBuilder.build();
        request.start();
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button rate = view.findViewById(R.id.rate_button);


        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUrlRequestCallback myUrlRequestCallback = new MyUrlRequestCallback();
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://"+getString(R.string.ip)+":5000/add_rating?" +
                                "email=" + s1 +
                                "&rate=" + ratingBar.getRating(),
                        myUrlRequestCallback, executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });





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

            if (StandardCharsets.UTF_8.decode(byteBuffer).toString().contains("ok")){
                Intent intent = new Intent(getContext(), HomePageActivity.class);
                startActivity(intent);
            }
            if (StandardCharsets.UTF_8.decode(byteBuffer).toString().contains("&")){
                int pos = 0;
                String[] s = res.split("&");
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        TextView text = getView().findViewById(R.id.textView);
                        String[] ss = s[0].split(",");
                        text.setText("What do you think about the last sleep \n" +
                                "from: " + ss[0] + "\n" +
                                "to: " +ss[1]);

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
