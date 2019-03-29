package com.example.asimikram.batterytest_new;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.FileOutputStream;
import java.util.List;

public class RunningAppsService extends Service {
    public RunningAppsService() {
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

        String filename = "Apps.txt";
        FileOutputStream outputStream;

        Context context = getApplicationContext();

        //Referenced from: https://github.com/jaredrummler/AndroidProcesses
        PackageManager pm = context.getPackageManager();

        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();

        for(int i=0; i<processes.size(); i++)
        {
            if(AndroidProcesses.isMyProcessInTheForeground()) {
                AndroidAppProcess process = processes.get(i);
                try {
                    PackageInfo packageInfo = process.getPackageInfo(getApplicationContext(), 0);
                    String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                    String newAppName = appName + "\n";
                    outputStream = openFileOutput(filename,Context.MODE_APPEND);
                    outputStream.write(newAppName.getBytes());
                    //deleteFile(filename);



                    //                String processName = process.name;
                    //apps.setText(apps.getText() + appName + "\n");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
