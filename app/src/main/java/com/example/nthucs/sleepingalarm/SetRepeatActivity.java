package com.example.nthucs.sleepingalarm;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SetRepeatActivity extends AppCompatActivity {

    boolean[] weekStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_repeat);

        weekStart = getIntent().getBundleExtra("WeekBundle").getBooleanArray("WeekStart");

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, days);
        ListView v = (ListView)findViewById(R.id.repeatChoose);
        v.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        v.setAdapter(adapter);

        for(int i = 0 ; i < 7 ; i++){
            v.setItemChecked(i, weekStart[i]);
        }

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weekStart[position] = !weekStart[position];
            }
        });

        Button setOK = (Button)findViewById(R.id.OKbutton2);
        setOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle weekBundle = new Bundle();
                weekBundle.putBooleanArray("WeekStart", weekStart);

                Intent intent = getIntent();
                //放入要回傳的包裹。
                intent.putExtra("WeekBundle", weekBundle);

                //設定回傳狀態。
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
