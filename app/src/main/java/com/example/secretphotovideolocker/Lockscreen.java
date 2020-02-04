package com.example.secretphotovideolocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class Lockscreen extends AppCompatActivity {
    PinLockView mPinLockView;
    SharedPreferences sharedPreferences;
    int which=-1;
    TextView forgot,info;
    String lock="";
    int tryi=3;
    private IndicatorDots mIndicatorDots;
    String pp="";
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);
        forgot=(TextView)findViewById(R.id.forgot);
        info=(TextView)findViewById(R.id.info);
         vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        lock = sharedPreferences.getString("lock", "");

        if(lock.equals(""))
        {
            which=0;
            info.setText("Create a New PassCode");
        }
        else
        {
            which=1;
            info.setText("Enter PassCode");
        }

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.enableLayoutShuffling();
        mPinLockView.setPinLength(6);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            if(which==1)
            {
                if(lock.equals(pin))
                {
                    Intent i=new Intent(Lockscreen.this,MainActivitym.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                    else
                    {
                        vibrator.vibrate(200);
                    }
                    info.setText("Wrong Passcode");
                    mPinLockView.enableLayoutShuffling();
                    info.setTextColor(Color.RED);
                    mPinLockView.resetPinLockView();

                     forgot.setVisibility(View.VISIBLE);
                }
            }

            else
            {
                if(pp.equals("")) {
                    mPinLockView.resetPinLockView();
                    info.setText("Re-Enter PassCode");
                    mPinLockView.enableLayoutShuffling();
                    pp = pin;
                }
                else
                {
                    if(pp.equals(pin))
                    {
                        sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        Toast.makeText(Lockscreen.this,"Passcode created successful",Toast.LENGTH_SHORT).show();
                        editor.putString("lock",pin);
                        editor.commit();
                        Intent intent=new Intent(Lockscreen.this,MainActivitym.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        info.setText("Please Enter Same Passcode");
                        mPinLockView.enableLayoutShuffling();
                        info.setTextColor(Color.RED);
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                        }
                        else
                        {
                            vibrator.vibrate(200);
                        }
                        tryi--;
                        mPinLockView.resetPinLockView();

                        if(tryi==0)
                        {
                            info.setTextColor(Color.WHITE);
                            mPinLockView.enableLayoutShuffling();
                            pp="";
                        }

                    }
                }
            }
        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };


}
