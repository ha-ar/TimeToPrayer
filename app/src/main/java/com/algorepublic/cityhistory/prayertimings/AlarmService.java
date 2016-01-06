package com.algorepublic.cityhistory.prayertimings;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by waqas on 8/4/15.
 */
public class AlarmService extends Service {
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon);
        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);
        Notification notification = new Notification(R.mipmap.icon,"Namaz Time Notification!", System.currentTimeMillis());

        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), "Prayer Times Notification", "Namaz Time Notification!", pendingNotificationIntent);
        mManager.notify(0, notification);

        MediaPlayer mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.adhan_doa_converted);
////        try {
////            mediaPlayer.start();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
        mediaPlayer.start();


        Uri path = Uri.parse("android.resource://com.algorepublic.cityhistory.prayertimings/raw/adhan_doa_converted");
        RingtoneManager.setActualDefaultRingtoneUri(
                getApplicationContext(), RingtoneManager.TYPE_RINGTONE,
                path);
        Log.i("TESTT", "Ringtone Set to Resource: " + path.toString());
        RingtoneManager.getRingtone(getApplicationContext(), path)
                .play();

        return super.onStartCommand(intent, flags, startId);
    }
}
