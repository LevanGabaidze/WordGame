package com.example.levan.wordsgame.backClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by levan on 7/22/2016.
 */
public class GameResult implements Serializable {
    ArrayList<Integer> winners;
    ArrayList<Integer> scores;
    ArrayList<String> words;
    ArrayList<Integer> money;
    ArrayList<Integer> playersNowOut;

    public ArrayList<Integer> getPlayersNowOut() {
        return playersNowOut;
    }

    public void setPlayersNowOut(ArrayList<Integer> playersNowOut) {
        this.playersNowOut = playersNowOut;
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public ArrayList<Integer> getWinners() {
        return winners;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public ArrayList<Integer> getMoney() {
        return money;
    }

    public void setMoney(ArrayList<Integer> money) {
        this.money = money;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }

    public void setWinners(ArrayList<Integer> winners) {
        this.winners = winners;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }
}
