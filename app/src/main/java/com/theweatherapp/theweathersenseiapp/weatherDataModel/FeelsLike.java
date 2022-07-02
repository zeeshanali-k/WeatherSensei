package com.theweatherapp.theweathersenseiapp.weatherDataModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeelsLike implements Parcelable
{

    @SerializedName("day")
    @Expose
    private double day;
    @SerializedName("night")
    @Expose
    private double night;
    @SerializedName("eve")
    @Expose
    private double eve;
    @SerializedName("morn")
    @Expose
    private double morn;
    public final static Parcelable.Creator<FeelsLike> CREATOR = new Creator<FeelsLike>() {


        public FeelsLike createFromParcel(Parcel in) {
            return new FeelsLike(in);
        }

        public FeelsLike[] newArray(int size) {
            return (new FeelsLike[size]);
        }

    }
            ;

    protected FeelsLike(Parcel in) {
        this.day = ((double) in.readValue((double.class.getClassLoader())));
        this.night = ((double) in.readValue((double.class.getClassLoader())));
        this.eve = ((double) in.readValue((double.class.getClassLoader())));
        this.morn = ((double) in.readValue((double.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public FeelsLike() {
    }

    public FeelsLike(double day, double night, double eve, double morn) {
        super();
        this.day = day;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(day);
        dest.writeValue(night);
        dest.writeValue(eve);
        dest.writeValue(morn);
    }

    public int describeContents() {
        return 0;
    }

}
