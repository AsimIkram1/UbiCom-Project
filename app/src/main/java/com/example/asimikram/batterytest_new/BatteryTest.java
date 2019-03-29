package com.example.asimikram.batterytest_new;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class BatteryTest extends Activity {

    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;

    String latitude = "0";
    String longitude = "0";
    String removeLongitude = "0";
    String removeLatitude = "0";
    double inLatitude = 0;
    double inLongitude = 0;
    double outLatitude = 0;
    double outLongitude = 0;

    double latitude2 = 0;
    double longitude2 = 0;
    double removeLat2 = 0;
    double removeLon2 = 0;
    double latChange = 0;
    double lonChange = 0;
    String filename = "Locations.txt";
    FileOutputStream outputStream;
    //Context context = getApplicationContext();
    double checkLat = 0;
    double checkLong = 0;
    double originalLat = 0;
    double originalLong = 0;
    String finalLocationString = null;
    boolean check = false;

    List<Address> addresses;
    Geocoder geocoder;

    boolean batteryCharge1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_test);


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will  execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //startService(new Intent(getBaseContext(), LocationService.class));

        final TextView batteryStatus = (TextView) findViewById(R.id.batteryView);
        final TextView batteryPercentage = (TextView) findViewById(R.id.perView);
        final TextView oldLatitude = (TextView) findViewById(R.id.oldLat);
        final TextView oldLongitude = (TextView) findViewById(R.id.oldLong);
        final TextView newLatitude = (TextView) findViewById(R.id.newLat);
        final TextView newLongitude = (TextView) findViewById(R.id.newLong);
        final TextView inLocation = (TextView) findViewById(R.id.inLocationName);
        final TextView outLocation = (TextView) findViewById(R.id.outLocationName);

        final BatteryService bat = new BatteryService();
        final LocationService loc = new LocationService(BatteryTest.this);

        geocoder = new Geocoder(this, Locale.getDefault());

        String firstLine = "0~0";

        try {
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(firstLine.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }


        final Handler handler = new Handler();

        batteryCharge1 = bat.isPhonePluggedIn(getBaseContext());

        final Runnable r = new Runnable() {
            public void run() {

                boolean batteryCharge = bat.isPhonePluggedIn(getBaseContext());
                if (batteryCharge) {
                    startService();
                    batteryStatus.setText("Charging");
                    outLocation.setText("");
                    latitude = String.valueOf(loc.getLatitude());
                    longitude = String.valueOf(loc.getLongitude());
                    inLatitude = loc.getLatitude();
                    inLongitude = loc.getLongitude();
                    latitude2 = loc.getLatitude();
                    longitude2 = loc.getLongitude();

                    try {
                        addresses = geocoder.getFromLocation(inLatitude, inLongitude, 1);
                        String address = addresses.get(0).getLocality();
                        inLocation.setText(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    oldLatitude.setText(latitude);
                    oldLongitude.setText(longitude);
                    newLatitude.setText("0");
                    newLongitude.setText("0");
                } else {                                          //Plugged out
                    batteryUsageNotif();
                    batteryStatus.setText("Not Charging");
                    inLocation.setText("");
                    removeLatitude = String.valueOf(loc.getLatitude());
                    removeLongitude = String.valueOf(loc.getLongitude());
                    outLatitude = loc.getLatitude();
                    outLongitude = loc.getLongitude();

                    removeLat2 = loc.getLatitude();
                    removeLon2 = loc.getLongitude();

                    latChange = removeLat2 - latitude2;
                    lonChange = longitude2 - removeLon2;

                    try {
                        addresses = geocoder.getFromLocation(outLatitude, outLongitude, 1);
                        String address = addresses.get(0).getLocality();
                        outLocation.setText(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    newLatitude.setText(removeLatitude);
                    newLongitude.setText(removeLongitude);
                    oldLatitude.setText("0");
                    oldLongitude.setText("0");



                    ///////////////////////CHECKING IF POSITION IS THE SAME AND ASKING USER////////////////////////////////////
//                    if(latChange == 0 && lonChange == 0)
//                    {
//                    try {
//                        InputStream inputStream = getApplicationContext().openFileInput("Locations.txt");
//                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                        String write = removeLatitude + removeLongitude;
//                        if (inputStream == null) {
//                            Toast.makeText(getApplicationContext(), "EMPTY", Toast.LENGTH_LONG).show();
//                            outputStream.write(write.getBytes());
//                        } else {
//                            String duplicate = "";
//                            while ((duplicate = bufferedReader.readLine()) != null) {
//                                if (duplicate.equals(write)) {
//                                    Toast.makeText(getApplicationContext(), "EQUAL", Toast.LENGTH_LONG).show();
//                                    //Do nothing
//                                } else {
//                                    //Generate dialogue
////                                        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(BatteryTest.this);
////                                        alertdialogBuilder.setMessage("Mark this location as a charging spot?");
////                                        alertdialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener()
////                                        {
////                                            @Override
////                                            public  void onClick(DialogInterface arg0, int arg1)
////                                            {
////                                                Toast.makeText(BatteryTest.this,"You clicked yes", Toast.LENGTH_LONG).show();
////                                            }
////
////                                        });
////
////                                        alertdialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
////                                            @Override
////                                            public void onClick(DialogInterface dialog, int which) {
////                                                finish();
////                                            }
////                                        });
////
////                                        AlertDialog alertDialog = alertdialogBuilder.create();
////                                        alertDialog.show();
//
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }


                    //}

                }
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

        final Handler handler2 = new Handler();

        final Runnable r2 = new Runnable() {
            public void run() {

                int percentage = bat.batteryPercentage(getBaseContext());
                batteryPercentage.setText(String.valueOf(percentage));

                handler2.postDelayed(this, 1000);
            }
        };
        handler2.postDelayed(r2, 1000);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                    checkLat = loc.getLatitude();
                    checkLong = loc.getLongitude();

                    if (batteryCharge1) {
                        try {
                            InputStream inputStream = getApplicationContext().openFileInput("Locations.txt");
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String str = null;

                            while((str = bufferedReader.readLine()) != null)
                            {
                                String fileLocations[] = str.split("~");
                                originalLat = Double.parseDouble(fileLocations[0]);
                                originalLong = Double.parseDouble(fileLocations[1]);
                                if(originalLong == checkLong || originalLat == checkLat)
                                {
                                    check = true;
                                    //Toast.makeText(BatteryTest.this, Double.toString(originalLat) + " " + Double.toString(originalLong), Toast.LENGTH_LONG );
                                }
//                                else
//                                {
//                                    Toast.makeText(BatteryTest.this, Double.toString(originalLat) + " " + Double.toString(originalLong), Toast.LENGTH_LONG );
//                                    final AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(BatteryTest.this);
//                                    alertdialogBuilder.setMessage("Mark this location as a charging spot?");
//                                    alertdialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface arg0, int arg1) {
//                                            try {
//                                                String strCheckLat = Double.toString(checkLat);
//                                                String strCheckLong = Double.toString(checkLong);
//                                                finalLocationString = strCheckLat + "~" + strCheckLong;
//                                                outputStream.write(finalLocationString.getBytes());
//                                                //Toast.makeText(BatteryTest.this, "Location Saved", Toast.LENGTH_LONG).show();
//                                                arg0.dismiss();
//                                            }
//                                            catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//
//                                    });
//
//                                    alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//                                    AlertDialog alertDialog = alertdialogBuilder.create();
//                                    alertDialog.show();
//                                }
                            }//End of while loop

                            if(check != true)
                            {
                                Toast.makeText(BatteryTest.this, Double.toString(originalLat) + " " + Double.toString(originalLong), Toast.LENGTH_LONG );
                                    final AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(BatteryTest.this);
                                    alertdialogBuilder.setMessage("Mark this location as a charging spot?");
                                    alertdialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            try {
                                                String strCheckLat = Double.toString(checkLat);
                                                String strCheckLong = Double.toString(checkLong);
                                                finalLocationString = strCheckLat + "~" + strCheckLong;
                                                outputStream.write(finalLocationString.getBytes());
                                                //Toast.makeText(BatteryTest.this, "Location Saved", Toast.LENGTH_LONG).show();
                                                arg0.dismiss();
                                            }
                                            catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    });

                                    alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alertDialog = alertdialogBuilder.create();
                                    alertDialog.show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(getApplicationContext(), Double.toString(latChange), Toast.LENGTH_LONG).show();
//                        final AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(BatteryTest.this);
//                        alertdialogBuilder.setMessage("Mark this location as a charging spot?");
//                        alertdialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                try {
//
//                                    Toast.makeText(BatteryTest.this, "Location Saved", Toast.LENGTH_LONG).show();
//                                    arg0.dismiss();
//                                }
//                                catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        });
//
//                        alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        AlertDialog alertDialog = alertdialogBuilder.create();
//                        alertDialog.show();
                    }

                }

        });

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(!batteryCharge1)
//                {
//                    batteryUsageNotif();
//                }
//            }
//        });


        startService2();

        showRunningApps();
        showBatteryUsage();


        //startService();

    }

    public void showRunningApps()
    {
        Button showRunningAppsButton = (Button)findViewById(R.id.appsButton);
        final Context context = this;

        showRunningAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RunningApps.class);
                startActivity(intent);
            }
        });
    }

    public void showBatteryUsage()
    {
        Button showUsageButton = (Button)findViewById(R.id.levelShow);
        final Context context = this;

        showUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BatteryLevel.class);
                startActivity(intent);
            }
        });

    }

    public void batteryUsageNotif()
    {
        startService(new Intent(getBaseContext(), PluggedOutService.class));
    }


    public void startService() {

        startService(new Intent(getBaseContext(), PluggedInService.class));
    }

    public void startService2() {
        startService(new Intent(getBaseContext(), RunningAppsService.class));
    }

}
