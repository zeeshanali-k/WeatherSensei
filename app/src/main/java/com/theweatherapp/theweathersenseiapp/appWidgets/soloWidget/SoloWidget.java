package com.theweatherapp.theweathersenseiapp.appWidgets.soloWidget;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.theweatherapp.theweathersenseiapp.MainActivity;
import com.theweatherapp.theweathersenseiapp.R;
import com.theweatherapp.theweathersenseiapp.TheSplashScreen;
import com.theweatherapp.theweathersenseiapp.backEnd.RetrofitDao;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.WeatherData;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Weather_;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.N;

public class SoloWidget extends AppWidgetProvider {

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;
    private final String TAG="tagg";

    private boolean isFahrenheit;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, WeatherData weatherData, boolean isFahrenheit) {

        // Construct the RemoteViews object

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.solo_widget);
        views.setTextViewText(R.id.solo_widget_temp_tv,String.valueOf((int)weatherData.getCurrent().getTemp()));
        String minTemp=String.valueOf((int)(weatherData.getDaily().get(0).getTemp().getMin()));
        String maxTemp=String.valueOf((int)(weatherData.getDaily().get(0).getTemp().getMax()+1));
        views.setTextViewText(R.id.solo_widget_min_max_temp_tv,minTemp+"-"+maxTemp);
        views.setTextViewText(R.id.solo_widget_time_tv,new SimpleDateFormat("hh:mm aaa", Locale.getDefault())
        .format(new Date()));
        views.setTextViewText(R.id.solo_widget_temp_unit,isFahrenheit?"°F":"°C");
        //weather condition data
        setWeatherIcon(views,context,weatherData.getCurrent().getWeather().get(0));
        views.setTextViewText(R.id.solo_widget_weather_cond_tv,weatherData.getCurrent().getWeather().get(0).getMain());

        //creating and setting pending intent
        Intent intent=new Intent(context, TheSplashScreen.class);
        PendingIntent pendingIntent=PendingIntent
                .getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.solo_widget_layout,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //method for icon setting

    private static void setWeatherIcon(RemoteViews views,Context context, Weather_ iconDetails) {
        String theIcon = null;
        String iconDesc = iconDetails.getDescription().toLowerCase().trim();
        switch (iconDesc) {
            case "shower rain and drizzle":
            case "heavy shower rain and drizzle":
            case "shower drizzle":
            case "light intensity shower rain":
            case "shower rain":
            case "heavy intensity shower rain":
            case "ragged shower rain":
                theIcon = "shower_rain.png";

                break;
            case "light shower snow":
            case "shower snow":
            case "heavy shower snow":

                theIcon = "shower_snow.png";

                break;
            case "sleet":
            case "light shower sleet":
            case "shower sleet":
                theIcon = "sleet.png";
                break;
        }
        if (theIcon==null){
            theIcon=iconDetails.getIcon()+".png";
        }

        try {
            InputStream iconStream = context.getResources().getAssets().open(theIcon);
            views.setImageViewBitmap(R.id.solo_widget_weather_cond_img, BitmapFactory.decodeStream(iconStream));

        } catch (IOException e) {
            Log.d("tagg", "setImage: "+e);
        }


    }


    private void getLocationUpdates() {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Location result = task.getResult();
                getWeatherData(result.getLatitude(), result.getLongitude());
            }
        });
    }

    private void getWeatherData(double latitude, double longitude){
        if (SDK_INT<N){
            Executor executor= Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                RetrofitDao retrofitDao=RetrofitDao.retroFit.create(RetrofitDao.class);
                Map<String,String> queryMap=new HashMap<>();

                queryMap.put("lat",String.valueOf(latitude)); //change 1 to lattitude
                queryMap.put("lon",String.valueOf(longitude)); //change 1 to lattitude

                boolean isFahrenheit = context.
                        getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES, Context.MODE_PRIVATE)
                        .getBoolean(MainActivity.IS_FAHRENHEIT, false);
                queryMap.put("units",isFahrenheit?"imperial":"metric");
                queryMap.put("appid","2e6bf386ab7eb4a6650f129879bd2e03");
                queryMap.put("exclude","minutely");

                Call<WeatherData> weatherdata = retrofitDao.getWeatherdata(queryMap);


                WeatherData finalWeatherData = null;
                try {
                    finalWeatherData = weatherdata.execute().body();
                } catch (IOException e) {
                    Log.d(TAG, "getWeatherData: "+e.getLocalizedMessage());
                }
                //updating widgets after web request completion if data in not null
                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId,
                                finalWeatherData, isFahrenheit);
                    }

            });
            //end of if
        }
        //if android version > Nougat
        else {
            CompletableFuture.supplyAsync(() -> {
                RetrofitDao retrofitDao=RetrofitDao.retroFit.create(RetrofitDao.class);
                Map<String,String> queryMap=new HashMap<>();

                queryMap.put("lat",String.valueOf(latitude)); //change 1 to lattitude
                queryMap.put("lon",String.valueOf(longitude)); //change 1 to lattitude

                isFahrenheit = context.
                        getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES, Context.MODE_PRIVATE)
                        .getBoolean(MainActivity.IS_FAHRENHEIT, false);
                queryMap.put("units",isFahrenheit?"imperial":"metric");
                queryMap.put("appid","2e6bf386ab7eb4a6650f129879bd2e03");
                queryMap.put("exclude","minutely");

                Call<WeatherData> weatherdata = retrofitDao.getWeatherdata(queryMap);


                WeatherData finalWeatherData = null;
                try {
                    finalWeatherData = weatherdata.execute().body();
                } catch (IOException e) {
                    Log.d(TAG, "getWeatherData: "+e.getLocalizedMessage());
                }
                //updating widgets after web request completion if data in not null

                return finalWeatherData;
            })
                    .thenAccept(weatherData -> {
                        if (weatherData!=null) {
                            for (int appWidgetId : appWidgetIds) {
                                updateAppWidget(context, appWidgetManager, appWidgetId,
                                        weatherData, isFahrenheit);
                            }
                        }
                    });
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        this.context=context;
        this.appWidgetManager=appWidgetManager;
        this.appWidgetIds=appWidgetIds;

        getLocationUpdates();
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

