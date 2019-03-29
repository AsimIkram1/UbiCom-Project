package com.example.asimikram.batterytest_new;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class PluggedOutService extends Service {

    BatteryService bat = new BatteryService();

    public PluggedOutService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        boolean batteryCharge = bat.isPhonePluggedIn(getBaseContext());

        if(!batteryCharge)
        {
            //Referenced from: http://stackoverflow.com/questions/31428325/android-how-to-start-activity-when-user-clicks-a-notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                            .setContentTitle("Battery Status")
                            .setContentText("Check Batttery Status");

            Intent notificationIntent = new Intent(getApplicationContext(),BatteryLevel.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
            mBuilder.setContentIntent(pIntent);

            NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(001,mBuilder.build());



            //Toast.makeText(this, "Plugged in", Toast.LENGTH_LONG).show();

        }
        else
        {
            //Toast.makeText(this, "Plugged out", Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }
}
