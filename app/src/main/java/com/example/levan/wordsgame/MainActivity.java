package com.example.levan.wordsgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ViewAnimator;

import com.example.levan.wordsgame.backClasses.MyLexicon;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public int k=0;
    ArrayList<String> arr;
    Button play, multy, exit, begin, back;
    RadioButton easyDif, np1, np2, np3;
    AppCompatActivity c;


    private void initButtons() {
        back = (Button) findViewById(R.id.back_btn);
        play = (Button) findViewById(R.id.start_play);
        exit = (Button) findViewById(R.id.btn_exit);
        multy = (Button) findViewById(R.id.multiPlayer);
        begin = (Button) findViewById(R.id.begin_game);
        easyDif = (RadioButton) findViewById(R.id.diff_lvl1);
        np1 = (RadioButton) findViewById(R.id.oponent_q1);
        np2 = (RadioButton) findViewById(R.id.oponent_q2);
        np3 = (RadioButton) findViewById(R.id.oponent_q3);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int np = getNp();
                int diff;
                if (easyDif.isChecked()) diff = 1; else diff = 2;
                Intent in = new Intent(c,RoomActivity.class);
                in.putExtra("diff",diff);
                in.putExtra("np",np);
                startActivity(in);
                c.finish();
            }
        });


    }

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        h = new Handler();
        c=this;
        initButtons();
        final ViewAnimator an = (ViewAnimator) findViewById(R.id.animator);
        final CircularProgressView load = (CircularProgressView) findViewById(R.id.progress_view);
        play =(Button) findViewById(R.id.start_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                an.showNext();
               // Intent in = new Intent(c,RoomActivity.class);
                //startActivity(in);
                //c.finish();

            }
        });
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        load.stopAnimation();
                        an.showNext();
                    }
                });

            }
        });
        th.start();
        load.startAnimation();

        //an.showNext();

       // Intent in = new Intent(this,RoomActivity.class);
       // startActivity(in);
        //this.finish();

    }


    private void permutate(String prefix,String str) {
        int n = str.length();
        System.out.println(prefix);
        if (n == 0) ;
        else {
            for (int i = 0; i < n; i++) {
                k += 1;
                permutate(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
            }
        }
    }


    public int getNp() {
        if(np3.isChecked()) return 3;
        if (np2.isChecked()) return 2;
        return 1;
    }
}
