package com.example.levan.wordsgame;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.levan.wordsgame.backClasses.DataStore;
import com.example.levan.wordsgame.backClasses.MyLexicon;

/**
 * Created by Nodar.Kviraia on 7/21/2016.
 */
public class RoomActivity extends AppCompatActivity {

    MyLexicon lex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_room);
        lex = (MyLexicon) getIntent().getSerializableExtra("lex");
        DataStore.createMap(this);


    }


}
