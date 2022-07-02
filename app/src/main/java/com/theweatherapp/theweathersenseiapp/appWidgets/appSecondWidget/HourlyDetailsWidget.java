package com.theweatherapp.theweathersenseiapp.appWidgets.appSecondWidget;

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
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Weather__;

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

public class HourlyDetailsWidget extends AppWidgetProvider{

    private static final String TAG = "tagg";
    private Context context;
    private int[] appWidgetIds;
    private AppWidgetManager appWidgetManager;
    private boolean isFahrenheit;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, WeatherData weatherData, boolean isFahrenheit) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.hourly_details_widget);

        int[] hourlyTimeViewIds = getHourlyTimeViewIds();
        int[] hourlyCondImgViewIds = getHourlyCondImgViewIds();
        int[] hourlyCondTvViewIds = getHourlyCondTvViewIds();
        int[] hourlyTempViewIds = getHourlyTempViewIds();
        int[] hourlyTempUnitViewIds = getHourlyTempUnitViewIds();

        views.setTextViewText(R.id.hourly_widget_location_tv,
                getLocationName(context, weatherData.getLat(), weatherData.getLon()));

        //setting chances of rain
        views.setTextViewText(R.id.hourly_widget_pop_tv,
                (int)(weatherData.getDaily().get(0).getPop()*100)+"%");

        //setting temp details
        views.setTextViewText(R.id.hourly_widget_temp_tv,
                String.valueOf((int)weatherData.getCurrent().getTemp()));
        views.setTextViewText(R.id.hourly_widget_temp_unit,isFahrenheit?"°F":"°C");

        //setting pending intent
        Intent intent=new Intent(context, TheSplashScreen.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1231,intent,0);
        views.setOnClickPendingIntent(R.id.hourly_details_widget_parent,pendingIntent);

        for (int i=0;i<7;i++) {

            views.setTextViewText(hourlyTimeViewIds[i], new SimpleDateFormat("hh:mm aaa",
                    Locale.getDefault()).format(new Date(weatherData.getHourly().get(i+1).getDt() * 1000)));

            setWeatherIcon(views,context,weatherData.getHourly().get(i+1).getWeather().get(0),hourlyCondImgViewIds[i]);

            views.setTextViewText(hourlyTempViewIds[i],
                    String.valueOf((int) weatherData.getHourly().get(i+1).getTemp()));

            views.setTextViewText(hourlyTempUnitViewIds[i],isFahrenheit?"°F":"°C");

            views.setTextViewText(hourlyCondTvViewIds[i],
                    weatherData.getHourly().get(i+1).getWeather().get(0).getMain());

        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static int[] getHourlyTimeViewIds() {

        int []hourlyTimeViewIds=new int[7];
        hourlyTimeViewIds[0]=R.id.hourly_widget_time1;
        hourlyTimeViewIds[1]=R.id.hourly_widget_time2;
        hourlyTimeViewIds[2]=R.id.hourly_widget_time3;
        hourlyTimeViewIds[3]=R.id.hourly_widget_time5;
        hourlyTimeViewIds[4]=R.id.hourly_widget_time6;
        hourlyTimeViewIds[5]=R.id.hourly_widget_time7;
        hourlyTimeViewIds[6]=R.id.hourly_widget_time8;

        return hourlyTimeViewIds;
    }

    private static int[] getHourlyTempViewIds() {

        int []hourlyTempViewIds=new int[7];
        hourlyTempViewIds[0]=R.id.hourly_widget_temp1;
        hourlyTempViewIds[1]=R.id.hourly_widget_temp2;
        hourlyTempViewIds[2]=R.id.hourly_widget_temp3;
        hourlyTempViewIds[3]=R.id.hourly_widget_temp5;
        hourlyTempViewIds[4]=R.id.hourly_widget_temp6;
        hourlyTempViewIds[5]=R.id.hourly_widget_temp7;
        hourlyTempViewIds[6]=R.id.hourly_widget_temp8;

        return hourlyTempViewIds;
    }

    private static int[] getHourlyTempUnitViewIds() {

        int []hourlyTempViewIds=new int[7];
        hourlyTempViewIds[0]=R.id.hourly_widget_temp_unit1;
        hourlyTempViewIds[1]=R.id.hourly_widget_temp_unit2;
        hourlyTempViewIds[2]=R.id.hourly_widget_temp_unit3;
        hourlyTempViewIds[3]=R.id.hourly_widget_temp_unit4;
        hourlyTempViewIds[4]=R.id.hourly_widget_temp_unit6;
        hourlyTempViewIds[5]=R.id.hourly_widget_temp_unit7;
        hourlyTempViewIds[6]=R.id.hourly_widget_temp_unit8;

        return hourlyTempViewIds;
    }

    private static int[] getHourlyCondImgViewIds() {

        int []hourlyCondImgViewIds=new int[7];
        hourlyCondImgViewIds[0]=R.id.hourly_widget_cond_img1;
        hourlyCondImgViewIds[1]=R.id.hourly_widget_cond_img2;
        hourlyCondImgViewIds[2]=R.id.hourly_widget_cond_img3;
        hourlyCondImgViewIds[3]=R.id.hourly_widget_cond_img5;
        hourlyCondImgViewIds[4]=R.id.hourly_widget_cond_img6;
        hourlyCondImgViewIds[5]=R.id.hourly_widget_cond_img7;
        hourlyCondImgViewIds[6]=R.id.hourly_widget_cond_img8;

        return hourlyCondImgViewIds;
    }

    private static int[] getHourlyCondTvViewIds() {

        int []hourlyCondImgViewIds=new int[7];
        hourlyCondImgViewIds[0]=R.id.hourly_widget_cond_tv1;
        hourlyCondImgViewIds[1]=R.id.hourly_widget_cond_tv2;
        hourlyCondImgViewIds[2]=R.id.hourly_widget_cond_tv3;
        hourlyCondImgViewIds[3]=R.id.hourly_widget_cond_tv5;
        hourlyCondImgViewIds[4]=R.id.hourly_widget_cond_tv6;
        hourlyCondImgViewIds[5]=R.id.hourly_widget_cond_tv7;
        hourlyCondImgViewIds[6]=R.id.hourly_widget_cond_tv8;

        return hourlyCondImgViewIds;
    }

    private static void setWeatherIcon(RemoteViews views,Context context, Weather__ iconDetails,int viewId) {
        String theIcon;
        String iconDesc = iconDetails.getDescription().toLowerCase().trim();
        switch (iconDesc) {
            //rain icon
            case "shower rain and drizzle":
            case "heavy shower rain and drizzle":
            case "shower rain":
            case "heavy intensity shower rain":
            case "ragged shower rain":
                theIcon = "shower_rain.png";

                break;

                //snow icon
            case "light shower snow":
            case "shower snow":
            case "heavy shower snow":

                theIcon = "shower_snow.png";

                break;

                //sleet (snowfall) icon
            case "sleet":
            case "light shower sleet":
            case "shower sleet":
                theIcon = "sleet.png";
                break;
            default:
                theIcon = iconDetails.getIcon() + ".png";
                break;
        }

        try {
            InputStream iconStream = context.getResources().getAssets().open(theIcon);
            views.setImageViewBitmap(viewId, BitmapFactory.decodeStream(iconStream));

        } catch (IOException e) {
            Log.d("tagg", "setImage: "+e);
        }

    }

    private static String getLocationName(Context context,double lat,double lon) {
        Geocoder geocoder=new Geocoder(context, Locale.getDefault());
        String address=null;
        try {
            List<Address> fromLocation = geocoder.getFromLocation(lat, lon, 1);
            address=fromLocation.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(TAG, "onUpdate: hourly");
        this.context = context;
        this.appWidgetIds=appWidgetIds;
        this.appWidgetManager=appWidgetManager;

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

        if (SDK_INT<N) {
            Executor executor
                    = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                RetrofitDao retrofitDao = RetrofitDao.retroFit.create(RetrofitDao.class);
                Map<String, String> queryMap = new HashMap<>();

                queryMap.put("lat", String.valueOf(latitude));
                queryMap.put("lon", String.valueOf(longitude));
                isFahrenheit = context.
                        getSharedPreferences(MainActivity.WEAHTER_SENSEI_PREFERENCES, Context.MODE_PRIVATE)
                        .getBoolean(MainActivity.IS_FAHRENHEIT, false);
                queryMap.put("units", isFahrenheit ? "imperial" : "metric");
                queryMap.put("appid", "2e6bf386ab7eb4a6650f129879bd2e03");
                queryMap.put("exclude", "minutely");

                Call<WeatherData> weatherData = retrofitDao.getWeatherdata(queryMap);
                WeatherData finalWeatherData = null;
                try {
                    finalWeatherData = weatherData.execute()
                            .body();
                } catch (IOException e) {
                    Log.d(TAG, "loadInBackground: " + e.getLocalizedMessage());
                }
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, finalWeatherData, isFahrenheit);
                }

            });
        }

        else {
            //running async
            CompletableFuture.supplyAsync(() -> {
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

                Call<WeatherData> weatherData = retrofitDao.getWeatherdata(queryMap);
                WeatherData finalWeatherData=null;
                try {
                    finalWeatherData = weatherData.execute()
                            .body();
                } catch (IOException e) {
                    Log.d(TAG, "loadInBackground: "+e.getLocalizedMessage());
                }
                return finalWeatherData;
            })
                    //setting values
                    .thenAccept(weatherData -> {
                        if (weatherData!=null) {
                            for (int appWidgetId : appWidgetIds)
                                updateAppWidget(context, appWidgetManager, appWidgetId, weatherData, isFahrenheit);
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

