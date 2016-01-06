package com.algorepublic.cityhistory.prayertimings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by waqas on 8/13/15.
 */
public class AlarmFragment extends Fragment {

    public ToggleButton button;
    AQuery aq;
    int Loop = 0;
    GPSTracker gps;
    ArrayList prayerTimes;
    ImageView imageView;
    Intent alarmIntent;
    public View Views;
    int day=3600000*24;
    PendingIntent pendingIntent;
    long atime= 0;
    private  long time;
    BaseClass base;
    long fireTime;
    AlarmManager alarmManager;
    ArrayList prayersTime24;
    int index=0;Calendar cal;
    String string_date = "12-December-2012";
    long dateforrow;
    private TextView txtPrayerTimes,sunrise;
    public static AlarmFragment newInstance() {
        AlarmFragment fragment = new AlarmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.alarmfrag, container, false);
        aq = new AQuery(getActivity(), view);
        base = ((BaseClass) getActivity().getApplicationContext());
        gps = new GPSTracker(getActivity());
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getRawOffset();
        Log.e("GMT offset is %s hours", String.valueOf(TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS)));
        Log.e("LOcation",String.valueOf(mGMTOffset));
        button = (ToggleButton) view.findViewById(R.id.home_detail);
        button.setTextOff("Off");
        button.setTextOn("On");
        getTimeandsetView();
        if (base.isChange()){
            setAlarmValuesON();
        }else
        {
            setAlarmValuesOFF();
        }
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setAlarmValuesON();
                    Toast.makeText(getActivity(), "Alarm ON!!",
                            Toast.LENGTH_LONG).show();

                } else {
                    setAlarmValuesOFF();
                    Toast.makeText(getActivity(), " Alarm OFF !!",
                            Toast.LENGTH_LONG).show();

//                            alarmManager.setRepeating(AlarmManager.RTC, atime, 3600000 * 24, pendingIntent);
//                            PendingIntent.getBroadcast(getActivity(), 0, alarmIntent,
//                                    PendingIntent.FLAG_UPDATE_CURRENT).cancel();
                }


            }
        });

        return view;
    }
    public void setAlarmValuesON(){
        button.setChecked(true);
        base.setChange(true);
        if(alarmManager !=null)
            alarmManager= null;

        getTimeandsetView();
        alarmON();
    }
    public void setAlarmValuesOFF(){
        base.setChange(false);
        button.setChecked(false);
        alarmOff();
    }
    private void getTimeandsetView() {
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        double timezone = (Calendar.getInstance().getTimeZone()
                .getOffset(Calendar.getInstance().getTimeInMillis()))
                / (1000 * 60 * 60);
        PrayTime prayers12 = new PrayTime();
        PrayTime prayers24 = new PrayTime();
        prayers12.setTimeFormat(prayers12.Time12);
        prayers12.setCalcMethod(prayers12.Makkah);
        prayers12.setAsrJuristic(prayers12.Shafii);
        prayers12.setAdjustHighLats(prayers12.AngleBased);


        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers12.tune(offsets);

        Date now = new Date();
        cal = Calendar.getInstance();
        cal.setTime(now);

        prayerTimes = prayers12.getPrayerTimes(cal, latitude,
                longitude, timezone);

        ArrayList prayerNames = prayers12.getTimeNames();

        LinearLayout parent = (LinearLayout) aq.id(R.id.inflater).getView();
        for (Loop=0; Loop < prayerNames.size(); Loop++) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Views = inflater.inflate(R.layout.custom,
                    null, true);

            txtPrayerTimes = (TextView) Views.findViewById(R.id.txtPrayerTimes_alram);
            txtPrayerTimes.setText(prayerNames.get(Loop).toString());
            sunrise = (TextView)Views.findViewById(R.id.time);
            sunrise.setText(prayerTimes.get(Loop).toString());
            Views.setId(Loop);

            parent.addView(Views);

        }
    }


    private boolean isFirstTime()
    {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();


        }
        return ranBefore;

    }
    private  void alarmON(){
        alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        for (int i = 0; i < prayerTimes.size(); i++) {
            if (!(i == 1 || i == 4)) {

                final String[] alarmTime = prayerTimes.get(i).toString().split(":");
                int hour = Integer.valueOf(alarmTime[0]);
                String[] alarmTimeMin = alarmTime[1].split(" ");
                int min = Integer.valueOf(alarmTimeMin[0]);
                cal.set(Calendar.HOUR, hour);
                cal.set(Calendar.MINUTE, min);
                cal.set(Calendar.AM_PM, (alarmTimeMin[1].equalsIgnoreCase("am") ? Calendar.AM : Calendar.PM));
                fireTime = cal.getTimeInMillis();

//            Log.e("apps", String.valueOf(i));
                if (i >= 2) {
                    fireTime = fireTime - (3600000 * 24);
                }
                Log.e("sss", String.valueOf(fireTime));
                time = System.currentTimeMillis();

                if (fireTime > time) {
                    atime = fireTime;
                    Log.e("TAime", atime + "");
                } else if (fireTime < time) {
                    atime = fireTime + (3600000 * 24);
                    Log.e("TAGa", atime + "");
                }
                Toast.makeText(getActivity(), "Alarm ON!!",
                        Toast.LENGTH_LONG).show();
                int ii = (int) atime;
                Log.e("atime", String.valueOf(atime));
                alarmIntent = new Intent(getActivity(), AlarmManagerHelper.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), ii, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC, atime, 3600000 * 24, pendingIntent);
            }
        }
    }

    private  void alarmOff(){
        Toast.makeText(getActivity(), " Alarm OFF !!",
                Toast.LENGTH_LONG).show();
        alarmIntent = new Intent(getActivity(), AlarmManagerHelper.class);
        alarmManager=null;
//        pendingIntent.cancel();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

   }

}
