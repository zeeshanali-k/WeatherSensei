package com.theweatherapp.theweathersenseiapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TheSplashScreen extends AppCompatActivity {
    private ConnectivityManager connectivityManager;

    private boolean isChecked=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_splash_screen_layout);
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Network[] allNetworks = connectivityManager.getAllNetworks();

        if (allNetworks.length==0){
            showMyDialog();
        }
        else {
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    Intent intent=new Intent(TheSplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }

    private void showMyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection Found!")
                .setMessage("Please enable wifi or data to proceed")
                .setCancelable(false)
                .setIcon(R.drawable.weather_sensei_icon)
                .setPositiveButton("Settings", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_SETTINGS)))
                .setNegativeButton("Exit", (dialogInterface, i) -> finish()).create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isChecked=true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isChecked) {
            Network[] allNetworks = connectivityManager.getAllNetworks();
            if (allNetworks.length==0) {
                showMyDialog();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

}
