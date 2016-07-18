package com.example.levan.wordsgame.backClasses;

import java.util.HashSet;
import java.util.List;

/**
 * Created by levan on 7/18/2016.
 */
public class MyLexicon {

    private  LexiconStructure lexiconRoot;

    public  MyLexicon(){
        lexiconRoot=new LexiconStructure(0,null,false);

    }

    public MyLexicon InitializeLexicon(List<String> words){

        for(int i=0;i<words.size();i++){
            LexiconStructure curRoot=lexiconRoot;
            String curWord=words.get(i);
            for(int j=0;j<curWord.length();i++){
                LexiconStructure curStructure;
                if(j==curWord.length()-1){
                     curStructure=new LexiconStructure(j+1,curWord.charAt(j),true);
                }else {
                     curStructure=new LexiconStructure(j+1,curWord.charAt(j),false);
                }

                if(!curRoot.contains(curStructure)){
                    curRoot.addChild(curStructure);
                    curRoot=curStructure;
                }else {
                    curRoot=curRoot.getChild(curStructure.hashCode());
                    curRoot.setWord(curStructure.isWord());
                }

            }


        }

        return this;
    }

    public boolean startsWith(String word){
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
