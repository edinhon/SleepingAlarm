package com.example.nthucs.sleepingalarm;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
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
import java.util.ArrayList;

public class RingAlarmDialogActivity extends Activity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    MediaPlayer mp;
    Parameter_DBSet p_dbSet;
    ArrayList<Parameter> parameterList = new ArrayList<>();
    Parameter parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_alarm_dialog);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        p_dbSet = new Parameter_DBSet(getApplicationContext());
        if (p_dbSet.getCount() == 0) {
            parameter = new Parameter(100, 0, 0);
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

        //Show Toast.
        Toast.makeText(this, "Ring", Toast.LENGTH_LONG).show();

        //Let device vibrate.
        final Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator() && parameter.isVibratable()) {
            vibrator.vibrate(new long[]{1000, 1000}, 0);
        }

        //Play music.
        String ringDataPath = getIntent().getExtras().getString("RingDataPath");
        //Toast.makeText(this, ringDataPath, Toast.LENGTH_LONG).show();

        setVolumeControlStream(AudioManager.STREAM_ALARM);
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_NORMAL);

        if (ringDataPath != null && !ringDataPath.equals("")) {
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
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
            } catch (NullPointerException e) {
                e.printStackTrace();
                mp = MediaPlayer.create(this, R.raw.shot);
                mp.start();
            }
        } else {
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
