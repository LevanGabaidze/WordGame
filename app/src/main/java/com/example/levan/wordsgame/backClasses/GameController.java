package com.example.levan.wordsgame.backClasses;

import android.os.Handler;

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
    private Date now;
    private int turnTorise;
    private int turnToAcceptRise;
    private boolean alreadyRised;
    private  boolean gameFinished;
    private Handler mainPlayerHandler;




    public void GameController(int nPlayer, ArrayList<Player> players, Handler mainPlayerHandler){
        this.players=players;
        this.nPlayer=nPlayer;
        responseGiven=new boolean[nPlayer+1];
        response=new String[nPlayer+1];
        riseAccepted=new boolean[nPlayer+1];
        bidRisen=new boolean[nPlayer+1];
        now=new Date();
        turnTorise=0;
        turnToAcceptRise=1;
        alreadyRised=false;
        gameFinished=false;
        this.mainPlayerHandler=mainPlayerHandler;
    }

    @Override
    public void run() {
        super.run();
        while(true){
            dealCards();
            //amomwebs dro xo ar gavida an yvelam xo ar gasca pasuxi da tu romelime shesrulda momdevno etapze gadadis
            while((now.getTime()-staredWaiting)<=40000){
                boolean cheker=true;
                for(int i=0;i<responseGiven.length;i++){
                    cheker=(cheker&&responseGiven[i]);

                }
                if(cheker) break;
            }

            for(int i=0;i<nPlayer;i++){
                suggestRise();
                if(alreadyRised) break;
            }

            askToAcceptRise();

            evaliateResult();
            if(gameFinished){
                msgUItofinish();
                return;

            }




        }


    }

    private void msgUItofinish() {
    }

    private void evaliateResult() {
    }

    private void askToAcceptRise() {
    }

    private void suggestRise() {
    }

    private void dealCards() {
        staredWaiting=now.getTime();
    }

    //am metods izaxebs da tan romeli playeria eubneba da tan awyobil strings azlevs
    public synchronized void   setAnswer(int player,String answer){
        if((new Date().getTime()-staredWaiting)>40000)
            return;

        responseGiven[player]=true;
        response[player]=answer;

    }

   //es metodi unda gamoizaxos playerma an tvinma roca kitxavs gamecontrolleri xo ar ginda awioo,rigirigobit ekitxeba ra tipebs
    public synchronized void   riseBid(int player){
        if((new Date().getTime()-staredWaiting)>10000)
            return;
        bidRisen[player]=true;
    }



    //es metodi unda gamoizaxos playerma an tvinma roca mesigs daichers rom vigacam awia da shen ra shvrebio
    public synchronized void   acceptBid(int player){
        if((new Date().getTime()-staredWaiting)>10000)
            return;
        riseAccepted[player]=true;
    }




}
