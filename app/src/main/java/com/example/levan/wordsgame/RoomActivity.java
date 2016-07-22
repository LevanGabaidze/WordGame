package com.example.levan.wordsgame;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.levan.wordsgame.backClasses.DataStore;
import com.example.levan.wordsgame.backClasses.GameController;
import com.example.levan.wordsgame.backClasses.GameResult;
import com.example.levan.wordsgame.backClasses.MediumAI;
import com.example.levan.wordsgame.backClasses.MyLexicon;
import com.example.levan.wordsgame.backClasses.OriginalAI;
import com.example.levan.wordsgame.backClasses.Player;

import java.util.ArrayList;

/**
 * Created by Nodar.Kviraia on 7/21/2016.
 */
public class RoomActivity extends AppCompatActivity {


    ArrayList<TextView> cards;
    ArrayList<TextView> myCards;

    MyLexicon lex;
    GameController gm;
    Handler myHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        lex = new MyLexicon();
        lex.InitializeLexicon(this);
        DataStore.createMap(this);
        myHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ReactOnMessage(msg);
            }
        };
        InitialazieGame(2,1);




    }

    private void ReactOnMessage(Message msg) {
       String flag= msg.getData().getString(DataStore.requestTypeFlag);
       switch (flag) {
           case DataStore.askAnswer:
               // am dros karti pirvelad darigda xeli
               String commonCard = msg.getData().getString(DataStore.commonCards);
               String playerCards = msg.getData().getString(DataStore.playerCards);
               System.out.println(commonCard+" chemi: "+playerCards);
               break;
           case DataStore.askRise:
               // aq nisHnavs ro mekitxeba vareizeb tu ara
               break;
           case DataStore.askToAccetptRise:
               //aq gekitxeba miyvebi tu ara.
               break;
           case DataStore.graphicRequest:
               changeGameGraphics(msg);
               break;
           case DataStore.gameResult:
               GameResult result =(GameResult) msg.getData().getSerializable(DataStore.gameResult);
               System.out.println("gameResult"+result.getWinners());
               System.out.println("es sityvebia: "+result.getWords());



       }
    }

    private void changeGameGraphics(Message msg) {
        String flag = msg.getData().getString(DataStore.graphicRequest);
        int player;
        switch (flag) {
            case DataStore.messageToUIAksedToRise:
                player = msg.getData().getInt(DataStore.messageToUIAksedToRise);
                System.out.println(player+" vkitxe raisi");
                break;
            case DataStore.messageToUIAksedToAcceptRise:
                player = msg.getData().getInt(DataStore.messageToUIAksedToRise);
                System.out.println("kitxa" + player+" call tu ara");
                break;
            case DataStore.messageToUIRiseCalled:
                player = msg.getData().getInt(DataStore.messageToUIRiseCalled);
                System.out.println(player+ " aman dacalla ");
                break;
            case DataStore.messageToUIRise:
                player = msg.getData().getInt(DataStore.messageToUIRise);
                System.out.println(player+ " aman daaraisa pirvelma");
                break;
        }

    }

    private void InitialazieGame(int players,int lvl) {
        ArrayList<Player> pArray=new ArrayList<>();
        for(int i=0;i<players;i++){
            OriginalAI ai=null;
            if(lvl==1){
                ai=new OriginalAI(lex);

            }else{
                ai=new MediumAI(lex);
            }
            Player p=new Player(ai,i+1);
            pArray.add(p);
        }
        gm=new GameController(players,pArray,myHandler);
        for(int i=0;i<pArray.size();i++){
            pArray.get(i).setGamecontroller(gm);
            pArray.get(i).start();
        }
        gm.start();


    }


}
