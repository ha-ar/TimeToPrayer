package com.algorepublic.cityhistory.prayertimings;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by waqas on 8/4/15.
 */
public class AlarmManagerHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Namaz Alert", Toast.LENGTH_SHORT).show();
        Intent service1 = new Intent(context, AlarmService.class);
        context.startService(service1);
    }

    public static void setAlarms(Context context) {

    }

    public static void cancelAlarms(Context context) {

    }

    private static PendingIntent createPendingIntent(Context context, AlarmModel model) {
        return null;
    }


}
