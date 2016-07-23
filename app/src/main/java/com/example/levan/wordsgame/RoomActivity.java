package com.example.levan.wordsgame;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.levan.wordsgame.backClasses.DataStore;
import com.example.levan.wordsgame.backClasses.GameController;
import com.example.levan.wordsgame.backClasses.GameResult;
import com.example.levan.wordsgame.backClasses.MediumAI;
import com.example.levan.wordsgame.backClasses.MyLexicon;
import com.example.levan.wordsgame.backClasses.OriginalAI;
import com.example.levan.wordsgame.backClasses.Player;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Nodar.Kviraia on 7/21/2016.
 */
public class RoomActivity extends AppCompatActivity {


    ArrayList<View> cards;
    ArrayList<View> myCards;
    ArrayList<View> players;
    ArrayList<View> chosenCards = new ArrayList<>();
    String myWord ="";
    boolean callType = false;
    Button clear, delete, ok, yes, no;
    TextView composedWord;
    Typeface type;
    int numPlayers;
    View playerBoard;
    ArrayList<Integer> moneys =  new ArrayList<>();
    int diff;

    MyLexicon lex;
    GameController gm;
    Handler myHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        type = Typeface.createFromAsset(getAssets(),"fonts/acadnusx.ttf");
        numPlayers = getIntent().getIntExtra("np",1);
        diff = getIntent().getIntExtra("diff",0);
        lex = new MyLexicon();
        initCards();
        initPlayers();
        initButtons();
        lex.InitializeLexicon(this);
        DataStore.createMap(this);
        myHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ReactOnMessage(msg);
            }
        };
        InitialazieGame(2,1);
        composedWord = (TextView) findViewById(R.id.composed_text);

        composedWord.setTypeface(type);





    }

    private void initButtons() {
        playerBoard = findViewById(R.id.player_board);
        ok = (Button) findViewById(R.id.btn_enter);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        clear = (Button) findViewById(R.id.btn_clear);
        delete = (Button) findViewById(R.id.btn_backspace);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callType) {
                    gm.acceptBid(0);
                } else {
                    gm.riseBid(0);
                }
                playerBoard.setVisibility(View.INVISIBLE);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // aq metodia gmshi shesacvleli bool ro gadaeces
                if (callType) {
                    //
                   // gm.acceptBid(0);
                } else {
                   // gm.riseBid(0);
                }
                playerBoard.setVisibility(View.INVISIBLE);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gm.setAnswer(0,myWord);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWord = "";
                composedWord.setText("");
                chosenCards.clear();
                for (int i=0; i<cards.size(); i++) {
                    //cards.get(i).setEnabled(true);
                    enable(cards.get(i),true);
                }
                for (int i=0; i<myCards.size(); i++) {
                    //myCards.get(i).setEnabled(true);
                    enable(myCards.get(i),true);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chosenCards.get(chosenCards.size()-1).setEnabled(true);
                if (chosenCards.size()<1) return;
                enable(chosenCards.get(chosenCards.size()-1),true);
                chosenCards.remove(chosenCards.size()-1);
                if (myWord.length()<2) myWord = ""; else myWord = myWord.substring(0,myWord.length()-1);
                composedWord.setText(myWord);
            }
        });

        for (int i=0; i<cards.size(); i++) {
            //final int ii = i;
            setListener(cards.get(i));
        }

        for (int i=0; i<myCards.size(); i++) {
            setListener(myCards.get(i));
        }



    }

    private void enable(View v, boolean b) {

        if (b) v.setVisibility(View.VISIBLE);
            else v.setVisibility(View.INVISIBLE);
        v.setEnabled(b);
    }

    private void initPlayers() {
        players = new ArrayList<>();
        players.add(findViewById(R.id.op1));
        players.add(findViewById(R.id.op2));
        players.add(findViewById(R.id.op3));
        for (int i=0; i<numPlayers; i++) {
            moneys.add(100);
        }
        for(int i=0; i<players.size(); i++) {
            if (i>=numPlayers) {
                players.get(i).setEnabled(false);
                players.get(i).setVisibility(View.GONE);
            }
        }
    }

    private void initCards() {
        cards = new ArrayList<>();
        myCards = new ArrayList<>();
        cards.add(findViewById(R.id.crd1));
        cards.add(findViewById(R.id.crd2));
        cards.add(findViewById(R.id.crd3));
        cards.add(findViewById(R.id.crd4));
        cards.add(findViewById(R.id.crd5));
        cards.add(findViewById(R.id.crd6));
        cards.add(findViewById(R.id.crd7));
        myCards.add(findViewById(R.id.crd8));
        myCards.add(findViewById(R.id.crd9));
        for (int i=0; i<cards.size(); i++) {
            ((TextView)cards.get(i).findViewById(R.id.card_letter)).setTypeface(type);
        }
        for (int i=0; i<myCards.size(); i++) {
            ((TextView)myCards.get(i).findViewById(R.id.card_letter)).setTypeface(type);
        }
    }



    private void ReactOnMessage(Message msg) {
       String flag= msg.getData().getString(DataStore.requestTypeFlag);
       switch (flag) {
           case DataStore.askAnswer:
               // am dros karti pirvelad darigda xeli
               String commonCard = msg.getData().getString(DataStore.commonCards);
               String playerCards = msg.getData().getString(DataStore.playerCards);
               // aq chemi fulic unda vakotrolo
               for (int i=1; i<moneys.size(); i++) {
                   ((TextView) players.get(i).findViewById(R.id.money)).setText(moneys.get(i)+"$");
               }
               composedWord.setText("dro CarTulia");
               for (int i=0; i<commonCard.length(); i++) {
                   cards.get(i).setEnabled(true);
                   ((TextView)cards.get(i).findViewById(R.id.card_letter)).setText(commonCard.charAt(i)+"");
                    cards.get(i).setVisibility(View.VISIBLE);
               }
               for (int i=0; i<playerCards.length(); i++) {
                   myCards.get(i).setEnabled(true);
                   ((TextView)myCards.get(i).findViewById(R.id.card_letter)).setText(playerCards.charAt(i)+"");
                   myCards.get(i).setVisibility(View.VISIBLE);
               }
               System.out.println(commonCard+" chemi: "+playerCards);
               break;
           case DataStore.askRise:
               composedWord.setText("gsurT gazardoT fsoni?");
               playerBoard.setVisibility(View.VISIBLE);
               callType = false;
               break;
           case DataStore.askToAccetptRise:
               //aq gekitxeba miyvebi tu ara.
               callType = true;
               playerBoard.setVisibility(View.VISIBLE);
               composedWord.setText("asworebT Tu ara fsons?");
               break;
           case DataStore.graphicRequest:
               changeGameGraphics(msg);
               break;
           case DataStore.gameResult:
               GameResult result =(GameResult) msg.getData().getSerializable(DataStore.gameResult);
               finishHand(result);
               System.out.println("gameResult"+result.getWinners());
               System.out.println("es sityvebia: "+result.getWords());



       }
    }

    private void finishHand(GameResult result) {
        int winner = result.getWinners().get(0);
        myWord = "";
        if (winner == 0) composedWord.setText("You win!"); else {
            ((TextView)(players.get(winner-1).findViewById(R.id.oponent_message))).setText("Winner");
            // aq Cemi fuli unda Sevcvalo
            for (int i=0; i<result.getScores().size(); i++) {
                if (i==0) continue;
                ((TextView)players.get(i-1).findViewById(R.id.money)).setText(result.getScores().get(i)+" pnt");
            }
            for (int i=0; i<result.getWords().size(); i++) {
                if (i==0) continue;
                ((TextView)players.get(i-1).findViewById(R.id.oponent_message)).setText(result.getWords().get(i)+"");
            }
            moneys = result.getMoney();
            clear.callOnClick();

        }


    }


    private void changeGameGraphics(Message msg) {
        String flag = msg.getData().getString(DataStore.graphicRequest);
        int player;
        switch (flag) {
            case DataStore.messageToUIAksedToRise:
                player = msg.getData().getInt(DataStore.messageToUIAksedToRise);
                System.out.println(player+" vkitxe raisi");
                if (player == 0) return;
                ((TextView)players.get(player-1).findViewById(R.id.oponent_message)).setText("waiting");
                break;
            case DataStore.messageToUIAksedToAcceptRise:
                player = msg.getData().getInt(DataStore.messageToUIAksedToRise);
                System.out.println("kitxa" + player+" call tu ara");
                if (player == 0) return;
                ((TextView)players.get(player-1).findViewById(R.id.oponent_message)).setText("waiting");
                break;
            case DataStore.messageToUIRiseCalled:
                player = msg.getData().getInt(DataStore.messageToUIRiseCalled);
                System.out.println(player+ " aman dacalla ");
                if (player ==0) return;
                ((TextView)players.get(player-1).findViewById(R.id.oponent_message)).setText("called");
                break;
            case DataStore.messageToUIRise:
                player = msg.getData().getInt(DataStore.messageToUIRise);
                System.out.println(player+ " aman daaraisa pirvelma");
                if (player == 0) return;
                ((TextView)players.get(player-1).findViewById(R.id.oponent_message)).setText("raised");
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


    public void setListener(View listener) {
        listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String let =(String)((TextView) view.findViewById(R.id.card_letter)).getText();
                myWord = myWord+let;
                //view.setEnabled(false);
                enable(view,false);
                composedWord.setText(myWord);
                chosenCards.add(view);
            }
        });
    }
}
