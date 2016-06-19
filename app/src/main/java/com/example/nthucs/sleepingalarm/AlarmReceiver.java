package com.example.nthucs.sleepingalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
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
            /*final View item = LayoutInflater.from(context).inflate(R.layout.alert_dialog_ring_alarm, null);
            new AlertDialog.Builder(context)
                    .setTitle("Alarm Ring")
                    .setView(item)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close voice;
                        }
                    })
                    .show();*/
        }
    }
}
