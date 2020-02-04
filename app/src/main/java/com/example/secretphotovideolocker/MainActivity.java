package com.example.secretphotovideolocker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewGallery;
    private ArrayList<Picture> pictures;
    SharedPreferences sharedPreferences;
    GalleryItemAdapter adapter;
    Handler handler;

    private ImageView imageViewButtonSend;
    private TextView textViewSelectedCount;
    private ConstraintLayout constraintLayoutSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        imageViewButtonSend = findViewById(R.id.button_send);
        textViewSelectedCount = findViewById(R.id.textViewSeletedCount);
        constraintLayoutSend = findViewById(R.id.layoutSend);


        ImageView imageViewSendDetail = findViewById(R.id.button_send);
        imageViewSendDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDetail = new Intent(MainActivity.this,MainActivitym.class);
                ArrayList<Picture> picturesSelected = adapter.getAllPictureSelected();
                Uri u=Uri.fromFile(new File(picturesSelected.get(0).getPath()+""));
                sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor =sharedPreferences.edit();
                editor.putString("check","1");
                editor.commit();
                intentDetail.putParcelableArrayListExtra("listpicture", picturesSelected);
                startActivity(intentDetail);
                finish();
            }
        });

        pictures = new ArrayList<>();
        recyclerViewGallery = findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GalleryItemAdapter(this, pictures, new GalleryItemAdapter.ItemSelectedChangeListener() {
            @Override
            public void onItemSelectedChange(int number) {
                if (number > 0) {
                    constraintLayoutSend.setVisibility(View.VISIBLE);
                    textViewSelectedCount.setText(number + "");
                } else {
                    constraintLayoutSend.setVisibility(View.GONE);
                }
            }
        });
        recyclerViewGallery.setAdapter(adapter);
        handler = new Handler();
        if (isReadStoragePermissionGranted()) {

            new Thread() {

                @Override
                public void run() {
                    Looper.prepare();
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            pictures.clear();
                            pictures.addAll(Picture.getGalleryPhotos(MainActivity.this));
                           // Uri u[]=Picture.getGalleryPhotos(MainActivity.this);
                            adapter.notifyDataSetChanged();

                        }
                    });
                    Looper.loop();
                }


            }.start();
        }
    }


    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Thread() {

                    @Override
                    public void run() {
                        Looper.prepare();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                pictures.clear();
                                pictures.addAll(Picture.getGalleryPhotos(MainActivity.this));
                                adapter.notifyDataSetChanged();

                            }
                        });
                        Looper.loop();
                    }
                }.start();

            } else {
                Libraries.requestPermissionStorageDeny(MainActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
