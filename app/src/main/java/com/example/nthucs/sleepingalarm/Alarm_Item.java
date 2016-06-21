package com.example.nthucs.sleepingalarm;

/**
 * Created by NTHUCS on 2016/6/18.
 */
public class Alarm_Item {

    private int hour;
    private int minute;
    private String showTimeText;
    private long id;
    public boolean[] weekStart = new boolean[7];
    public String ringDataPath;

    public Alarm_Item(int hrInput, int minInput, String text){
        setHour(hrInput);
        setMinute(minInput);
        setText(text);
        ringDataPath = "";
        for(int i = 0 ; i < 7 ; i++){
            weekStart[i] = false;
        }
    }

    public void setId(long idInput){
        id = idInput;
    }

    public long getId(){
        return id;
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

    public void setText(String text){
        showTimeText = text;
    }

    public String getText(){
        return showTimeText;
    }

    public void setRingPath(String ringInput){
        ringDataPath = ringInput;
    }

    public String getRingPath(){
        return ringDataPath;
    }
}
