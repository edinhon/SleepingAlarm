package com.example.nthucs.sleepingalarm;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewAlarmActivity extends AppCompatActivity {
    int hour, minute;
    int index;
    boolean[] weekStart;
    String showTimeText;
    String ringDataPath;
    TextView timeBeSet;
    TextView repeatBeSet;
    TextView ringBeSet;
    int numberTimeTicket, numberRingTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        getSupportActionBar().setTitle("Alarm Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Take argument from bundle.
        Bundle alarmBundle = getIntent().getBundleExtra("AlarmBundle");
        hour = alarmBundle.getInt("Hour");
        minute = alarmBundle.getInt("Minute");
        index = alarmBundle.getInt("Index");
        weekStart = alarmBundle.getBooleanArray("WeekStart");
        showTimeText = alarmBundle.getString("ShowTimeText");
        ringDataPath = alarmBundle.getString("RingDataPath");
        Bundle parameterBundle = getIntent().getBundleExtra("ParameterBundle");
        numberTimeTicket = parameterBundle.getInt("NumberTimeTicket");
        numberRingTicket = parameterBundle.getInt("NumberRingTicket");

        //Set TextView by argument.
        //Set time text.
        timeBeSet = (TextView)findViewById(R.id.textTime);
        timeBeSet.setText(showTimeText);
        //Set repeat text
        repeatBeSet = (TextView)findViewById(R.id.textRepeat);
        String temp = "";
        for(int i = 0 ; i < 7 ; i++){
            if(weekStart[i] && !temp.equals("")){
                temp += (", " + (i+1));
            }
            else if(weekStart[i] && temp.equals("")){
                temp += (i+1);
            }
        }
        if(!temp.equals(""))repeatBeSet.setText(temp);
        else repeatBeSet.setText("None Repeat");
        //Set ring text.
        ringBeSet = (TextView)findViewById(R.id.textRing);
        String temp2;
        char[] tempCA = ringDataPath.toCharArray();
        int startP = 0;
        if(tempCA.length != 0){
            for(int i = tempCA.length-1 ; i >= 0 ; i--){
                if(tempCA[i] == '/'){
                    startP = i;
                    break;
                }
            }
            temp2 = ringDataPath.substring(startP+1, tempCA.length);
        }
        else temp2 = "Default Ring";
        ringBeSet.setText(temp2);

        //Let click text to change alarm time.
        final TimePickerDialog timePickerDialog = new TimePickerDialog(NewAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String temp;
                if (hourOfDay >= 12) {
                    if ((hourOfDay - 12) >= 10) temp = "PM " + (hourOfDay - 12);
                    else temp = "PM 0" + (hourOfDay - 12);

                    if (minute >= 10) temp += " : " + minute;
                    else temp += " : 0" + minute;
                } else {
                    if (hourOfDay >= 10) temp = "AM " + hourOfDay;
                    else temp = "AM 0" + hourOfDay;

                    if (minute >= 10) temp += " : " + minute;
                    else temp += " : 0" + minute;
                }
                timeBeSet.setText(temp);
                NewAlarmActivity.this.hour = hourOfDay;
                NewAlarmActivity.this.minute = minute;
                NewAlarmActivity.this.showTimeText = temp;
            }
        }, hour, minute, false);
        timeBeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index >= 7){
                    timePickerDialog.show();
                } else {

                    new AlertDialog.Builder(NewAlarmActivity.this)
                            .setTitle("Want to set time ?")
                            .setMessage("That will consume a time ticket. You have " + numberTimeTicket)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (numberTimeTicket >= 1) {
                                        numberTimeTicket--;
                                        timePickerDialog.show();
                                    } else {
                                        new AlertDialog.Builder(NewAlarmActivity.this)
                                                .setTitle("Fail")
                                                .setMessage("You haven't enough ticket.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });




        //Let click text to change repeat days.
        /*repeatBeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSetRepeat = new Intent();

                Bundle weekBundle = new Bundle();
                weekBundle.putBooleanArray("WeekStart", weekStart);

                goToSetRepeat.putExtra("WeekBundle", weekBundle);

                goToSetRepeat.setClass(NewAlarmActivity.this, SetRepeatActivity.class);
                NewAlarmActivity.this.startActivityForResult(goToSetRepeat, 0);
            }
        });*/

        //Let click text to change rings.
        ringBeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index >= 7){
                    Intent goToSetRings = new Intent();

                    Bundle ringBundle = new Bundle();
                    ringBundle.putString("RingDataPath", ringDataPath);

                    goToSetRings.putExtra("RingBundle", ringBundle);

                    goToSetRings.setClass(NewAlarmActivity.this, MusicChooseActivity.class);
                    NewAlarmActivity.this.startActivityForResult(goToSetRings, 1);
                } else {

                    new AlertDialog.Builder(NewAlarmActivity.this)
                            .setTitle("Want to set ring ?")
                            .setMessage("That will consume a ring ticket. You have " + numberRingTicket)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (numberRingTicket >= 1) {

                                        numberRingTicket--;
                                        Intent goToSetRings = new Intent();

                                        Bundle ringBundle = new Bundle();
                                        ringBundle.putString("RingDataPath", ringDataPath);

                                        goToSetRings.putExtra("RingBundle", ringBundle);

                                        goToSetRings.setClass(NewAlarmActivity.this, MusicChooseActivity.class);
                                        NewAlarmActivity.this.startActivityForResult(goToSetRings, 1);

                                    } else {

                                        new AlertDialog.Builder(NewAlarmActivity.this)
                                                .setTitle("Fail")
                                                .setMessage("You haven't enough ticket.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });




        Button setOK = (Button)findViewById(R.id.OKbutton1);
        setOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //建立包裹，放入回傳值。
                Bundle returnBundle = new Bundle();
                returnBundle.putInt("Index", index);
                returnBundle.putInt("Hour", hour);
                returnBundle.putInt("Minute", minute);
                returnBundle.putBooleanArray("WeekStart", weekStart);
                returnBundle.putString("ShowTimeText", showTimeText);
                returnBundle.putString("RingDataPath", ringDataPath);
                Bundle parameterBundle = new Bundle();
                parameterBundle.putInt("NumberTimeTicket", numberTimeTicket);
                parameterBundle.putInt("NumberRingTicket", numberRingTicket);

                //取出上一個Activity傳過來的 Intent 物件。
                Intent intent = getIntent();
                //放入要回傳的包裹。
                intent.putExtra("ReturnBundle", returnBundle);
                intent.putExtra("ParameterBundle", parameterBundle);

                //設定回傳狀態。
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Set repeat successful
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            //將包裹從 Intent 中取出。
            Bundle weekBundle = data.getBundleExtra("WeekBundle");
            //將回傳值用指定的 key 取出
            weekStart = weekBundle.getBooleanArray("WeekStart");

            repeatBeSet = (TextView)findViewById(R.id.textRepeat);
            String temp = "";
            for(int i = 0 ; i < 7 ; i++){
                if(weekStart[i] && !temp .equals("")){
                    temp += (", " + (i+1));
                }
                else if(weekStart[i] && temp.equals("")){
                    temp += (i+1);
                }
            }
            if(!temp.equals(""))repeatBeSet.setText(temp);
            else repeatBeSet.setText("None Repeat");
        }

        //Set ring successful
        else if(resultCode == Activity.RESULT_OK && requestCode == 1){
            Bundle ringBundle = data.getBundleExtra("RingBundle");
            //將回傳值用指定的 key 取出
            ringDataPath = ringBundle.getString("RingDataPath");

            ringBeSet = (TextView)findViewById(R.id.textRing);
            String temp = "";
            char[] tempCA = ringDataPath.toCharArray();
            int startP = 0;
            if(tempCA.length != 0){
                for(int i = tempCA.length-1 ; i >= 0 ; i--){
                    if(tempCA[i] == '/'){
                        startP = i;
                        break;
                    }
                }
                temp = ringDataPath.substring(startP+1, tempCA.length);
            }
            ringBeSet.setText(temp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
