package com.example.asimikram.batterytest_new;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class RunningApps extends Activity {

    public String [] user_data = new String [100];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_apps);

        TextView runningApps = (TextView)findViewById(R.id.runningAppsView);
        runningApps.setMovementMethod(new ScrollingMovementMethod());
        final TextView apps = (TextView)findViewById(R.id.runningAppsView);
        apps.setText("");

//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(Integer.MAX_VALUE);
//
//
//        for(int i=0; i<tasks.size(); i++)
//        {
//            apps.setText(apps.getText() + tasks.get(i).baseActivity.toShortString() + "\n");
//        }

//        UsageStatsManager lUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
//        List<UsageStats> lUsageStatsList = lUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis()- TimeUnit.DAYS.toMillis(1),System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(1));
//
//
//
//
//
//        for (UsageStats lUsageStats:lUsageStatsList){
//            apps.setText(apps.getText() + lUsageStats.getPackageName() + "\n");
//        }

        //apps.setText(lStringBuilder.toString());

        Context context = getApplicationContext();

        String filename = "Apps.txt";
//        FileOutputStream outputStream;
//
//
//
//        PackageManager pm = context.getPackageManager();
//
//        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
//        for(int i=0; i<processes.size(); i++)
//        {
//            if(AndroidProcesses.isMyProcessInTheForeground()) {
//                AndroidAppProcess process = processes.get(i);
//                try {
//                    PackageInfo packageInfo = process.getPackageInfo(getApplicationContext(), 0);
//                    String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
//                    String newAppName = appName + "\n";
//                    outputStream = openFileOutput(filename,Context.MODE_APPEND);
//                    outputStream.write(newAppName.getBytes());
//
//                    //                String processName = process.name;
//                    //apps.setText(apps.getText() + appName + "\n");
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
        try {
            InputStream inputStream = context.openFileInput("Apps.txt");
            if(inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = "";
                int ind = 0;
                while((str = bufferedReader.readLine()) != null)
                {
                    apps.setText(apps.getText() + str + "\n");
                    user_data[ind] = str;
                    ind = ind+1;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public String [] get_data()
    {
        return user_data;
    }

}
