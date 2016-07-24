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
    private ArrayList<Card> cardsOriginal;

    private int nPlayer;
    private boolean[] responseGiven;
    private String[] response;
    private boolean[] bidRisen;
    private boolean[] riseAccepted;
    private long staredWaiting;
    private boolean[] acceptCall;
    private boolean[] acceptRise;
    private Date now;
    private int turnTorise;
    private int turnToAcceptRise;
    private boolean alreadyRised;
    private  boolean gameFinished;
    private Handler mainPlayerHandler;
    private Set<Integer> playersOut;
    private int curHand;
    private int pot;
    private int[] money;



    public GameController(int nPlayer, ArrayList<Player> players, Handler mainPlayerHandler){
        this.players=players;
        this.nPlayer=nPlayer;
        responseGiven=new boolean[nPlayer+1];
        response=new String[nPlayer+1];
        riseAccepted=new boolean[nPlayer+1];
        bidRisen=new boolean[nPlayer+1];
        acceptCall=new boolean[nPlayer+1];
        acceptRise=new boolean[nPlayer+1];
        money=new int[nPlayer+1];
        now=new Date();
        turnTorise=0;
        turnToAcceptRise=1;
        alreadyRised=false;
        gameFinished=false;
        this.mainPlayerHandler=mainPlayerHandler;
        cards=DataStore.getDasta();
        Collections.shuffle(cards);
        dealtCards=new ArrayList<>();
        playersOut=new HashSet<>();
        curHand=0;
        pot=0;

    }

    @Override
    public void run() {
        super.run();
        for(int i=0;i<nPlayer+1;i++){
            money[i]=DataStore.startMoney;
        }
        while(true){
            boolean cheker=true;
            for(int i=0;i<players.size();i++){
                cheker=cheker&&(players.get(i).getmHanlder()!=null);
            }
            if(cheker) break;
        }

        while(true){
            dealtCards.clear();
            Collections.shuffle(cards);
            pot=0;
            dealCards();
            suggestRise();
            askToAcceptRise();
            if(curHand<4) curHand+=1;
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
        ArrayList<Integer> scores=getScores();
        int maxScore=scores.get(scores.size()-1);
        ArrayList<Integer> winners=getWinners(maxScore,scores);
        GameResult gmr=new GameResult();
        gmr.setScores(scores);
        gmr.setWinners(winners);
        // es davamate (rame tu auria)))
        setMoney(winners);
        ArrayList<String> words=new ArrayList<>();
        for(int i=0;i<response.length;i++){
            words.add(response[i]);
            response[i]=null;
            responseGiven[i]=false;
            riseAccepted[i]=false;
            bidRisen[i]=false;
        }
        ArrayList<Integer> moneys=new ArrayList<>();

        for(int i=0;i<money.length;i++){
            moneys.add(money[i]);
        }
        gmr.setMoney(moneys);
        gmr.setWords(words);
        ArrayList<Integer> plNowOut=checkIfanyOneOut();
        gmr.setPlayersNowOut(plNowOut);
        Message mg=new Message();
        mg.getData().putString(DataStore.requestTypeFlag,DataStore.gameResult);
        mg.getData().putSerializable(DataStore.gameResult,gmr);
        mainPlayerHandler.sendMessage(mg);
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private ArrayList<Integer> checkIfanyOneOut() {
        ArrayList<Integer> playersNowOut=new ArrayList<>();
        for(int i=0;i<money.length;i++){
            if(!players.contains(i) && money[i]<DataStore.bidSequence[curHand]+10 ){
                playersOut.add(i);
                playersNowOut.add(i);
            }
        }
        if((playersOut.size()==nPlayer+1)|| playersOut.contains(0)){
            gameFinished=true;
        }

        return  playersNowOut;
    }

    private ArrayList<Integer> getWinners(int maxScore, ArrayList<Integer> scores) {
        ArrayList<Integer> winners=new ArrayList<>();
        for(int i=0;i<scores.size()-1;i++){
            if(scores.get(i)==null) continue;
            if(scores.get(i)==maxScore){
                winners.add(i);
            }
        }
        return  winners;
    }

    private void askToAcceptRise() {
        long lastSend;
        for(int i=0;i<nPlayer+1;i++){
            if(playersOut.contains(i) || bidRisen[i]==true ) continue;
            Message ms=new Message();
            ms.getData().putString(DataStore.requestTypeFlag,DataStore.askToAccetptRise);
            if(i==0){
                mainPlayerHandler.sendMessage(ms);
                lastSend=new Date().getTime();
            }else {
                Message mg=new Message();
                mg.getData().putString(DataStore.requestTypeFlag,DataStore.graphicRequest);
                mg.getData().putString(DataStore.graphicRequest,DataStore.messageToUIAksedToAcceptRise);
                mg.getData().putInt(DataStore.messageToUIAksedToAcceptRise,i);
                mainPlayerHandler.sendMessage(mg);
                players.get(i-1).getmHanlder().sendMessage(ms);

                // es me davamate (tu auria )))
              //  mainPlayerHandler.sendMessage(ms);
                lastSend=new Date().getTime();
            }
            acceptCall[i]=true;
            while((new Date().getTime()-lastSend)<15000){
                if(riseAccepted[i]) break;
            }
            acceptCall[i]=false;

        }

    }

    private void suggestRise() {
        long lastSend;
        alreadyRised=false;
        for(int i=0;i<nPlayer+1;i++){
            if(playersOut.contains(i)) continue;
            Message ms=new Message();
            ms.getData().putString(DataStore.requestTypeFlag,DataStore.askRise);
                if(i==0) {
                    mainPlayerHandler.sendMessage(ms);
                    lastSend=new Date().getTime();
                }else{
                    Message mg=new Message();
                    mg.getData().putString(DataStore.requestTypeFlag,DataStore.graphicRequest);
                    mg.getData().putString(DataStore.graphicRequest,DataStore.messageToUIAksedToRise);
                    mg.getData().putInt(DataStore.messageToUIAksedToRise,i);
                    mainPlayerHandler.sendMessage(mg);
                    players.get(i-1).getmHanlder().sendMessage(ms);
                    lastSend=new Date().getTime();
                }
                acceptRise[i]=true;
                while((new Date().getTime()-lastSend)<15000){
                    if(alreadyRised) return;
                }
                acceptRise[i]=false;



        }

    }

    private void dealCards() {
        String curCards="";
        if(cards.size()<(DataStore.Card_Number+2*(nPlayer-playersOut.size()+1))) {
            gameFinished=true;
            notifyNoMoreCards();
            interrupt();
        }
        int darigebuli=0;

        for(int i=0;i<DataStore.Card_Number;i++){
            dealtCards.add(cards.get(darigebuli));
            curCards+=cards.get(darigebuli).getCharacter();
            darigebuli++;
        }


       //deal to person
        String mPlayerHand="";

        for(int j=0; j<2;j++){
            dealtCards.add(cards.get(darigebuli));
            mPlayerHand+=cards.get(darigebuli).getCharacter();
            darigebuli++;
        }
        pot+=DataStore.bidSequence[curHand];
        money[0]-=DataStore.bidSequence[curHand];
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
            for(int j=darigebuli; j<2;j++){
                dealtCards.add(cards.get(darigebuli));
                cardsTosend+=cards.get(darigebuli).getCharacter();
               darigebuli++;
            }
            pot+=DataStore.bidSequence[curHand];
            money[i+1]-=DataStore.bidSequence[curHand];
            Message ms=new Message();
            ms.getData().putString(DataStore.requestTypeFlag,DataStore.askAnswer);
            ms.getData().putString(DataStore.sentStringKey,cardsTosend);
            players.get(i).getmHanlder().sendMessage(ms);
        }
        Message potMs=new Message();
        potMs.getData().putString(DataStore.requestTypeFlag,DataStore.potUpdate);
        potMs.getData().putInt(DataStore.potUpdate,pot);
        mainPlayerHandler.sendMessage(potMs);
        waitForAnswers();


    }

    private void notifyNoMoreCards() {
        Message msgTomainActivity= new Message();
        msgTomainActivity.getData().putString(DataStore.requestTypeFlag,DataStore.noMoreCards);

        mainPlayerHandler.sendMessage(msgTomainActivity);

    }

    private void waitForAnswers() {
        staredWaiting=new Date().getTime();
        System.out.println("dro danisna");
        //amomwebs dro xo ar gavida an yvelam xo ar gasca pasuxi da tu romelime shesrulda momdevno etapze gadadis
        while((new Date().getTime()-staredWaiting)<=40000){
            boolean cheker=true;
            for(int i=0;i<responseGiven.length;i++){
                cheker=(cheker&&responseGiven[i]);

            }
            if(cheker) break;
        }
        System.out.println("dro gavida");
    }

    //am metods izaxebs da tan romeli playeria eubneba da tan awyobil strings azlevs
    public synchronized void   setAnswer(int player,String answer){
        if((new Date().getTime()-staredWaiting)>40000)
            return;

        responseGiven[player]=true;
        response[player]=answer;
        Message m=new Message();

    }

   //es metodi unda gamoizaxos playerma an tvinma roca kitxavs gamecontrolleri xo ar ginda awioo,rigirigobit ekitxeba ra tipebs
    public synchronized void   riseBid(int player){
        if(!acceptRise[player])
            return;
        bidRisen[player]=true;
        riseAccepted[player]=true;
        alreadyRised=true;
        Message mg=new Message();
        mg.getData().putString(DataStore.requestTypeFlag,DataStore.graphicRequest);
        mg.getData().putString(DataStore.graphicRequest,DataStore.messageToUIRise);
        mg.getData().putInt(DataStore.messageToUIRise,player);
        pot+=10;
        money[player]-=10;
        mainPlayerHandler.sendMessage(mg);
        Message ms=new Message();
        ms.getData().putString(DataStore.requestTypeFlag,DataStore.potUpdate);
        ms.getData().putInt(DataStore.potUpdate,pot);
        mainPlayerHandler.sendMessage(ms);
    }



    //es metodi unda gamoizaxos playerma an tvinma roca mesigs daichers rom vigacam awia da shen ra shvrebio
    public synchronized void   acceptBid(int player){
        if(!acceptCall[player])
            return;
        riseAccepted[player]=true;
        Message mg=new Message();
        mg.getData().putString(DataStore.requestTypeFlag,DataStore.graphicRequest);
        mg.getData().putString(DataStore.graphicRequest,DataStore.messageToUIRiseCalled);
        mg.getData().putInt(DataStore.messageToUIRiseCalled,player);
        pot+=10;
        mainPlayerHandler.sendMessage(mg);
        money[player]=money[player]-10;
        Message ms=new Message();
        ms.getData().putString(DataStore.requestTypeFlag,DataStore.potUpdate);
        ms.getData().putInt(DataStore.potUpdate,pot);
        mainPlayerHandler.sendMessage(ms);
    }


    public ArrayList<Integer> getScores() {
        ArrayList<Integer> winners=new ArrayList<>();
        int max=0;
        int value=0;
        for(int i=0;i<response.length;i++){
            if(!playersOut.contains(i)&& responseGiven[i]){
                if((alreadyRised && riseAccepted[i])||!alreadyRised){
                    value=DataStore.evaluate(response[i]);
                    if(value>max) max=value;
                    winners.add(value);

                }else{
                    winners.add(null);

                }
            }else {
                winners.add(null);
            }
        }
        //last integer is max
        winners.add(max);
        return winners;
    }


    public void setMoney(ArrayList<Integer> winners) {
        for(int i=0;i<winners.size();i++){
            money[i]+=pot/winners.size();
        }
    }

    public synchronized int getCurrentRound() {
        return curHand;
    }
}
