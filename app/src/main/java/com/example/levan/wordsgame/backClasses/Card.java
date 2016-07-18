package com.example.levan.wordsgame.backClasses;

/**
 * Created by levan on 7/18/2016.
 */
public class Card {
    private Character character;
    private int value;

    public Card(Character character,int value){
        this.character=character;
        this.value=value;
    }


    public Character getCharacter() {
        return character;
    }

    public int getValue() {
        return value;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
