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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.example.levan.wordsgame.backClasses.DataStore;
import com.example.levan.wordsgame.backClasses.GameController;
import com.example.levan.wordsgame.backClasses.GameResult;
import com.example.levan.wordsgame.backClasses.MediumAI;
import com.example.levan.wordsgame.backClasses.MyLexicon;
import com.example.levan.wordsgame.backClasses.OriginalAI;
import com.example.levan.wordsgame.backClasses.Player;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nodar.Kviraia on 7/21/2016.
 */
public class RoomActivity extends AppCompatActivity {


    ArrayList<View> cards;
    ArrayList<View> myCards;
    boolean isLost, isLasstWin;
    ArrayList<View> players;
    ArrayList<View> chosenCards = new ArrayList<>();
    String myWord ="";
    boolean callType = false;
    TimerThread timer;
    Button clear, delete, ok, yes, no;
    int countOut = 0;
    CircularProgressView progress;
    TextView composedWord;
    ViewAnimator anim;
    Typeface type;
    int numPlayers;
    TextView myCurMoney, myCurScore, curTime;
    View playerBoard, pot;
    TextView pot_value;
    ArrayList<Integer> moneys =  new ArrayList<>();
    int diff;
    AppCompatActivity c = this;

    MyLexicon lex;
    GameController gm;
    Handler myHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        anim =(ViewAnimator) findViewById(R.id.animator_room);
        progress = (CircularProgressView)findViewById(R.id.progress_view_room);
        progress.startAnimation();
        //timer = new TimerThread(curTime,40);
        type = Typeface.createFromAsset(getAssets(),"fonts/acadnusx.ttf");
        numPlayers = getIntent().getIntExtra("np",1);
        diff = getIntent().getIntExtra("diff",1);
        lex = new MyLexicon();
        myCurMoney = (TextView) findViewById(R.id.my_cur_money);
        myCurScore =(TextView) findViewById(R.id.cur_possible_score);
        curTime =(TextView) findViewById(R.id.timer);
        initCards();
        initPlayers();
        initButtons();
        pot = findViewById(R.id.pot_overall);
        pot_value =(TextView) pot.findViewById(R.id.pot_amount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lex.InitializeLexicon(c);
                DataStore.createMap(c);
                InitialazieGame(numPlayers,diff);
                anim.post(new Runnable() {
                    @Override
                    public void run() {
                        progress.stopAnimation();
                        anim.showNext();
                    }
                });
            }
        }).start();
       // lex.InitializeLexicon(this);

        myHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                ReactOnMessage(msg);
            }
        };

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
                    gm.acceptBid(0,true);
                    moneys.set(0,moneys.get(0)-10);
                    updateMoneys();
                } else {
                    gm.riseBid(0,true);
                    moneys.set(0,moneys.get(0)-10);
                    updateMoneys();
                }
                playerBoard.setVisibility(View.INVISIBLE);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // aq metodia gmshi shesacvleli bool ro gadaeces
                if (callType) {
                    gm.acceptBid(0,false);
                //    moneys.set(0,moneys.get(0)-10);
                    updateMoneys();
                } else {
                    gm.riseBid(0,false);
                  //  moneys.set(0,moneys.get(0)-10);
                    updateMoneys();
                }
                playerBoard.setVisibility(View.INVISIBLE);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lex.isWord(myWord)){
                    gm.setAnswer(0,myWord);

                }else{
                    gm.setAnswer(0,"");
                }
                ok.setEnabled(false);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWord = "";
                myCurScore.setText("0 pnt");
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
                myCurScore.setText(getScore(myWord)+"pnt");
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

    private void updateMoneys() {

        for (int i=0; i<Math.min(numPlayers,moneys.size()); i++) {
            if (i == 0) {
                myCurMoney.setText(moneys.get(0) + "$");
            } else {
                ((TextView) players.get(i - 1).findViewById(R.id.money)).setText(moneys.get(i) + "$");
            }
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
          //  if (i>4) cards.get(i).setVisibility(View.GONE);
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
               ok.setEnabled(true);
               myWord = "";
               String commonCard = msg.getData().getString(DataStore.commonCards);
               String playerCards = msg.getData().getString(DataStore.playerCards);
               //int mM = moneys.get(0);
               updateMoneys();
               composedWord.setText("dro CarTulia");
               timer = new TimerThread(curTime,40);
               timer.start();
               for (int i=0; i<commonCard.length(); i++) {
                   cards.get(i).setEnabled(true);
                   ((TextView)cards.get(i).findViewById(R.id.card_letter)).setText(commonCard.charAt(i)+"");
                   ((TextView)cards.get(i).findViewById(R.id.card_score)).setText(DataStore.getValue(commonCard.charAt(i))+"");
                    cards.get(i).setVisibility(View.VISIBLE);
               }
               for (int i=0; i<playerCards.length(); i++) {
                   myCards.get(i).setEnabled(true);
                   ((TextView)myCards.get(i).findViewById(R.id.card_letter)).setText(playerCards.charAt(i)+"");
                   ((TextView)myCards.get(i).findViewById(R.id.card_score)).setText(DataStore.getValue(playerCards.charAt(i))+"");
                   myCards.get(i).setVisibility(View.VISIBLE);
               }
               System.out.println(commonCard+" chemi: "+playerCards);
               break;
           case "fin":
               System.out.println("interrrupppppppppppppppp");
               Intent in = new Intent(this, MainActivity.class);
               startActivity(in);
               this.finish();
                break;
           case DataStore.askRise:
               composedWord.setText("gsurT gazardoT fsoni?");
               timer.interrupt();
               timer = new TimerThread(curTime,15);
               timer.start();
               playerBoard.setVisibility(View.VISIBLE);
               callType = false;
               break;
           case DataStore.askToAccetptRise:
               //aq gekitxeba miyvebi tu ara.
               callType = true;
               playerBoard.setVisibility(View.VISIBLE);
               composedWord.setText("asworebT Tu ara fsons?");
               timer.interrupt();
               timer = new TimerThread(curTime,15);
               timer.start();
               break;
           case DataStore.graphicRequest:
               changeGameGraphics(msg);
               break;
           case DataStore.gameResult:
               GameResult result =(GameResult) msg.getData().getSerializable(DataStore.gameResult);
               finishHand(result);
               System.out.println("gameResult"+result.getWinners());
               System.out.println("es sityvebia: "+result.getWords());
               break;
           case DataStore.potUpdate:
               int potN = msg.getData().getInt(DataStore.potUpdate);
               changePot(potN);
               //
               break;

       }
    }

    private void changePot(int potN) {
        pot_value.setText(potN+"");
    }

    private void finishHand(GameResult result) {
        int winner = result.getWinners().get(0);
        timer.interrupt();
        ArrayList<Integer> out = result.getPlayersNowOut();
        for (int i=0; i<out.size(); i++) {
            countOut++;
            if (out.get(i)==0){
                lost();
                return;
            }
            players.get(out.get(i)-1).setVisibility(View.INVISIBLE);
            if (countOut == numPlayers) {
                winAllGame();
                return;
            }
        }
        myWord = "";
        if (winner == 0) composedWord.setText("You win!"); else {
            ((TextView) (players.get(winner - 1).findViewById(R.id.oponent_message))).setText("Winner");
        }
            // aq Cemi fuli unda Sevcvalo
            for (int i=0; i<result.getScores().size()-1; i++) {
                if (i==0) continue;
                if (result.getScores().get(i) != null) {
                    ((TextView)players.get(i-1).findViewById(R.id.money)).setText(result.getScores().get(i)+" pnt");
                }
            }
            for (int i=0; i<result.getWords().size(); i++) {
                if (i==0) continue;
                String st;
                st = result.getWords().get(i);
                if (st == null) st = "no word";
                ((TextView)players.get(i-1).findViewById(R.id.oponent_message)).setText(st+"");
            }
            moneys = result.getMoney();
            updateMoneys();
            clear.callOnClick();




    }

    private void winAllGame() {
        composedWord.setText("Tqven moigeT!");

        isLasstWin = true;
    }

    private void lost() {
        composedWord.setText("Tqven waageT");
        isLost = true;
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
                timer.interrupt();
                break;
            case DataStore.messageToUIAksedToAcceptRise:
                player = msg.getData().getInt(DataStore.messageToUIAksedToAcceptRise);
                System.out.println("kitxa" + player+" call tu ara");
                if (player == 0) return;
                ((TextView)players.get(player-1).findViewById(R.id.oponent_message)).setText("thinking");
                timer.interrupt();
                break;
            case DataStore.messageToUIRiseCalled:
                player = msg.getData().getInt(DataStore.messageToUIRiseCalled);
                System.out.println(player+ " aman dacalla ");

                if (player ==0) return;
                boolean bb =  msg.getData().getBoolean(DataStore.answerIs);
                String st = "fold";
                if (bb) st = "called";
                ((TextView)players.get(player-1).findViewById(R.id.oponent_message)).setText(st);
                timer.interrupt();
                break;
            case DataStore.messageToUIRise:
                player = msg.getData().getInt(DataStore.messageToUIRise);
                boolean b = msg.getData().getBoolean(DataStore.answerIs);
                System.out.println(player+ " aman daaraisa pirvelma");
                if (player == 0) return;
                if (b)((TextView)players.get(player-1).findViewById(R.id.oponent_message)).setText("raised");
                timer.interrupt();
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
                myCurScore.setText(getScore(myWord)+" pnt");
                chosenCards.add(view);
            }
        });
    }

    private class TimerThread extends Thread {
        TextView timer;
        int count = 0;
        int secs;

        public TimerThread(TextView v, int sec) {
            timer = v;
            secs = sec;
            count = sec;
        }
        @Override
        public void run() {
            super.run();
            while (count>0) {
                if(isInterrupted()) return;
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    return;
                    //e.printStackTrace();
                }
                count--;
                timer.post(new Runnable() {
                        @Override
                        public void run() {
                            timer.setText(count+ "s");
                        }
                    });
            }
            timer.post(new Runnable() {
                @Override
                public void run() {
                    timer.setText("time up");
                }

            });
        }
    }

    private int getScore(String st) {
        if (st == null) return 0;
        int res= 0;
        for (int i=0; i<st.length(); i++) {
            res = res+DataStore.getValue(st.charAt(i));
        }
        return res;
    }
}
