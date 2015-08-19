package com.algorepublic.cityhistory.prayertimings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by waqas on 8/13/15.
 */
public class AlarmFragment extends Fragment {

   public Switch button;
    AQuery aq;
    int Loop = 0;
    GPSTracker gps;
    ArrayList prayerTimes;
    ImageView imageView;
    public View Views;
    int day=3600000*24;
    private  long time;
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
        gps = new GPSTracker(getActivity());
        button = (Switch) view.findViewById(R.id.home_detail);
        button.setChecked(false);
        Calendar cl = Calendar.getInstance();
        time = cl.getTimeInMillis();
        Log.e("tipss",time+"");
        getTime();
        return view;
    }

    private void getTime() {
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Log.e("lat",String.valueOf(latitude)+String.valueOf(longitude));
        double timezone = (Calendar.getInstance().getTimeZone()
                .getOffset(Calendar.getInstance().getTimeInMillis()))
                / (1000 * 60 * 60);
        PrayTime prayers = new PrayTime();
        prayers.setTimeFormat(prayers.Time12);
        prayers.setCalcMethod(prayers.Makkah);
        prayers.setAsrJuristic(prayers.Shafii);
        prayers.setAdjustHighLats(prayers.MidNight);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        prayerTimes = prayers.getPrayerTimes(cal, latitude,
                longitude, timezone);

        Log.e("praTime",prayerTimes+"");
        ArrayList prayerNames = prayers.getTimeNames();

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


        for (int i = 0; i < prayerTimes.size(); i++) {


            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a"); // I assume d-M, you may refer to M-d for month-day instead.
            Date date = null; // You will need try/catch around this
            try {
                date = formatter.parse(prayerTimes.get(i).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = date.getTime();
            String[] alarmTime = prayerTimes.get(i).toString().split(":");
            int hour = Integer.valueOf(alarmTime[0]);
            String[] alarmTimeMin = alarmTime[1].split(" ");
            int min = Integer.valueOf(alarmTimeMin[0]);
            cal.set(Calendar.HOUR, hour);
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.AM_PM, (alarmTimeMin[1].equalsIgnoreCase("am") ? Calendar.AM : Calendar.PM));

            long fireTime=cal.getTimeInMillis()-(3600000*24);
            if (i<2){
                fireTime=fireTime+(3600000*24);
            }

            long atime= 0;
            Log.e("fire",fireTime+"");

            if (fireTime>time){

                atime=  fireTime;


                Log.e("loger", atime + "");
            } else if (fireTime < time) {
                atime = fireTime +( 3600000 * 24);
                Log.e("sups", atime + "");

                if (button.isChecked()){

                Intent alarmIntent = new Intent(getActivity(), AlarmManagerHelper.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, atime, 3600000 * 24, pendingIntent);

            }
            }
        }

    }







    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);



    }

}
