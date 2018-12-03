package com.assignment.contactbook;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivityWithDelay(SPLASH_DELAY);
    }

    private void startActivityWithDelay(long delay) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ContactBookActivity.startContactBookActivity(SplashActivity.this);
                finish();
            }
        }, delay);

    }
}
