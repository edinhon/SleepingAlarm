package com.example.nthucs.sleepingalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by user on 2016/6/19.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        if(b.get("msg").equals("ring_alarm")) {
            Intent goToRing = new Intent(context, RingAlarmDialogActivity.class);
            goToRing.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goToRing);
        }
    }
}
