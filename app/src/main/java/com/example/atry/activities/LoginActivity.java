package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.atry.R;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.buttonLogin);
        Button register = findViewById(R.id.buttonRegister);
        EditText password = findViewById(R.id.editTextPassword);
        EditText email = findViewById(R.id.editTextEmail);
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(this);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUrlRequestCallback myUrlRequestCallback = new MyUrlRequestCallback();
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://192.168.1.206:5000/login?" +
                                "email=" +  email.getText() +
                                "&password=" + password.getText(),
                        myUrlRequestCallback, executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
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