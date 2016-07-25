package com.example.levan.wordsgame.backClasses;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by levan on 7/20/2016.
 */
public class Player extends Thread {
    private Handler mHanlder;
    private int money;
    private GameController gm;
    private OriginalAI ai;
    private int myNumber;


    public Player(OriginalAI ai,int playerNumber){
        this.ai=ai;
        money=DataStore.startMoney;
        this.myNumber=playerNumber;
    }

    public void setGamecontroller(GameController gm){
        this.gm=gm;
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

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gm.acceptBid(myNumber,true);
            setMoney( money-10);

        }else{
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gm.acceptBid(myNumber,false);

        }
    }

    private void rise() {
        if(ai.riseOrNot()){
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gm.riseBid(myNumber,true);
            setMoney(money-DataStore.bidSequence[gm.getCurrentRound()]);
        }else{
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            gm.riseBid(myNumber,false);

        }
    }

    private void giveAnswer(String str) {
        String answer=ai.getWord(str);
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gm.setAnswer(myNumber,answer);
    }

    public void setMoney(int mo){
        money=mo;

    }
    public int getMoney(){
        return money;
    }

}
