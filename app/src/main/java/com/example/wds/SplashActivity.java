package com.example.wds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private ImageView splash_logo;
    private static int splashTimeOut=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash_logo= (ImageView)findViewById(R.id.splash_logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(SplashActivity.this, login_Activity.class);
                startActivity(i);
            }
        },splashTimeOut);


        Animation a = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        splash_logo.startAnimation(a);

    }
}
