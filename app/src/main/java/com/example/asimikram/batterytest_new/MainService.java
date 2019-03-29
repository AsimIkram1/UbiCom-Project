package com.example.asimikram.batterytest_new;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class MainService extends Service {

    BatteryService bat = new BatteryService();
    int status = -1;

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        boolean batteryCharge = bat.isPhonePluggedIn(getBaseContext());

        if(batteryCharge)
        {
            //Toast.makeText(this, "Plugged in", Toast.LENGTH_LONG).show();
            status = 0;
        }
        else
        {
            Toast.makeText(this, "Plugged out", Toast.LENGTH_LONG).show();
            status = 1;
        }

        return START_STICKY;
    }

    public int checkStatus()
    {
        return status;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
