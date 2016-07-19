package com.example.levan.wordsgame.backClasses;

import java.util.HashMap;

/**
 * Created by levan on 7/19/2016.
 */
public class Node {
        public Boolean endOfWord = false; //Does this Node mark the end of a particular word?
        public HashMap<Character,Node> children = new HashMap<Character,Node>();

}
