package com.example.myalarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    TimePicker myTimePicker;
    Button setAlarmButton;
    Button cancelAlarmButton;
    AlarmManager aManager;
    PendingIntent pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTimePicker = findViewById(R.id.mTimePicker);
        setAlarmButton = findViewById(R.id.setAlarmButton);
        cancelAlarmButton = findViewById(R.id.cancelAlarmButton);

        deviceBootReceiver();

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                if(Build.VERSION.SDK_INT>=23)
                myCalender.set(myCalender.get(Calendar.YEAR),myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_WEEK),myTimePicker.getHour(),myTimePicker.getMinute(),0);
                else
                {
                    myCalender.set(myCalender.get(Calendar.YEAR),myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_WEEK),myTimePicker.getCurrentHour(),myTimePicker.getCurrentMinute(),0);
                }
                setAlarm(myCalender.getTimeInMillis());
            }
        });


        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });

    }

    private void setAlarm(long timeInMillis) {
        aManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,MyAlarm.class);

        pIntent =  PendingIntent.getBroadcast(this,0,intent,0);
        aManager.setRepeating(AlarmManager.RTC,timeInMillis,AlarmManager.INTERVAL_DAY,pIntent);

        Toast.makeText(this,"Alarm set",Toast.LENGTH_SHORT).show();
    }

    private void cancelAlarm() {
        // If the alarm has been set, cancel it.
        if (aManager!= null) {
            aManager.cancel(pIntent);
            Toast.makeText(this,"Alarm Canceled",Toast.LENGTH_SHORT).show();
        }
    }

    private void deviceBootReceiver() {
        ComponentName receiver = new ComponentName(getApplicationContext(), SampleBootReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

}
