package com.example.cuongphan.multithreadedprogramming_allthreadsdone;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FlipCoinActivity extends Activity {
    private LinearLayout mLinearLayout;
    private TextView mTextView;
    public int max = 0;
    private static final int HEADER_TIMEOUT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mTextView = new TextView(this);
    }

    public void flipCoin(View view) {
        mLinearLayout.removeAllViews();
        mLinearLayout.requestLayout();
        ExecutorService taskList = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 5; i++) {
            taskList.execute(new FlipCoin());
        }
        try {
            taskList.shutdown();
            taskList.awaitTermination(HEADER_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTextView.setText("max consecutive heads: "+max);
        mLinearLayout.addView(mTextView);
    }

    private class FlipCoin implements Runnable {
        @Override
        public void run() {
            int coin_sum = 0;
            int coin;
            Random rd = new Random();
            synchronized (this) {
                for (int i = 0; i < 1000; i++) {
                    coin = rd.nextInt(2);
                    if (coin_sum == (coin_sum + coin)) {
                        coin_sum = 0;
                    } else {
                        coin_sum += coin;
                    }
                    if (coin_sum > max) {
                        max = coin_sum;
                    }
                }
            }
        }
    }
}
