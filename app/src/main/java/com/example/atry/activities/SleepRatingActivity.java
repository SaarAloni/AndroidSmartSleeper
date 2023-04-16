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
                        "http://192.168.1.206:5000/add_rating?" +
                                "email=" + email +
                                "&rate=" + ratingBar.getRating() +
                                "&sleep_id=1", //TODO get sleep_id
                        myUrlRequestCallback, executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });
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
            //Log.i(TAG, StandardCharsets.UTF_8.decode(byteBuffer).toString());
            if (StandardCharsets.UTF_8.decode(byteBuffer).toString().contains("ok")){
                Intent intent = new Intent(SleepRatingActivity.this, HomePageActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                EditText email = findViewById(R.id.editTextEmail);

                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("email", email.getText().toString());
                myEdit.apply();

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