package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class SleepRatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_rating);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        Button rate = findViewById(R.id.rate_button);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String email = sh.getString("email", "");

        CronetEngine.Builder myBuilder = new CronetEngine.Builder(this);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUrlRequestCallback myUrlRequestCallback = new MyUrlRequestCallback();
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://"+getString(R.string.ip)+":5000/add_rating?" +
                                "email=" + email +
                                "&rate=" + ratingBar.getRating(),
                        myUrlRequestCallback, executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "http://" + getString(R.string.ip) + ":5000/get_last_sleep?" +
                        "email=" + email,
                new MyUrlRequestCallback(), executor);

        UrlRequest request = requestBuilder.build();
        request.start();

    }

    public class MyUrlRequestCallback extends UrlRequest.Callback {
        private static final String TAG = "MyUrlRequestCallback";
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
            Log.i("TAG", res);
            if (res.contains("ok")){
                Intent intent = new Intent(SleepRatingActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
            if (res.contains("&")){
                String[] s = res.split("&");
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        TextView text = findViewById(R.id.textView);
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