package com.algorepublic.cityhistory.prayertimings;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by waqas on 8/21/15.
 */
public class BaseClass extends Application {


    public SharedPreferences appSharedPrefs;
    public SharedPreferences.Editor prefsEditor;
    public String SHARED_NAME = "com.algorepublic.cityhistory.prayertimings";
    private String Message = "Message";
    Boolean isInternetPresent = false;
    public double latitude;
    public double longitude;
    GPSTracker gps;
    ConnectionDetector cd;

    @Override
    public void onCreate() {
        super.onCreate();

        /* initialize preferences for future use */
        this.appSharedPrefs = getSharedPreferences(SHARED_NAME,
                Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
        gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        }



    public void setChange(boolean change) {
        prefsEditor.putBoolean("change", change).commit();
    }

    public boolean isChange() {
        return appSharedPrefs.getBoolean("change", false);
    }

    public Boolean CheckInternet(Context context)
    {
        if(!NetworkUtil.isInternetConnected(context)) {
            NetworkUtil.internetFailedDialog(context);
            return true;
        }else
            return false;
    }
    public void setMessage(String message){
        prefsEditor.putString(this.Message, message).commit();
    }
    public String getMessage(){
        return appSharedPrefs.getString(this.Message, "");
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = gps.latitude;
        Log.e("TAG",String.valueOf(gps.latitude));
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = gps.longitude;
    }
}
