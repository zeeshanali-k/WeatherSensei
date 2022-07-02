package com.theweatherapp.theweathersenseiapp;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.theweatherapp.theweathersenseiapp.ui.WeatherDataFrag;
import com.theweatherapp.theweathersenseiapp.ui.customInrterfaces.MyClickInterFace;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Daily;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Hourly;

public class MainActivity extends AppCompatActivity implements MyClickInterFace {

    //ads unit ids
    public static final String NATIVE_AD_UNIT_ID = "";
    //original native: ca-app-pub-7908107276821748/7304260823
    //test: ca-app-pub-3940256099942544/2247696110

    public static final String INTERSTITIAL_AD_UNIT_ID = "";
    //original interstitial: ca-app-pub-7908107276821748/9738852477
    //test: ca-app-pub-3940256099942544/1033173712

    public static final String HOURLY_DATA="hourlydata";
    public static final String DAILY_DATA="dailydata";
    public static final String WEAHTER_SENSEI_PREFERENCES ="UNIT_PREFERENCES";
    public static final String IS_FAHRENHEIT="IS_FAHRENHEIT";
    public static final String IS_BACKGROUND_IMAGE = "IS_BACKGROUND_IMAGE";
    public static final String SHOW_TIPS = "show_tips";
    private DrawerLayout drawerLayout;

    //ads fields
    private com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd;
    private int clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_holder,new WeatherDataFrag())
                .commit();

        //tips setup
       if(getSharedPreferences(WEAHTER_SENSEI_PREFERENCES,MODE_PRIVATE).getBoolean(SHOW_TIPS,true)) {
           new AlertDialog.Builder(this)
                   .setTitle("Tip")
                   .setIcon(R.drawable.ic_baseline_info_24)
                   .setMessage(R.string.weather_guide_tip)
                   .setPositiveButton("Ok", null)
                   .create().show();
       }

        //finding drawer layout
        drawerLayout=findViewById(R.id.drawer_layout);

        //handling ads
        InterstitialAd.load(this,INTERSTITIAL_AD_UNIT_ID,
                new AdRequest.Builder().build(),new InterstitialAdLoadCallback(){
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd iAd) {
                        super.onAdLoaded(iAd);
                        interstitialAd=iAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }
                });

    }
    @Override
    public void onHourlyItemClick(Hourly hourlyData,Pair<View,String> []pairsList) {

        if (clicked>4){
            if(interstitialAd!=null) {
                interstitialAd.show(this);
                return;
            }
        }

        ActivityOptions activityOptions=ActivityOptions
                .makeSceneTransitionAnimation(MainActivity.this,pairsList);
        Intent intent=new Intent(MainActivity.this,WeatherHourlyDetails.class);
        intent.putExtra(HOURLY_DATA,hourlyData);
        startActivity(intent,activityOptions.toBundle());

    }

    @Override
    public void onDailyItemClick(Daily dailyData, Pair<View, String> []pairsList) {
        //change after upload on PlayStore
        if (clicked>4){
            if(interstitialAd!=null) {
                interstitialAd.show(this);
                return;
            }
        }
        clicked++;

        ActivityOptions activityOptions=ActivityOptions
                .makeSceneTransitionAnimation(MainActivity.this,pairsList);
        Intent intent=new Intent(MainActivity.this,WeatherDailyDetails.class);
        intent.putExtra(DAILY_DATA,dailyData);
        startActivity(intent,activityOptions.toBundle());

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout!=null && drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MobileAds.initialize(this);
    }
}