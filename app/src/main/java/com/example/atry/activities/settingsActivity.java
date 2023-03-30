package com.example.atry.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atry.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class settingsActivity extends AppCompatActivity {

    androidx.appcompat.widget.SwitchCompat nightModeSwitch;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //getSupportActionBar().hide();

        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        if (nightMode) {
            nightModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        nightModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();


            }
        });
/*
        bottomNavigationView.setOnItemSelectedListener(item ->  {

            switch (item.getItemId()) {
                case R.id.settings_nav:
                    Intent i = new Intent(getApplicationContext(), settingsActivity.class);
                    startActivity(i);
                    break;
                case R.id.register_nav:
                    Intent i2 = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i2);
                    break;
            }
            return true;
        });

 */



    }
    /*
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.setting_title, fragment);
        fragmentTransaction.commit();
    }

     */
}