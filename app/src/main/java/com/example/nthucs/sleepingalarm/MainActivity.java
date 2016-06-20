package com.example.nthucs.sleepingalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> alarmTimeList = new ArrayList<String>();
    ArrayList<Alarm_Item> alarmList = new ArrayList<Alarm_Item>();
    ArrayAdapter adapter;
    TimePickerDialog timePickerDialog;
    FloatingActionButton addButton;
    Alarm_Item_DBSet dbSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 建立資料庫物件
        dbSet = new Alarm_Item_DBSet(getApplicationContext());

        // 取得所有記事資料
        alarmList = dbSet.getAll();
        turnItemListToTextList();

        ListView mainList = (ListView) findViewById(R.id.MainAlarmListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alarmTimeList);
        mainList.setAdapter(adapter);

        //Click Item to set alarm detail.
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent goToSetExistedAlarm = new Intent();

                Bundle alarmBundle = new Bundle();
                Alarm_Item chosenAlarm = alarmList.get(position);
                alarmBundle.putInt("Hour", chosenAlarm.getHour());
                alarmBundle.putInt("Minute", chosenAlarm.getMinute());
                alarmBundle.putBooleanArray("WeekStart", chosenAlarm.weekStart);
                alarmBundle.putInt("Index", position);
                alarmBundle.putString("ShowTimeText", chosenAlarm.getText());

                goToSetExistedAlarm.putExtra("AlarmBundle", alarmBundle);

                goToSetExistedAlarm.setClass(MainActivity.this, NewAlarmActivity.class);
                MainActivity.this.startActivityForResult(goToSetExistedAlarm, 0);
            }
        });

        //Long click to delete alarm.
        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this alarm ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbSet.delete(alarmList.get(position).getId());
                                alarmList.remove(position);
                                alarmTimeList.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Not Delete", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

                return false;
            }
        });

        //New a alarm.
        GregorianCalendar calendar = new GregorianCalendar();
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
                alarmTimeList.add(temp);

                Alarm_Item newAlarm = new Alarm_Item(hourOfDay, minute, temp);
                newAlarm = dbSet.insert(newAlarm);
                alarmList.add(newAlarm);

                adapter.notifyDataSetChanged();

                newAlarmInSystem(hourOfDay, minute);

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
                adapter.notifyDataSetChanged();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //將包裹從 Intent 中取出。
            Bundle alarmBundle = data.getBundleExtra("ReturnBundle");
            //將回傳值用指定的 key 取出
            final int index = alarmBundle.getInt("Index");
            Alarm_Item alarmBeSet = alarmList.get(index);
            alarmTimeList.remove(index);

            alarmBeSet.setHour(alarmBundle.getInt("Hour"));
            alarmBeSet.setMinute(alarmBundle.getInt("Minute"));
            final boolean[] weekStart = alarmBundle.getBooleanArray("WeekStart");
            alarmBeSet.setText(alarmBundle.getString("ShowTimeText"));
            for(int i = 0 ; i < 7 ; i++){
                alarmBeSet.weekStart[i] = weekStart[i];
            }
            dbSet.update(alarmBeSet);
            alarmTimeList.add(index, alarmBundle.getString("ShowTimeText"));
            adapter.notifyDataSetChanged();
        }
    }

    public void turnItemListToTextList(){
        for(Alarm_Item a : alarmList){
            alarmTimeList.add(a.getText());
        }
    }

    public void newAlarmInSystem(int hour, int minute){
        Calendar cal = Calendar.getInstance();
        // 設定於 3 分鐘後執行
        cal.add(Calendar.SECOND, 5);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg", "ring_alarm");

        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
