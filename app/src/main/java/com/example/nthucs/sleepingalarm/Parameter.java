package com.example.nthucs.sleepingalarm;

/**
 * Created by NTHUCS on 2016/6/22.
 */
public class Parameter {

    private boolean vibratable;
    private int money;
    private int numberTimeTicket;
    private int numberRingTicket;
    private long id;

    public Parameter(int m, int ntt, int nrt){
        setMoney(m);
        setNumberTimeTicket(ntt);
        setNumberRingTicket(nrt);
        setVibratable(true);
    }

    public void setVibratable(boolean input){
        vibratable = input;
    }

    public boolean isVibratable() {
        return vibratable;
    }

    public void setMoney(int input){
        money = input;
    }

    public int getMoney(){
        return money;
    }

    public void setNumberTimeTicket(int input){
        numberTimeTicket = input;
    }

    public int getNumberTimeTicket(){
        return numberTimeTicket;
    }

    public void setNumberRingTicket(int input){
        numberRingTicket = input;
    }

    public int getNumberRingTicket(){
        return numberRingTicket;
    }

    public void setId(long input){
        id = input;
    }

    public long getId(){
        return id;
    }

}
