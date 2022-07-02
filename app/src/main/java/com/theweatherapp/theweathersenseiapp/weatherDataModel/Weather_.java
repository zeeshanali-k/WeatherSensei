package com.theweatherapp.theweathersenseiapp.weatherDataModel;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather_ implements Parcelable
{

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;
    public final static Parcelable.Creator<Weather_> CREATOR = new Creator<Weather_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Weather_ createFromParcel(Parcel in) {
            return new Weather_(in);
        }

        public Weather_[] newArray(int size) {
            return (new Weather_[size]);
        }

    }
            ;

    protected Weather_(Parcel in) {
        this.id = ((long) in.readValue((long.class.getClassLoader())));
        this.main = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.icon = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Weather_() {
    }


    public Weather_(long id, String main, String description, String icon) {
        super();
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(main);
        dest.writeValue(description);
        dest.writeValue(icon);
    }

    public int describeContents() {
        return 0;
    }

}
