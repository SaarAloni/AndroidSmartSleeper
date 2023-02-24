package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.atry.R;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class contactActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Button but = findViewById(R.id.contactButton);
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(this);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                        "http://10.5.0.2:6000/", new MyUrlRequestCallback(), executor);

                UrlRequest request = requestBuilder.build();
                request.start();
            }
        });
    }
}