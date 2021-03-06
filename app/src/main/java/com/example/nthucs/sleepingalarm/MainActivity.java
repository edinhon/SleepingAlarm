package com.example.nthucs.sleepingalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> alarmTimeList = new ArrayList<>();
    ArrayList<Alarm_Item> alarmList = new ArrayList<>();
    ArrayAdapter adapter;
    TimePickerDialog timePickerDialog;
    FloatingActionButton addButton;
    Alarm_Item_DBSet dbSet;
    Parameter_DBSet p_dbSet;
    ArrayList<Parameter> parameterList = new ArrayList<>();
    Parameter parameter;

    public CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.nthucs.sleepingalarm",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", "ERROR");
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", "ERROR");
        }

        setVolumeControlStream(AudioManager.STREAM_ALARM);

        // 建立資料庫物件
        dbSet = new Alarm_Item_DBSet(getApplicationContext());
        p_dbSet = new Parameter_DBSet(getApplicationContext());

        // 取得所有記事資料
        //If new application, create a new Parameter.
        if (p_dbSet.getCount() == 0) {
            parameter = new Parameter(1000, 7, 7);
            parameter = p_dbSet.insert(parameter);
            parameterList = p_dbSet.getAll();
        }
        //If not a new app, get Parameter.
        else {
            parameterList = p_dbSet.getAll();
            for (Parameter p : parameterList) {
                parameter = p;
            }
        }
        //If new app, create 7 alarm item.
        if(dbSet.getCount() == 0){
            Alarm_Item a = new Alarm_Item(0, 0, "AM 00 : 00");
            for(int i = 0 ; i < 7 ; i++){
                a.weekStart[i] = true;
                if(i != 0) a.weekStart[i-1] = false;
                dbSet.insert(a);
            }
            alarmList = dbSet.getAll();
            notFullAlarmTextList();
        } else if (!parameter.isAllSet()){
            alarmList = dbSet.getAll();
            notFullAlarmTextList();
        }
        else {
            alarmList = dbSet.getAll();
            turnItemListToTextList();
        }

        ListView mainList = (ListView) findViewById(R.id.MainAlarmListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alarmTimeList);
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
                alarmBundle.putString("RingDataPath", chosenAlarm.getRingPath());

                goToSetExistedAlarm.putExtra("AlarmBundle", alarmBundle);

                Bundle parameterBundle = new Bundle();
                parameterBundle.putInt("NumberTimeTicket", parameter.getNumberTimeTicket());
                parameterBundle.putInt("NumberRingTicket", parameter.getNumberRingTicket());

                goToSetExistedAlarm.putExtra("ParameterBundle", parameterBundle);

                goToSetExistedAlarm.setClass(MainActivity.this, NewAlarmActivity.class);
                MainActivity.this.startActivityForResult(goToSetExistedAlarm, 1);
            }
        });

        //Long click to delete alarm.
        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position > 6) {

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete")
                            .setMessage("Do you want to delete this alarm ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbSet.delete(alarmList.get(position).getId());
                                    removeAlarmInSystem(alarmList.get(position).getId());
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
                }
                return true;
            }
        });

        //New a alarm.
        final GregorianCalendar calendar = new GregorianCalendar();
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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
                alarmTimeList.add(temp);

                Alarm_Item newAlarm = new Alarm_Item(hourOfDay, minute, temp);
                newAlarm = dbSet.insert(newAlarm);
                alarmList.add(newAlarm);

                adapter.notifyDataSetChanged();

                newAlarmInSystem(hourOfDay, minute, newAlarm.getId(), newAlarm.getRingPath());

            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.updateTime(GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        GregorianCalendar.getInstance().get(Calendar.MINUTE));
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

    protected void facebookSDKInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
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
            for (int i = 0; i < 7; i++) {
                alarmBeSet.weekStart[i] = weekStart[i];
            }
            alarmBeSet.setRingPath(alarmBundle.getString("RingDataPath"));
            alarmBeSet.setHaveSet(true);
            dbSet.update(alarmBeSet);
            if(index >= 7){
                alarmTimeList.add(index, alarmBundle.getString("ShowTimeText"));
            } else {
                String day = "";
                switch (index) {
                    case 0:
                        day = "Monday ";
                        break;
                    case 1:
                        day = "Tuesday ";
                        break;
                    case 2:
                        day = "Wednesday ";
                        break;
                    case 3:
                        day = "Thursday ";
                        break;
                    case 4:
                        day = "Friday ";
                        break;
                    case 5:
                        day = "Saturday ";
                        break;
                    case 6:
                        day = "Sunday ";
                        break;
                }
                alarmTimeList.add(index, day + alarmBundle.getString("ShowTimeText"));
            }

            adapter.notifyDataSetChanged();
            removeAlarmInSystem(alarmBeSet.getId());
            newAlarmInSystem(alarmBeSet.getHour(), alarmBeSet.getMinute(), alarmBeSet.getId(), alarmBeSet.getRingPath());

            Bundle parameterBundle = data.getBundleExtra("ParameterBundle");
            parameter.setNumberTimeTicket(parameterBundle.getInt("NumberTimeTicket"));
            parameter.setNumberRingTicket(parameterBundle.getInt("NumberRingTicket"));

            p_dbSet.update(parameter);
        } else if(resultCode == Activity.RESULT_OK && requestCode == 2){

            Bundle parameterBundle = data.getBundleExtra("ParameterBundle");
            parameter.setMoney(parameterBundle.getInt("Money"));
            parameter.setNumberTimeTicket(parameterBundle.getInt("NumberTimeTicket"));
            parameter.setNumberRingTicket(parameterBundle.getInt("NumberRingTicket"));

            p_dbSet.update(parameter);

        }
    }

    public void turnItemListToTextList() {
        int i = 0;
        for (Alarm_Item a : alarmList) {
            String day = "";
            switch (i) {
                case 0:
                    day = "Monday ";
                    break;
                case 1:
                    day = "Tuesday ";
                    break;
                case 2:
                    day = "Wednesday ";
                    break;
                case 3:
                    day = "Thursday ";
                    break;
                case 4:
                    day = "Friday ";
                    break;
                case 5:
                    day = "Saturday ";
                    break;
                case 6:
                    day = "Sunday ";
                    break;
            }
            i++;
            alarmTimeList.add(day + a.getText());
        }
    }

    public void notFullAlarmTextList(){
        int i = 0;
        for (Alarm_Item a : alarmList) {

            if(a.isHaveSet() == true && i < 7){
                String day = "";
                switch (i) {
                    case 0:
                        day = "Monday ";
                        break;
                    case 1:
                        day = "Tuesday ";
                        break;
                    case 2:
                        day = "Wednesday ";
                        break;
                    case 3:
                        day = "Thursday ";
                        break;
                    case 4:
                        day = "Friday ";
                        break;
                    case 5:
                        day = "Saturday ";
                        break;
                    case 6:
                        day = "Sunday ";
                        break;
                }
                alarmTimeList.add(day + a.getText());
            } else if(i < 7){
                String day = "";
                switch (i) {
                    case 0:
                        day = "Monday";
                        break;
                    case 1:
                        day = "Tuesday";
                        break;
                    case 2:
                        day = "Wednesday";
                        break;
                    case 3:
                        day = "Thursday";
                        break;
                    case 4:
                        day = "Friday";
                        break;
                    case 5:
                        day = "Saturday";
                        break;
                    case 6:
                        day = "Sunday";
                        break;
                }
                alarmTimeList.add(day);
            } else {
                alarmTimeList.add(a.getText());
            }
            i++;
        }
    }

    public void newAlarmInSystem(int hour, int minute, long id, String ringDataPath) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        if (minute < currentMinute) {
            --hour;
            minute = minute - currentMinute + 60;
            hour = hour - currentHour;
        } else {
            minute = minute - currentMinute;
            hour = hour - currentHour;
        }
        if (hour < 0) {
            hour += 24;
        }
        Toast.makeText(MainActivity.this, "Alarm will ring after" + hour + "hr" + minute + "min", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg", "ring_alarm");

        intent.putExtra("RingDataPath", ringDataPath);

        PendingIntent pi = PendingIntent.getBroadcast(this, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        long nowTime = calendar.getTimeInMillis();
        nowTime = calendar.getTimeInMillis() - (nowTime % 60000);
        am.set(AlarmManager.RTC_WAKEUP, nowTime + (hour * 60 + minute) * 60 * 1000, pi);
    }

    public void removeAlarmInSystem(long id) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("msg", "ring_alarm");

        PendingIntent pi = PendingIntent.getBroadcast(this, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pi);
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
        if (id == R.id.push) {
            Calendar calendar = Calendar.getInstance();
            parameter.setSleepTime(calendar.getTimeInMillis());
            p_dbSet.update(parameter);
            return true;
        } else if(id == R.id.pull){
            Calendar calendar = Calendar.getInstance();
            long earnMoney = (calendar.getTimeInMillis() - parameter.getSleepTime()) / (1000 * 60);
            int nowMoney = parameter.getMoney();
            parameter.setMoney(nowMoney + (int)earnMoney);
            parameter.setSleepTime(calendar.getTimeInMillis());
            p_dbSet.update(parameter);
            Toast.makeText(MainActivity.this, "You earn " + earnMoney + " !", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_login) {

            fragment = new FragmentLogin();
            FrameLayout f = (FrameLayout) findViewById(R.id.mainFrame);
            f.setVisibility(View.VISIBLE);
            ListView mainList = (ListView) findViewById(R.id.MainAlarmListView);
            mainList.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);

        } else if (id == R.id.nav_home) {

            FrameLayout f = (FrameLayout) findViewById(R.id.mainFrame);
            f.setVisibility(View.GONE);
            ListView mainList = (ListView) findViewById(R.id.MainAlarmListView);
            mainList.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_shop) {

            Intent goToShop = new Intent();

            Bundle parameterBundle = new Bundle();
            parameterBundle.putInt("Money", parameter.getMoney());
            parameterBundle.putInt("NumberTimeTicket", parameter.getNumberTimeTicket());
            parameterBundle.putInt("NumberRingTicket", parameter.getNumberRingTicket());

            goToShop.putExtra("ParameterBundle", parameterBundle);

            goToShop.setClass(MainActivity.this, ShopActivity.class);
            MainActivity.this.startActivityForResult(goToShop, 2);

        } else if (id == R.id.nav_option) {

            fragment = new FragmentOption();
            FrameLayout f = (FrameLayout) findViewById(R.id.mainFrame);
            f.setVisibility(View.VISIBLE);
            ListView mainList = (ListView) findViewById(R.id.MainAlarmListView);
            mainList.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);

        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (ft != null && fragment != null) {
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(getApplication());
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.activateApp(getApplication());
    }


}
