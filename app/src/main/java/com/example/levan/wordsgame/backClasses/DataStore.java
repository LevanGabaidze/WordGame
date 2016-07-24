package com.example.levan.wordsgame.backClasses;

import android.content.Context;
import android.content.Intent;

import com.example.levan.wordsgame.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by levan on 7/21/2016.
 */
public  class  DataStore {

    public final static String messageToUIAnswerGiven="answerGiven";
    public final static String messageToUIRise="playerRised";
    public final static String messageToUIRiseCalled="playerCalled";

    public final static String messageToUICardsDealt="dealtAToPlayers";
    public final static String messageToUIAksedToRise="arskedPlayerToRise";
    public final static String messageToUIAksedToAcceptRise="askedPlayerToAcceptRise";



    public final static String graphicRequest="graphicRequest";
    public final static String gameResult="gameResult";
    public final static String potUpdate="sendingPotValue";


    public final static  String noMoreCards="noMoreCards";
    public final static  String commonCards="commonCards";
    public final static  String playerCards="playerCards";

    public final static  String requestTypeFlag="requestType";


    public final static  String askAnswer="askForAnswer";
    public final static  String askRise="askForRise";
    public final static  String askToAccetptRise="askToAcceptRise";
    public final static  String sentStringKey="sentStringKey";
    public final static  int Card_Number=5;
    public final static int startMoney=100;
    public final static int[] bidSequence= new int[]{10,20,30,40,50};
    private static HashMap<Character,Integer>  charMap;
    private static ArrayList<Card> dasta;


    public static void createMap(Context con){
        charMap=new HashMap<>();
        dasta=new ArrayList<>();
        String[] strs=con.getResources().getStringArray(R.array.characters_array);
        int[] frequency=con.getResources().getIntArray(R.array.frequency);
        int[] values=con.getResources().getIntArray(R.array.values);
        for(int i=0;i<strs.length;i++){
            charMap.put(strs[i].charAt(0),values[i]);
            for(int j=0;j<frequency[i];j++){
                Card c=new Card(strs[i].charAt(0),values[i]);
                dasta.add(c);
            }

        }

    }

    public static ArrayList<Card> getDasta(){
        return dasta;
    }

    public  static int getValue(Character ch){


        return charMap.get(ch);
    }

    public static int evaluate(String str){
        int value=0;
        for(int i=0;i<str.length();i++){
            value+=DataStore.getValue(str.charAt(i));
        }
        return  value;

    }

}
