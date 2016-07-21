package com.example.levan.wordsgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ViewAnimator an = (ViewAnimator) findViewById(R.id.animator);
        CircularProgressView load = (CircularProgressView) findViewById(R.id.progress_view);
        load.startAnimation();

        load.startAnimation();
        an.showNext();

        final MyLexicon  l = new MyLexicon();
        final Context c=this;
        Thread wokr=new Thread(new Runnable() {
            @Override
            public void run() {
                l.InitializeLexicon(c);
            }
        });
        l.InitializeLexicon(this);
        System.out.print("done init");
        System.out.println(l.isWord("Steri"));
        System.out.println(l.startsWith("cici"));
        System.out.println(l.startsWith("gadage"));
        System.out.println(l.startsWith("ck"));
     //   long st=new Date().getTime();
       // System.out.println("start "+ st);
        //permutate("","qwsdfrt");
        //long end=new Date().getTime();
        //System.out.println("end "+(end-st));

        Intent in = new Intent(this,RoomActivity.class);
        in.putExtra("lex",l);
        startActivity(in);
        this.finish();

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


}
