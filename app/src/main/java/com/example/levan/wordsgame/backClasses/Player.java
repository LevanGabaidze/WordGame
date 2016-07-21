package com.example.levan.wordsgame.backClasses;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by levan on 7/20/2016.
 */
public class Player extends Thread {
    private Handler mHanlder;
    private int money;
    private GameController gm;
    private OriginalAI ai;
    private int myNumber;


    public Player(GameController gm,OriginalAI ai,int playerNumber){
        this.gm=gm;
        this.ai=ai;
        money=DataStore.startMoney;
        this.myNumber=playerNumber;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        mHanlder=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String cheker=msg.getData().getString(DataStore.requestTypeFlag);
                switch (cheker){
                    case DataStore.askAnswer: giveAnswer((String)msg.getData().get(DataStore.sentStringKey)); break;
                    case DataStore.askRise:   rise();break;
                    case DataStore.askToAccetptRise: acceptRise(); break;
                    default: break;
                }
            }
        } ;

        Looper.loop();
    }


    public Handler getmHanlder() {
        return mHanlder;
    }

    private void acceptRise() {
        if(ai.acceptRise()){
            gm.acceptBid(myNumber);
        }
    }

    private void rise() {
        if(ai.riseOrNot()){
            gm.riseBid(myNumber);
        }
    }

    private void giveAnswer(String str) {
        String answer=ai.getWord(str);
        gm.setAnswer(myNumber,answer);
    }

    public void setMoney(int mo){
        money=mo;

    }
    public int getMoney(){
        return money;
    }

}
