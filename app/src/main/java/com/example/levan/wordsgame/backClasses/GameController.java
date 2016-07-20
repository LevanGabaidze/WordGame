package com.example.levan.wordsgame.backClasses;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by levan on 7/19/2016.
 */
public class GameController  extends Thread {


    private ArrayList<Player> players;
    private int nPlayer;
    private boolean[] responseGiven;
    private String[] response;
    private boolean[] bidRisen;
    private boolean[] riseAccepted;
    private long staredWaiting;




    public void GameController(int nPlayer, ArrayList<Player> players){
        this.players=players;
        this.nPlayer=nPlayer;
        responseGiven=new boolean[nPlayer+1];
        response=new String[nPlayer+1];
        riseAccepted=new boolean[nPlayer+1];
        bidRisen=new boolean[nPlayer+1];
    }


    public synchronized void   setAnswer(int player,String answer){
        if((new Date().getTime()-staredWaiting)>40000)
            return;

        responseGiven[player]=true;
        response[player]=answer;

    }


    public synchronized void   riseBid(int player){
        if((new Date().getTime()-staredWaiting)>10000)
            return;
        bidRisen[player]=true;
    }

    public synchronized void   acceptBid(int player){
        if((new Date().getTime()-staredWaiting)>10000)
            return;
        riseAccepted[player]=true;
    }




}
