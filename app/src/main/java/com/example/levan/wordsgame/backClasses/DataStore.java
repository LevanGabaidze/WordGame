package com.example.levan.wordsgame.backClasses;

/**
 * Created by levan on 7/21/2016.
 */
public class DataStore {
    public final static  String requestTypeFlag="requestType";

    public final static  String noMoreCards="noMoreCards";
    public final static  String commonCards="commonCards";
    public final static  String playerCards="playerCards";



    public final static  String askAnswer="askForAnswer";
    public final static  String askRise="askForRise";
    public final static  String askToAccetptRise="askToAcceptRise";
    public final static  String sentStringKey="sentStringKey";

    public final static  int Card_Number=5;

    public final static int startMoney=100;
    public final static int[] bidSequence= new int[]{10,20,30,40,50};




    public  static int getValue(Character ch){

        return 0;
    }

    public static int evaluate(String str){
        int value=0;
        for(int i=0;i<str.length();i++){
            value+=DataStore.getValue(str.charAt(i));
        }
        return  value;


    }

}
