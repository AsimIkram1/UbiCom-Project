package com.example.asimikram.batterytest_new;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.Toast;

public class BatteryService extends Service {
    public BatteryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

//        boolean batteryCheck = isPhonePluggedIn(getBaseContext());
//
//        if(batteryCheck)
//        {
//            Toast.makeText(getApplicationContext(),"Charging",Toast.LENGTH_LONG);
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(),"Not Charging",Toast.LENGTH_LONG);
//        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    //Referenced from: http://stackoverflow.com/questions/6243452/how-to-know-if-the-phone-is-charging
    public boolean isPhonePluggedIn(Context context){
        boolean charging = false;

        final Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharge = status==BatteryManager.BATTERY_STATUS_CHARGING;

        int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        if (batteryCharge) charging=true;
        if (usbCharge) charging=true;
        if (acCharge) charging=true;

        return charging;
    }


    //Referenced from: http://stackoverflow.com/questions/3291655/get-battery-level-and-state-in-android
    public int batteryPercentage(Context context){
        boolean charging = false;

        final Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int level = -1;
        if (rawlevel >= 0 && scale > 0) {
            level = (rawlevel * 100) / scale;
        }

        return level;
    }


}
