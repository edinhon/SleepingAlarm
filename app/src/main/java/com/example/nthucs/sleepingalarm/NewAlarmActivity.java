package com.example.nthucs.sleepingalarm;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class NewAlarmActivity extends AppCompatActivity {
    int hour, minute;
    boolean[] weekStart;
    String showTimeText;
    TextView timeBeSet;
    TextView repeatBeSet;
    TextView ringBeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        getSupportActionBar().setTitle("Alarm Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Take argument from bundle.
        Bundle alarmBundle = getIntent().getBundleExtra("AlarmBundle");
        final int index = alarmBundle.getInt("Index");
        hour = alarmBundle.getInt("Hour");
        minute = alarmBundle.getInt("Minute");
        weekStart = alarmBundle.getBooleanArray("WeekStart");
        showTimeText = alarmBundle.getString("ShowTimeText");

        //Set TextView by argument.
        timeBeSet = (TextView)findViewById(R.id.textTime);
        timeBeSet.setText(showTimeText);
        repeatBeSet = (TextView)findViewById(R.id.textRepeat);
        String temp = "";
        for(int i = 0 ; i < 7 ; i++){
            if(weekStart[i] && temp != ""){
                temp += (", " + (i+1));
            }
            else if(weekStart[i] && temp == ""){
                temp += (i+1);
            }
        }
        if(temp != "")repeatBeSet.setText(temp);
        else repeatBeSet.setText("None Repeat");
        ringBeSet = (TextView)findViewById(R.id.textRing);

        //Let click text to change alarm time.
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String temp;
                if(hourOfDay >= 12){
                    if((hourOfDay-12) >= 10) temp = "PM " + (hourOfDay-12);
                    else temp = "PM 0" + (hourOfDay-12);

                    if(minute >= 10)temp += " : " + minute;
                    else temp += " : 0" + minute;
                }else{
                    if(hourOfDay >= 10) temp = "AM " + hourOfDay;
                    else temp = "AM 0" + hourOfDay;

                    if(minute >= 10)temp += " : " + minute;
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
                timePickerDialog.show();
            }
        });

        //Let click text to change repeat days.
        repeatBeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSetRepeat = new Intent();

                Bundle weekBundle = new Bundle();
                weekBundle.putBooleanArray("WeekStart", weekStart);

                goToSetRepeat.putExtra("WeekBundle", weekBundle);

                goToSetRepeat.setClass(NewAlarmActivity.this, SetRepeatActivity.class);
                NewAlarmActivity.this.startActivityForResult(goToSetRepeat, 0);
            }
        });

        //Let click text to change rings.
        ringBeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSetRings = new Intent();

                goToSetRings.setClass(NewAlarmActivity.this, MusicChooseActivity.class);
                NewAlarmActivity.this.startActivity(goToSetRings);
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

                //取出上一個Activity傳過來的 Intent 物件。
                Intent intent = getIntent();
                //放入要回傳的包裹。
                intent.putExtra("ReturnBundle", returnBundle);

                //設定回傳狀態。
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //將包裹從 Intent 中取出。
            Bundle weekBundle = data.getBundleExtra("WeekBundle");
            //將回傳值用指定的 key 取出
            weekStart = weekBundle.getBooleanArray("WeekStart");

            repeatBeSet = (TextView)findViewById(R.id.textRepeat);
            String temp = "";
            for(int i = 0 ; i < 7 ; i++){
                if(weekStart[i] && temp != ""){
                    temp += (", " + (i+1));
                }
                else if(weekStart[i] && temp == ""){
                    temp += (i+1);
                }
            }
            if(temp != "")repeatBeSet.setText(temp);
            else repeatBeSet.setText("None Repeat");
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
