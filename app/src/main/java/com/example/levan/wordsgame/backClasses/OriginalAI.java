package com.example.levan.wordsgame.backClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.Stack;

/**
 * Created by levan on 7/21/2016.
 */
public class OriginalAI {

    private MyLexicon lx;
    private ArrayList<Word> words;

    public OriginalAI(MyLexicon lx){
        this.lx=lx;


    }




    public String getWord(String str ){
        words=new ArrayList<>();
        permutate("",str);
        String choice=choose();
        return choice;
    }
    private String choose() {
        if(words.size()==0) return "";
        Collections.sort(words);
        int choice=getRandomNumber(words.size());
        return words.get(choice).str;
    }




    private void permutate(String prefix,String str) {
        int n = str.length();
        if(!lx.startsWith(prefix)) return;

        if(lx.isWord(prefix)){
            int value=evaluate(prefix);
            Word neW=new Word(prefix,value);
            words.add(neW);
        }

        if (n == 0)  return ;
        else {
            for (int i = 0; i < n; i++) {
                permutate(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
            }
        }
    }

    private int evaluate(String prefix) {
        int value=0;
        for(int i=0;i<prefix.length();i++){
            value+=DataStore.getValue(prefix.charAt(i));
        }

        return  value;
    }
    //override
    public boolean riseOrNot(){
        Random ra=new Random();
        int choice=ra.nextInt((10-0)+1);

        return choice<3;
    }
    //to override
    public boolean acceptRise(){
        Random ra=new Random();
        int choice=ra.nextInt((10-0)+1);
        return choice<8;
    }

    public int getRandomNumber(int n) {
        int max=n/4*3;
        int min=n/2;
        Random ra=new Random();
        int choice=ra.nextInt((max-min)+1) +min;

        return choice;
    }


    private class  Word implements  Comparable{
        String str;
        int value;
        public Word(String str,int value){
            this.str=str;
            this.value=value;

        }

        @Override
        public int compareTo(Object o) {
            final int BEFORE = -1;
            final int EQUAL = 0;
            final int AFTER = 1;

            Word ob=(Word) o;

            if (this == ob) return EQUAL;
            if (this.value < ob.value) return BEFORE;
            if (this.value > ob.value) return AFTER;

            return EQUAL;
        }

    }

}


