package com.example.nthucs.sleepingalarm;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

public class RingAlarmDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_alarm_dialog);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        //Show Toast.
        Toast.makeText(this, "Ring", Toast.LENGTH_LONG).show();

        //Let device vibrate.
        final Vibrator vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        if(vibrator.hasVibrator()){
            vibrator.vibrate(new long[]{1000, 1000}, 0);
        }

        //Play music.
        //MediaPlayer mp = new MediaPlayer();
        /*try {
            mp.setDataSource("");
            mp.start();
            mp.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.shot);
        mp.start();


        //Show dialog.
        final View item = LayoutInflater.from(this).inflate(R.layout.alert_dialog_ring_alarm, null);
        new AlertDialog.Builder(this)
                .setTitle("Alarm Ring")
                .setView(item)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vibrator.cancel();
                        mp.stop();
                        finish();
                    }
                })
                .show();
    }
}
