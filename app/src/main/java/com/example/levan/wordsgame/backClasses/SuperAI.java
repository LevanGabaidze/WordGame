package com.example.levan.wordsgame.backClasses;

import java.util.Random;

/**
 * Created by levan on 7/24/2016.
 */
public class SuperAI  extends  OriginalAI{

    public SuperAI(MyLexicon lx) {
        super(lx);
    }

    @Override
    public int getRandomNumber(int n) {


        return n-1;
    }

    @Override
    public boolean acceptRise() {
        Random ra=new Random();
        int choice=ra.nextInt((10-0)+1);
        return choice<7;
    }

    @Override
    public boolean riseOrNot() {
        Random ra=new Random();
        int choice=ra.nextInt((10-0)+1);
        return choice<8;
    }
}
