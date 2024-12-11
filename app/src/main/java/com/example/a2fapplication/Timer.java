package com.example.a2fapplication;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;

public class Timer {

    private Context context;
    private int time;

    public static void main(String[] args) {
        // Start the countdown timer
//        startTimer();
    }

    public Timer(Context context, int timee) {
        this.context = context;
        time = timee;
    }

    public void startTimer() {
         
        // Create a CountDownTimer for 10 seconds (10,000 milliseconds)
        new CountDownTimer(time, 3000) { // 1000ms (1 second) interval

            @Override
            public void onTick(long millisUntilFinished) {
                // Called every second while the timer is running
                System.out.println("Time remaining: " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                // Called when the timer finishes
                System.out.println("10 seconds are up!");
                UserSession.setTimeValidity(false);
                Toast.makeText(context, "Code time has expired !", Toast.LENGTH_LONG).show();
            }
        }.start();
    }
}
