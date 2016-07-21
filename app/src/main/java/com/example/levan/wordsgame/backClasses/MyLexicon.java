package com.example.levan.wordsgame.backClasses;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by levan on 7/18/2016.
 */
public class MyLexicon implements Serializable {

    private int wordsN=0;
    private  LexiconStructure lexiconRoot;

    public  MyLexicon(){
        lexiconRoot=new LexiconStructure(0,null,false);

    }


    public void addWord(String str){
        LexiconStructure curRoot=lexiconRoot;
        String curWord=str;
        for(int j=0;j<curWord.length();j++){
            LexiconStructure curStructure;
            if(j==curWord.length()-1){
                curStructure=new LexiconStructure(j+1,curWord.charAt(j),true);
                wordsN=wordsN+1;
            }else {
                curStructure=new LexiconStructure(j+1,curWord.charAt(j),false);
            }

            if(!curRoot.contains(curStructure)){
                curRoot.addChild(curStructure);
                curRoot=curStructure;
            }else {
                curRoot=curRoot.getChild(curStructure.hashCode());
                if(j==curWord.length()-1){
                    curRoot.setWord(curStructure.isWord());
                    wordsN=wordsN+1;
                }


            }


        }




    }

    public MyLexicon InitializeLexicon(Context context){
       readDictionary(context);
        return this;
    }

    private void readDictionary(Context context) {
        InputStream is=null;
        try {
            System.out.println("start reading "+new Date().getTime());
            is = context.getResources().openRawResource(context.getResources().getIdentifier("words",
                    "raw", context.getPackageName()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while((line=reader.readLine())!=null)
                addWord(line);

        } catch (IOException e) {
            System.out.println( e.getStackTrace());
            System.out.println("shiiiiiiiit");

        }finally {
            try {
                System.out.println("words  "+wordsN);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end reading "+new Date().getTime());



    }

    public boolean startsWith(String word){
        if(word.equals("")) return  true;
        LexiconStructure curRoot=lexiconRoot;
        for(int i=0;i<word.length();i++){
            LexiconStructure curStruc=new LexiconStructure(i+1,word.charAt(i),false);
            if(!curRoot.contains(curStruc)) return false;
            curRoot=curRoot.getChild(curStruc.hashCode());
        }
        return true;
    }

    public boolean isWord(String word){

        LexiconStructure curRoot=lexiconRoot;
        for(int i=0;i<word.length();i++){
            LexiconStructure curStruc=new LexiconStructure(i+1,word.charAt(i),false);
            if(!curRoot.contains(curStruc)) return false;
            if(i==word.length()-1 && !curRoot.getChild(curStruc.hashCode()).isWord()) return false;
            curRoot=curRoot.getChild(curStruc.hashCode());
        }
        return true;

    }

}
