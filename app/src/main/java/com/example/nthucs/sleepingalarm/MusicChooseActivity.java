package com.example.nthucs.sleepingalarm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MusicChooseActivity extends AppCompatActivity {

    ArrayList<String> fileNames;
    ArrayAdapter adapter;
    String filePath;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_choose);
        getSupportActionBar().setTitle("Choose Ring");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED) ){
            Toast.makeText(MusicChooseActivity.this, "SD card Exist", Toast.LENGTH_SHORT).show();
        }


        fileNames = new ArrayList<>();

        Bundle ringBundle = getIntent().getBundleExtra("RingBundle");
        String ringDataPath = ringBundle.getString("RingDataPath");
        if(ringDataPath != null && !ringDataPath.equals("")){
            String temp2 = ringDataPath;
            char[] temp = ringDataPath.toCharArray();
            int endP = temp.length;
            for(int i = temp.length-1 ; i >= 0 ; i--){
                if(temp[i] == '/'){
                    endP = i;
                    break;
                }
            }
            filePath = temp2.substring(0, endP);
        }else {
            filePath = Environment.getExternalStorageDirectory().getPath();
        }
        getUnderPathFiles();

        ListView fileList = (ListView)findViewById(R.id.dataList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileNames);
        fileList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (fileNames.get(position).equals("Back to Previous")) {
                    backToPrevious();
                    adapter.notifyDataSetChanged();
                } else if (isDir(filePath + "/" + fileNames.get(position))) {
                    goToNext(filePath + "/" + fileNames.get(position));
                    adapter.notifyDataSetChanged();
                } else {
                    Bundle ringBundle = new Bundle();
                    ringBundle.putString("RingDataPath", filePath + "/" + fileNames.get(position));

                    Intent intent = getIntent();
                    //放入要回傳的包裹。
                    intent.putExtra("RingBundle", ringBundle);

                    //設定回傳狀態。
                    setResult(Activity.RESULT_OK, intent);
                    Toast.makeText(MusicChooseActivity.this, filePath + "/" + fileNames.get(position), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    public boolean isDir(String p){
        File file = new File(p);
        return file.isDirectory();
    }

    public void backToPrevious(){
        resetFileNames();
        char[] temp = filePath.toCharArray();
        int endP = temp.length;
        for(int i = temp.length-1 ; i >= 0 ; i--){
            if(temp[i] == '/'){
                endP = i;
                break;
            }
        }
        filePath = filePath.substring(0, endP);
        getUnderPathFiles();
        //Toast.makeText(MusicChooseActivity.this, filePath, Toast.LENGTH_SHORT).show();
    }

    public void goToNext(String p){
        resetFileNames();
        filePath = p;
        getUnderPathFiles();
        //Toast.makeText(MusicChooseActivity.this, filePath, Toast.LENGTH_SHORT).show();
    }

    public void getUnderPathFiles(){
        verifyStoragePermissions(MusicChooseActivity.this);
        File file = new File(filePath);
        File[] files = file.listFiles();

        if(files != null){
            fileNames.add("Back to Previous");
            for(File f : files){
                fileNames.add(f.getName());
            }
        }
        else{
            fileNames.add("Back to Previous");
        }
    }

    public void resetFileNames(){
        fileNames.clear();
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
