package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.atry.R;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = findViewById(R.id.register);
        EditText email = findViewById(R.id.editTextTextEmail);
        EditText birthday = findViewById(R.id.editTextBithday);
        RadioButton male = findViewById(R.id.male);
        RadioButton female = findViewById(R.id.female);
        EditText height = findViewById(R.id.editTextNumberHeight);
        EditText weight = findViewById(R.id.editTextNumberWeight);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sharedPreferences.getString("email", "");
        email.setText(s1);



        CronetEngine.Builder myBuilder = new CronetEngine.Builder(this);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String transform_gender;
                if (male.isChecked()) {
                    transform_gender = "1";
                } else {
                    transform_gender = "0";
                }
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://"+getString(R.string.ip)+":5000/register?" +
                                "email=" +  email.getText() +
                                "&birthday=" + birthday.getText() +
                                "&gender=" + transform_gender +
                                "&height=" + height.getText() +
                                "&weight=" + weight.getText() +
                                "&password=" + "111",
                        new MyUrlRequestCallback(), executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });
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
            if (StandardCharsets.UTF_8.decode(byteBuffer).toString().contains("ok")){
                Intent intent = new Intent(context, HomePageActivity.class);
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

