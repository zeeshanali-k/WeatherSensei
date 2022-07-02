package com.theweatherapp.theweathersenseiapp.appWidgets;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.N;

/**
 * Implementation of App Widget functionality.
 */
public class FirstWidget extends AppWidgetProvider {

    private static boolean isFahrenheit;
    private final String TAG = "tagg";

    //widget required fields

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, WeatherData weatherData) {

        Log.d("tagg", "updateAppWidget: ");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.first_widget);

        //setting location name
        String locationName = getLocationName(context, weatherData.getLat(), weatherData.getLon());
        if (locationName != null) {
            views.setTextViewText(R.id.first_widget_location_tv, locationName);
        }
        //setting current min max temp
        views.setTextViewText(R.id.first_widget_tv,
                String.valueOf((int) weatherData.getCurrent().getTemp()));
        views.setTextViewText(R.id.first_widget_min_temp,
                (int) (weatherData.getDaily().get(0).getTemp().getMin()) + " ");
        views.setTextViewText(R.id.first_widget_max_temp,
                (int) (weatherData.getDaily().get(0).getTemp().getMax()+1) + " ");


        views.setTextViewText(R.id.first_widget_temp_unit, isFahrenheit ? "°F" : "°C");
        views.setTextViewText(R.id.first_widget_max_temp_unit, isFahrenheit ? "°F" : "°C");
        views.setTextViewText(R.id.first_widget_min_temp_unit, isFahrenheit ? "°F" : "°C");
        //setting icon

        setWeatherIcon(views, context, weatherData.getCurrent().getWeather().get(0));


        //setting time
        String updateTime = new SimpleDateFormat("hh:mm aaa", Locale.getDefault())
                .format(new Date());

        views.setTextViewText(R.id.first_widget_time_tv, updateTime);

        //setting pop (chances of rain)

        views.setTextViewText(R.id.first_widget_pop_tv, (int) (weatherData.getDaily().get(0)
                .getPop() * 100) + "%");

        //setting weather condition
        views.setTextViewText(R.id.first_widget_weather_condition_tv, weatherData
                .getCurrent().getWeather().get(0).getMain());

        views.setTextViewText(R.id.first_widget_weather_desc, weatherData.getCurrent().getWeather().get(0).getDescription().toUpperCase());
        //setting intents etc
        Intent appIntent = new Intent(context, TheSplashScreen.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 10932, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.the_first_widget, pendingIntent);

        // Instruct the widget manager to update the widget

        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    private static void setWeatherIcon(RemoteViews views, Context context, Weather_ iconDetails) {
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
        if (theIcon == null) {
            theIcon = iconDetails.getIcon() + ".png";
        }

        try {
            InputStream iconStream = context.getResources().getAssets().open(theIcon);
            views.setImageViewBitmap(R.id.first_widget_img, BitmapFactory.decodeStream(iconStream));

        } catch (IOException e) {
            Log.d("tagg", "setImage: " + e);
        }


    }

    private static String getLocationName(Context context, double lat, double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String address = null;
        try {
            List<Address> fromLocation = geocoder.getFromLocation(lat, lon, 1);
            address = fromLocation.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(TAG, "onUpdate: ");
        this.context = context;
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;

        getLocationUpdates();

        // There may be multiple widgets active, so update all of them

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

                queryMap.put("lat",String.valueOf(latitude));
                queryMap.put("lon",String.valueOf(longitude));

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
                //updating widgets after web request completion
                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId,
                                finalWeatherData);
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
                                        weatherData);
                            }
                        }
                    });
        }

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

