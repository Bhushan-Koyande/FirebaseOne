package com.example.firebaseone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView logoImage;
    private TextView titleText;

    private int splashTimeout=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        titleText=findViewById(R.id.title);
        logoImage=findViewById(R.id.imageV);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        },splashTimeout);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        logoImage.startAnimation(animation);
        titleText.startAnimation(animation);

    }
}
