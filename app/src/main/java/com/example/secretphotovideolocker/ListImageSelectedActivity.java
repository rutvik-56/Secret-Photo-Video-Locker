package com.example.secretphotovideolocker;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;

public class ListImageSelectedActivity extends AppCompatActivity {

    private RecyclerView recyclerViewImageSelected;
    ImageSelectedAdapter adapter;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_image_selected);
        recyclerViewImageSelected=findViewById(R.id.recyclerViewSelected);

        Intent intent=getIntent();
        ArrayList<Picture> picturesSelected=intent.getParcelableArrayListExtra("listpicture");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewImageSelected.setLayoutManager(layoutManager);

        adapter=new ImageSelectedAdapter(this,picturesSelected);
        recyclerViewImageSelected.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter!=null){
            recyclerViewImageSelected=null;
            adapter=null;
            Runtime.getRuntime().gc();
        }
        Log.d("destroy","destroy_______________________");
    }
}
