package com.example.nthucs.sleepingalarm;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_choose);
        getSupportActionBar().setTitle("Choose Ring");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        fileNames = new ArrayList<String>();

        filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Toast.makeText(MusicChooseActivity.this, filePath, Toast.LENGTH_LONG).show();
        getUnderPathFiles();

        ListView fileList = (ListView)findViewById(R.id.dataList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames);
        fileList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(fileNames.get(position).toString() == "Back to Previous"){
                    backToPrevious();
                    adapter.notifyDataSetChanged();
                }
                else if(isDir(filePath + "/" + fileNames.get(position))){
                    goToNext(filePath + "/" + fileNames.get(position));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MusicChooseActivity.this, filePath + "/" + fileNames.get(position), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public boolean isDir(String p){
        File file = new File(p);
        if(file.isDirectory()) return true;
        else return false;
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
        Toast.makeText(MusicChooseActivity.this, filePath, Toast.LENGTH_SHORT).show();
    }

    public void goToNext(String p){
        resetFileNames();
        filePath = p;
        getUnderPathFiles();
        adapter.notifyDataSetChanged();
        Toast.makeText(MusicChooseActivity.this, p, Toast.LENGTH_SHORT).show();
    }

    public void getUnderPathFiles(){
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
}
