package com.example.partyplay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView quote,person;
    String[] quotes;
    int count=0;
    String[] people;
    int delay;

    public static final int MY_INTERNET_PERMISSION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_INTERNET_PERMISSION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            int color = Color.parseColor("#6600ff");

            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }

        quote = findViewById(R.id.quote);
        person = findViewById(R.id.person);
        quotes = getResources().getStringArray(R.array.quotes);
        people = getResources().getStringArray(R.array.people);

        delay = 3000;
        setQuote();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runagain();
            }
        },0,5000);



    }




    private void runagain(){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("RunnableMine", "In here");
                setQuote();
                Looper.loop();
            }

        }, 0);


    }
    private void setQuote(){
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        Animation in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        Random rand = new Random();
        int index= rand.nextInt(11);
        quote.startAnimation(out);
        person.startAnimation(out);
        quote.setText(quotes[index]);
        person.setText(people[index]);
        quote.startAnimation(in);
        person.startAnimation(in);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_INTERNET_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Internet Siktu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickedAdd(View view) {
        //Toast.makeText(this, "Clicked Add", Toast.LENGTH_SHORT).show();
        count= 1;
        Intent i = new Intent(this,AddMusic.class);
        startActivity(i);
        finish();

    }

    public void clickedPlay(View view) {
        //Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
        count= 1;
        Intent i = new Intent(this,PlayMusic.class);
        startActivity(i);
        finish();

    }
}
