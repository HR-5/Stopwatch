package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyCanvasView myCanvasView;
    Chronometer chronometer;
    boolean running;
    int seconds,minu;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        myCanvasView = new MyCanvasView(this);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.container);
        constraintLayout.addView(myCanvasView);
        running = true;
    }

    public void startTime(View v) {
        handler.removeCallbacks(runnable);
        running = true;
        if(seconds==0) {
            myCanvasView.drawsecond(30, 60);
            seconds++;
        }
        else {
            myCanvasView.drawsecond(seconds, minu);
            seconds++;
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                if (running) {
                    int sec;
                    int min = seconds / 60;
                    if (seconds % 30 == 0) {
                        sec = 30;
                    } else
                        sec = seconds % 30;
                    if (min % 60 == 0)
                        min = 60;
                    else
                        min = min % 60;
                    minu = min;
                    myCanvasView.drawsecond(sec, min);
                    seconds++;
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void pause(View v){
        running = false;
        handler.removeCallbacks(runnable);
    }

    public void reset(View v) {
        myCanvasView.clear();
        handler.removeCallbacks(runnable);
        seconds = 0;
        running = false;
    }
}
