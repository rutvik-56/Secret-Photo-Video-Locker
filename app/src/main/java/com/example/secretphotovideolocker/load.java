package com.example.secretphotovideolocker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class load extends AppCompatActivity {
    FloatingActionButton files,audio,video,photo,newfolder;
    int SELECT_PICTURES=11;
    int count;
    Intent var;
    String xxxx[];
    SharedPreferences sharedPreferences;
    String main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        newfolder=(FloatingActionButton)findViewById(R.id.folder);
        files=(FloatingActionButton)findViewById(R.id.file);
        audio=(FloatingActionButton)findViewById(R.id.audio);
        video=(FloatingActionButton)findViewById(R.id.video);
        photo=(FloatingActionButton)findViewById(R.id.photo);



        final int[] xx = {0};
        sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);

       /* GridView gridview =(GridView)findViewById(R.id.gridview);

        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getBaseContext(),
                        "pic" + (position + 1) + " selected",
                        Toast.LENGTH_SHORT).show();
            }
        });
*/
        newfolder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                final String[] storedir = {sharedPreferences.getString("dir", "")};
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.newfolder, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(load.this);
                alert.setTitle("Create New Folder");
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText e=(EditText)alertLayout.findViewById(R.id.nan);
                        String o=e.getText().toString();
                        if(o.equals(""))
                        {
                            e.setError("Enter Folder Name");
                        }
                        else {
                            if(storedir[0].equals(""))
                            {
                            storedir[0]=o;
                            }
                            else
                            {
                                storedir[0] = storedir[0] +","+o;
                            }

                            String xc[]=storedir[0].split(",");
                            Arrays.sort(xc,String.CASE_INSENSITIVE_ORDER);
                            editor.putString("dir",String.join(",",xc));
                            editor.putString(o,"");
                            editor.commit();
                            Toast.makeText(getBaseContext(), "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();


            }
        });


        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(load.this,xxxx[xx[0]],Toast.LENGTH_LONG).show();
                xx[0]++;

            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fin=null;
                try {
                    fin = openFileInput("myfile");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int c = 0;
                String temp="";
                while(true){
                    try {
                        if (!((c = fin.read()) != -1)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    temp = temp +(char) c;
                }

                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(load.this,temp+"",Toast.LENGTH_LONG).show();

            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return;
                }

                if (ContextCompat.checkSelfPermission(load.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //ask for permission
                    ActivityCompat.requestPermissions(load.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SELECT_PICTURES);
                }

                if(ContextCompat.checkSelfPermission(load.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
                }
            }
        });

    }
     Intent asd;
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURES) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (data.getClipData() != null) {
                         two();
                         asd=data;
                    } else if (data.getData() != null) {
                        count=1;
                        xxxx = new String[count];
                        Uri imageUri =data.getData();
                        xxxx[0] = getPathAPI19(load.this, imageUri);

                    } else {

                    }
                }
            }
                }
            }


   ProgressDialog progressDialog;
    int fi;

            void two()
            {
                progressDialog = new ProgressDialog(this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                final String[] storedir = {sharedPreferences.getString("dir", "")};
                final String s[]=storedir[0].split(",");
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(load.this);
                mBuilder.setTitle("Choose a Folder");
                mBuilder.setCancelable(false);
                mBuilder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fi=i;
                        dialogInterface.dismiss();
                        new DoSomeTask().execute();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }




    @SuppressLint("NewApi")
    public static String getPathAPI19(Context context, Uri uri) {

        if(Build.VERSION.SDK_INT>19) {
            String filePath = "";
            String fileId = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = fileId.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String selector = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, selector, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
        else
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
    }



    class DoSomeTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            final String[] storedir = {sharedPreferences.getString("dir", "")};
            final String s[]=storedir[0].split(",");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                int count = asd.getClipData().getItemCount();
                xxxx = new String[count];

                for (int il = 0; il < count; il++) {
                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    Uri imageUri = asd.getClipData().getItemAt(il).getUri();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    xxxx[il] = getPathAPI19(load.this, imageUri);

                    File directory = wrapper.getDir(s[fi], Context.MODE_PRIVATE);
                    String z[]=xxxx[il].split("/");
                    File file = new File(directory,z[z.length-1]);

                    if (!file.exists()) {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Object o) {

           progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }

      Bitmap []bb;
    private Bitmap [] loadInternalImages(String x){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(x, Context.MODE_PRIVATE);
        File[] imageList = directory.listFiles();
        if(imageList == null){
            imageList = new File[0];
        }
        else
        {
            bb=new Bitmap[imageList.length];
            for(int i=0;i<imageList.length;i++)
            {
                bb[i]=BitmapFactory.decodeFile(imageList[i].getAbsolutePath());
            }
        }
        return bb;
    }


    public class ImageAdapter extends BaseAdapter
    {
        Bitmap b[]=loadInternalImages("xxx");
        private Context context;
        public ImageAdapter(Context c)
        {
            context = c;
        }
        //---returns the number of images---
        public int getCount() {
            return b.length;
        }
        //---returns the item---
        public Object getItem(int position) {
            return position;
        }
        //---returns the ID of an item---
        public long getItemId(int position) {
            return position;
        }
        //---returns an ImageView view---
        public View getView(int position, View convertView,
                            ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new
                        GridView.LayoutParams(285, 230));
                imageView.setScaleType(
                        ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }
            Glide.with(context)
                    .load(b[position])
                    .into(imageView);
            //imageView.setImageBitmap(b[position]);
            return imageView;
        }
    }

}

