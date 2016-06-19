package com.example.nthucs.sleepingalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by user on 2016/6/19.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        if(b.get("msg").equals("ring_alarm")) {
            Toast.makeText(context, "Ring", Toast.LENGTH_LONG).show();


        }
    }
}
