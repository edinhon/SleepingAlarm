package com.example.nthucs.sleepingalarm;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    MediaPlayer mp;

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
        String ringDataPath = getIntent().getExtras().getString("RingDataPath");
        Toast.makeText(this, ringDataPath, Toast.LENGTH_LONG).show();

        if(ringDataPath != ""){
            mp = new MediaPlayer();
            try {
                verifyStoragePermissions(RingAlarmDialogActivity.this);
                mp.setDataSource(ringDataPath);
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer player) {
                        player.start();
                        player.setLooping(true);
                    }
                });
                mp.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                mp = MediaPlayer.create(this, R.raw.shot);
                mp.start();
            } catch (NullPointerException e){
                e.printStackTrace();
                mp = MediaPlayer.create(this, R.raw.shot);
                mp.start();
            }
        }
        else{
            mp = MediaPlayer.create(this, R.raw.shot);
            mp.start();
        }


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
                        mp.release();
                        finish();
                    }
                })
                .show();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
