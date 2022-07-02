package com.theweatherapp.theweathersenseiapp.ui.bindingadapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.theweatherapp.theweathersenseiapp.weatherDataModel.Weather_;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Weather__;
import com.theweatherapp.theweathersenseiapp.weatherDataModel.Weather___;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CommonBindingAdapters {

    private static String TAG="tagg";

    @BindingAdapter("textSetter")
    public static void setText(TextView view,long dtValue){
        if (dtValue>0) {
            String formattedDate = new SimpleDateFormat("E, dd MMM hh:mm aaa")
                    .format(new Date(dtValue*1000));
            view.setText(formattedDate);
        }
    }

    @BindingAdapter("hourlyTextSetter")
    public static void setHourlyText(TextView view,long dtValue){
        if (dtValue>0) {
            String formattedDate = new SimpleDateFormat("hh:mm aaa",Locale.getDefault())
                    .format(new Date(dtValue*1000));
            view.setText(formattedDate);
        }
    }
    @BindingAdapter("dailyTextSetter")
    public static void setDailyText(TextView view,long dtValue){
        if (dtValue>0) {
            String formattedDate = new SimpleDateFormat("E dd",Locale.getDefault())
                    .format(new Date(dtValue*1000));
            view.setText(formattedDate);
        }
    }

    //for details with Weather_ (1_) class


    @BindingAdapter("imageSetter")
    public static void setImage(ImageView view, Weather_ iconDetails){

        if (iconDetails!=null) {
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
                InputStream iconStream = view.getContext().getResources().getAssets().open(theIcon);
                view.setImageBitmap(BitmapFactory.decodeStream(iconStream));
                Log.d(TAG, "setImage: ");
            } catch (IOException e) {
                Log.d(TAG, "setImage: "+e);
            }


        }
    }

    //for details with Weather___ (3_) class

    @BindingAdapter("imageSetter")
    public static void setImage(ImageView view, Weather___ iconDetails){

        if (iconDetails!=null) {
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
                InputStream iconStream = view.getContext().getResources().getAssets().open(theIcon);
                view.setImageBitmap(BitmapFactory.decodeStream(iconStream));
                iconStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    //background image setter for main weather fragment
    @BindingAdapter("bgImageSetter")
    public static void setImage(ImageView view,String imageName){
        int randBg;
        if (imageName!=null && !imageName.equals("")) {
            String bgName=null;
            if (imageName.toLowerCase().equals("broken clouds") ||
            imageName.toLowerCase().equals("few clouds")){
                randBg=new Random().nextInt(2);
                if (randBg==0) bgName="broken_clouds.jpg";
                else if (randBg==1) bgName="dark_clouds.jpg";
            }
            else if (imageName.toLowerCase().equals("scattered clouds")){
                bgName="scattered_clouds.jpg";
            }
            else if (imageName.toLowerCase().equals("clear sky")){
                randBg = new Random().nextInt(3);
                if (randBg==0) bgName="clear_sky_one.jpg";
                else if (randBg==1) bgName="clear_sky_two.jpg";
                else if (randBg==2) bgName="clear_sky_three.jpg";
            }
            else if (imageName.toLowerCase().equals("rain")
            || imageName.toLowerCase().equals("shower rain")){
                randBg=new Random().nextInt(2);
                if (randBg==0) bgName="rain_one.jpg";
                else if (randBg==1) bgName="rain_two.jpg";
            }
            else if (imageName.toLowerCase().equals("snow")){
                randBg=new Random().nextInt(4);
                if (randBg==0) bgName="snow_one";
                else if (randBg==1) bgName="snow_two.jpg";
                else if (randBg==2) bgName="snow_three.jpg";
                else if (randBg==3) bgName="snow_four.jpg";
            }
            else if (imageName.toLowerCase().equals("thunderstorm")){
                randBg=new Random().nextInt(2);
                if (randBg==0) bgName="thunder_storm.jpg";
                else if (randBg==1) bgName="tornado_storm.jpg";
            }
            else if (imageName.toLowerCase().equals("mist")){
                bgName="mist.jpg";
            }

            if (bgName==null){
                return;
            }
            //getting image from assets and setting to image view

            Bitmap bgBitmap=null;
            try {
                InputStream bgStream=view.getContext().getResources().getAssets().open(bgName);
                bgBitmap = BitmapFactory.decodeStream(bgStream);
                bgStream.close();
            } catch (IOException e) {
                Log.d(TAG, "setImage: "+e.getLocalizedMessage());
            }
            if (bgBitmap!=null){
                view.setImageBitmap(bgBitmap);
            }

        }

    }

    //for details with Weather__ (2_) class
    @BindingAdapter("imageSetter")
    public static void setImage(ImageView view, Weather__ iconDetails){

        if (iconDetails!=null) {
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
                InputStream iconStream = view.getContext().getResources().getAssets().open(theIcon);
                view.setImageBitmap(BitmapFactory.decodeStream(iconStream));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



    @BindingAdapter(value = {"locationLat","locationLon"})
    public static void setLocationOnView(TextView view,double lat,double lon){
        if (lat!=0 && lon!=0 ) {
            Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
            try {
                List<Address> fromLocation = geocoder.getFromLocation(lat, lon, 1);
                if (fromLocation.get(0) != null) {
                    String addressLine = fromLocation.get(0).getAddressLine(fromLocation.get(0).getMaxAddressLineIndex());
                    view.setText(addressLine);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
