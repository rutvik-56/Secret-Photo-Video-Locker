package com.example.secretphotovideolocker;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public class imhjk extends AppCompatActivity {

    Intent i;
    TextView t;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imhjk);
        t=(TextView)findViewById(R.id.ba);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        imageView.setImage(ImageSource.uri(fo[position].getAbsolutePath()));
    }
}
