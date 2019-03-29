package com.example.asimikram.batterytest_new;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BatteryLevel extends AppCompatActivity {

    ImageView batteryImage;
    BatteryService bat = new BatteryService();
    TextView batteryUsage;
    TextView batteryLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_level);

        int level = bat.batteryPercentage(getApplicationContext());
        batteryUsage = (TextView)findViewById(R.id.batteryLevelText);
        batteryUsage.setTextSize(24);

        batteryLabel = (TextView)findViewById(R.id.batteryUsageLabel);
        batteryLabel.setTextSize(24);



        String result = null;
        batteryImage = (ImageView)findViewById(R.id.batteryView);
        classifier obj = new classifier();
        try {
             result = obj.classification();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        //Image referenced from: http://orig06.deviantart.net/013a/f/2010/228/e/5/battery_icon_by_apprenticeofart.png
        if(result.equals("Multi"))
        {
            batteryImage.setImageResource(R.drawable.low);
            batteryUsage.setText("Low");
        }
        else if(result.equals("Social"))
        {
            batteryImage.setImageResource(R.drawable.meduim);
            batteryUsage.setText("Medium");
        }
        else
        {
            batteryImage.setImageResource(R.drawable.high);
            batteryUsage.setText("High");
        }

    }
}
