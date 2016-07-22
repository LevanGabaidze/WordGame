package com.example.levan.wordsgame.backClasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by levan on 7/18/2016.
 */
public class LexiconStructure implements Serializable {
    private HashMap<Integer,LexiconStructure> childs;
    private int depth;
    private Character character;
    private boolean isWord;

    public LexiconStructure(int depth,Character character,boolean isWord){
        this.depth=depth;
        this.character=character;
        this.isWord=isWord;
        childs=new HashMap<Integer, LexiconStructure>();
    }




    public Character getCharacter() {
        return character;
    }


    public int getDepth() {
        return depth;
    }


    public  void setWord(boolean isWord){
        this.isWord=isWord;

    }
    public LexiconStructure getChild(Integer key) {
        return childs.get(key);
    }

    public boolean contains(LexiconStructure lex){
        return  childs.containsKey(lex.hashCode());
    }

    public void addChild(LexiconStructure lex){
        childs.put(lex.hashCode(),lex);
    }

    public boolean isWord() {
        return isWord;
    }

    @Override
    public int hashCode() {
        int result;
        result=character.hashCode()*31+depth;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LexiconStructure)) return false;

        LexiconStructure cur = (LexiconStructure) obj;

        if (character != cur.character) return false;

        return true;
    }
}
