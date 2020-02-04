package com.example.secretphotovideolocker;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.khizar1556.mkvideoplayer.MKPlayer;

import java.io.File;

public class vid extends AppCompatActivity {
    Intent i;
    TextView t;
    MKPlayer mkplayer;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid);
        i=getIntent();
        String positionz=i.getStringExtra("pos");
        int position=i.getIntExtra("posi",0);
        sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor =sharedPreferences.edit();
        String z= sharedPreferences.getString("dir", "");
        String x[]=z.split("`");
        String y=x[Integer.parseInt(positionz)];
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File direvctory = wrapper.getDir(y, Context.MODE_PRIVATE);
        File []fo = direvctory.listFiles();
        mkplayer= new  MKPlayer(this);
        mkplayer.play(fo[position].getAbsolutePath());
        mkplayer.setTitle(fo[position].getName());

        mkplayer.setPlayerCallbacks(new MKPlayer.playerCallbacks() {
            @Override
            public void onNextClick() {

            }

            @Override
            public void onPreviousClick() {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mkplayer != null) {
            mkplayer.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mkplayer != null) {
            mkplayer.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mkplayer != null) {
            mkplayer.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mkplayer != null) {
            mkplayer.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (mkplayer != null && mkplayer.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

}
