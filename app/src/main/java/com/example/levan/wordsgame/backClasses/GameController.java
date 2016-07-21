package com.example.levan.wordsgame.backClasses;

import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by levan on 7/19/2016.
 */
public class GameController  extends Thread {


    private ArrayList<Player> players;
    private ArrayList<Card> cards;
    private ArrayList<Card> dealtCards;

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
    private Set<Integer> playersOut;






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
        Collections.shuffle(cards);
        dealtCards=new ArrayList<>();
        playersOut=new HashSet<>();
    }

    @Override
    public void run() {
        super.run();
        while(true){
            dealtCards.clear();
            dealCards();
            suggestRise();
            askToAcceptRise();
            evaluateResult();
            if(gameFinished){
                msgUItofinish();
                return;

            }

        }


    }

    private void msgUItofinish() {
    }

    private void evaluateResult() {
    }

    private void askToAcceptRise() {
    }

    private void suggestRise() {
        long lastSend;
        alreadyRised=false;
        for(int i=0;i<nPlayer+1;i++){
            if(playersOut.contains(i)) continue;
            Message ms=new Message();
            ms.getData().putString(DataStore.requestTypeFlag,DataStore.askRise);
            if(turnTorise==i){
                if(i==0) {
                    mainPlayerHandler.sendMessage(ms);
                    lastSend=now.getTime();
                }else{
                    players.get(i-1).getmHanlder().sendMessage(ms);
                    lastSend=now.getTime();
                }
                while((now.getTime()-lastSend)<15000){
                    if(alreadyRised) return;
                }

            }


        }

    }

    private void dealCards() {
        String curCards="";
        if(cards.size()<(DataStore.Card_Number+2*(nPlayer-playersOut.size()+1))) {
            gameFinished=true;
            notifyNoMoreCards();
            interrupt();
        }
        for(int i=0;i<DataStore.Card_Number;i++){
            dealtCards.add(cards.get(0));
            curCards+=cards.get(0);
            cards.remove(0);
        }

       //deal to person
        String mPlayerHand="";
        for(int j=0; j<2;j++){
            dealtCards.add(cards.get(0));
            mPlayerHand+=cards.get(0);
            cards.remove(0);
        }
        Message msgTomainActivity= new Message();
        msgTomainActivity.getData().putString(DataStore.requestTypeFlag,DataStore.askAnswer);
        msgTomainActivity.getData().putString(DataStore.commonCards,curCards);
        msgTomainActivity.getData().putString(DataStore.playerCards,mPlayerHand);
        mainPlayerHandler.sendMessage(msgTomainActivity);

        //deal to computer
        for(int i=0;i<nPlayer;i++){
            String cardsTosend="";
            cardsTosend+=curCards;
            if(playersOut.contains(i+1)) continue;
            for(int j=0; j<2;j++){
                dealtCards.add(cards.get(0));
                cardsTosend+=cards.get(0);
                cards.remove(0);
            }
            Message ms=new Message();
            ms.getData().putString(DataStore.requestTypeFlag,DataStore.askAnswer);
            ms.getData().putString(DataStore.sentStringKey,cardsTosend);
            players.get(i).getmHanlder().sendMessage(ms);
        }
        waitForAnswers();

    }

    private void notifyNoMoreCards() {
        Message msgTomainActivity= new Message();
        msgTomainActivity.getData().putString(DataStore.requestTypeFlag,DataStore.noMoreCards);

        mainPlayerHandler.sendMessage(msgTomainActivity);
    }

    private void waitForAnswers() {
        staredWaiting=now.getTime();
        //amomwebs dro xo ar gavida an yvelam xo ar gasca pasuxi da tu romelime shesrulda momdevno etapze gadadis
        while((now.getTime()-staredWaiting)<=40000){
            boolean cheker=true;
            for(int i=0;i<responseGiven.length;i++){
                cheker=(cheker&&responseGiven[i]);

            }
            if(cheker) break;
        }
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
        alreadyRised=true;
    }



    //es metodi unda gamoizaxos playerma an tvinma roca mesigs daichers rom vigacam awia da shen ra shvrebio
    public synchronized void   acceptBid(int player){
        if((new Date().getTime()-staredWaiting)>10000)
            return;
        riseAccepted[player]=true;
    }




}
