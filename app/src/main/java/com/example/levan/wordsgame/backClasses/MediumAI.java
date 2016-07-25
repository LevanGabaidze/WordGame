package com.example.levan.wordsgame.backClasses;

import java.util.Collections;
import java.util.Random;

/**
 * Created by levan on 7/21/2016.
 */
public class MediumAI extends OriginalAI {
    public MediumAI(MyLexicon lx) {
        super(lx);
    }


    @Override
    public int getRandomNumber(int n) {

        int max=n;
        int min=n/4*3;
        Random ra=new Random();
        int choice=ra.nextInt((max-min)) +min;
        return choice;
    }

    @Override
    public boolean acceptRise() {
        Random ra=new Random();
        int choice=ra.nextInt((10-0)+1);
        return choice<5;
    }

    @Override
    public boolean riseOrNot() {
        Random ra=new Random();
        int choice=ra.nextInt((10-0)+1);
        return choice<5;
    }
}

