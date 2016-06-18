package com.example.nthucs.sleepingalarm;

/**
 * Created by NTHUCS on 2016/6/18.
 */
public class Alarm_Item {

    private int hour;
    private int minute;
    public boolean[] weekStart = new boolean[7];

    public Alarm_Item(int hrInput, int minInput){
        setHour(hrInput);
        setMinute(minInput);
        for(int i = 0 ; i < 7 ; i++){
            weekStart[i] = false;
        }
    }

    public void setHour(int hrInput){
        hour = hrInput;
    }

    public int getHour(){
        return hour;
    }

    public void setMinute(int minInput){
        minute = minInput;
    }

    public int getMinute(){
        return minute;
    }
}
