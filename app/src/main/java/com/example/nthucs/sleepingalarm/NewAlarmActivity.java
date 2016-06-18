package com.example.nthucs.sleepingalarm;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        final EditText timeBeSet = (EditText)findViewById(R.id.alarmTime);

        Button setOK = (Button)findViewById(R.id.OKbutton);

        setOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String returnValue = timeBeSet.getText().toString();
                //建立包裹，放入回傳值。
                Bundle argument = new Bundle();
                argument.putString("returnValue", returnValue);

                //取出上一個Activity傳過來的 Intent 物件。
                Intent intent = getIntent();
                //放入要回傳的包裹。
                intent.putExtras(argument);

                //設定回傳狀態。
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
