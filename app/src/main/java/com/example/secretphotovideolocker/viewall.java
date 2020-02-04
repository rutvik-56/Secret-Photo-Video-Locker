package com.example.secretphotovideolocker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.util.Arrays;

public class viewall extends AppCompatActivity {

    GridView gridView;
    SharedPreferences sharedPreferences;
    String positionz="0";
    File[] fo;
    FloatingActionButton delete,unhide;
    TextView select;
    String type[],name[];
    Bitmap imagefolder[];
    CheckBox radioButtons[];
    boolean checkposition[];
    ProgressDialog progressDialog;
    TextView t;
    int change=0;
    Intent i;
    Vibrator vibrator;
    int haa=0,onlycheck=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewall);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        progressDialog = new ProgressDialog(viewall.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        select=(TextView)findViewById(R.id.sel);
        gridView=(GridView)findViewById(R.id.gridho);
        delete=(FloatingActionButton)findViewById(R.id.delete);
        unhide=(FloatingActionButton)findViewById(R.id.unhide);

        i=getIntent();
        positionz=i.getStringExtra("posi");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (change != 0) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(viewall.this);
                    builder.setMessage("Are You Sure you want to Delete This File ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    progressDialog = new ProgressDialog(viewall.this);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setTitle("Please Wait..");
                                    progressDialog.setMessage("Loading...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    new y().execute();
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Delete File(s)");
                    alert.show();


                } else {
                    Toast.makeText(viewall.this, "You Are Not Selected Any Items.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        unhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change != 0) {

                    String h[]=new String[2];
                    h[0]="Default Path";
                    h[1]="Choose Other Path";
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(viewall.this);
                    mBuilder.setTitle("Choose a Folder");
                    mBuilder.setCancelable(false);
                    mBuilder.setSingleChoiceItems(h, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if(i==0)
                            {
                                progressDialog = new ProgressDialog(viewall.this);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setTitle("Please Wait..");
                                progressDialog.setMessage("Processing...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                new AsyncTask()
                                {

                                    String path[];
                                    @Override
                                    protected void onPreExecute() {
                                        sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
                                        final SharedPreferences.Editor editor =sharedPreferences.edit();
                                        String z1= sharedPreferences.getString("dir", "");
                                        String d[]=z1.split("`");
                                        String dir=d[Integer.parseInt(positionz)];
                                        final String storedirpath =sharedPreferences.getString(dir, "");
                                        path=storedirpath.split("`");
                                        super.onPreExecute();
                                    }

                                    @Override
                                    protected void onPostExecute(Object o) {
                                        new y().execute();
                                        progressDialog.dismiss();
                                        super.onPostExecute(o);
                                    }

                                    @Override
                                    protected Object doInBackground(Object[] objects) {
                                        int changeble=0;
                                        for(int i=0;i<checkposition.length;i++)
                                        {
                                            if(checkposition[i])
                                            {
                                               String a[]=fo[i].getName().split("~");
                                               String pp="";
                                               for(int j=0;j<path.length;j++)
                                               {
                                                   String zz[]=path[j].split("/");

                                                   if(a[1].equals(zz[zz.length-1]))
                                                   {
                                                       for(int z=0;z<zz.length-1;z++)
                                                       {
                                                           if(z!=zz.length-2) {
                                                               pp = pp + zz[z] + "/";
                                                           }
                                                           else
                                                           {
                                                               pp=pp+zz[z];
                                                           }
                                                       }
                                                       break;
                                                   }
                                               }
                                                String myfolder=pp;
                                                File f=new File(myfolder);
                                                if(!f.exists())
                                                    if(!f.mkdir()){

                                                    }
                                                String[] d=fo[i].getName().split("~");
                                                copyFile(fo[i],new File(pp,d[1]+""));

                                            }

                                        }
                                        return null;
                                    }
                                }.execute();
                            }
                            else {
                                final Intent chooserIntent = new Intent(viewall.this, DirectoryChooserActivity.class);

                                final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                                        .newDirectoryName("Secret Photo Video Locker")
                                        .allowReadOnlyDirectory(true)
                                        .allowNewDirectoryNameModification(true)
                                        .build();
                                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
                                startActivityForResult(chooserIntent, 1010);
                            }
                        }
                    });
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();

                } else {
                    Toast.makeText(viewall.this, "You Are Not Selected Any Items.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        new x().execute();
        t=(TextView)findViewById(R.id.ba);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    String PATH="";


    public boolean copyFile(File src, File dst) {
        boolean returnValue = true;

        FileChannel inChannel = null, outChannel = null;

        try {

            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();

        } catch (FileNotFoundException fnfe) {

            fnfe.printStackTrace();
            return false;
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);

        } catch (IllegalArgumentException iae) {

            iae.printStackTrace();
            returnValue = false;

        } catch (NonReadableChannelException nrce) {

            nrce.printStackTrace();
            returnValue = false;

        } catch (NonWritableChannelException nwce) {

            nwce.printStackTrace();
            returnValue = false;

        } catch (ClosedByInterruptException cie) {

            cie.printStackTrace();
            returnValue = false;

        } catch (AsynchronousCloseException ace) {

            ace.printStackTrace();
            returnValue = false;

        } catch (ClosedChannelException cce) {

            cce.printStackTrace();
            returnValue = false;

        } catch (IOException ioe) {

            ioe.printStackTrace();
            returnValue = false;


        } finally {

            if (inChannel != null)

                try {

                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (outChannel != null)
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        return returnValue;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1010) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                PATH=data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                new xx().execute();
            } else {
                // Nothing selected
            }
        }
    }


    class xx1 extends AsyncTask
    {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {

            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            for(int i=0;i<fo.length;i++) {

                if(checkposition[i]) {
                    fo[i].delete();
                }
            }
            return null;
        }
    }


    class xx extends AsyncTask
    {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            new xx1().execute();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            for(int i=0;i<fo.length;i++) {

                if(checkposition[i]) {
                    String[] d=fo[i].getName().split("~");
                    copyFile(fo[i],new File(PATH,d[1]+""));
                }
            }
            return null;
        }
    }


   class y extends AsyncTask
    {
        int xx=0;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {

                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            for(int i=0;i<fo.length;i++) {

                if(checkposition[i]) {
                  fo[i].delete();

                }
            }


            return null;
        }
    }



    class x extends AsyncTask
    {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
           gridView.setAdapter(new adapp(getBaseContext(),name,type,imagefolder));
           haa=1;
           progressDialog.dismiss();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor =sharedPreferences.edit();
            String z= sharedPreferences.getString("dir", "");
            String x[]=z.split("`");
            String y=x[Integer.parseInt(positionz)];
            ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
            File direvctory = wrapper.getDir(y, Context.MODE_PRIVATE);
            fo = direvctory.listFiles();
            int sdf=fo.length;
            name=new String[sdf];
            type=new String[sdf];
            radioButtons=new CheckBox[sdf];
            imagefolder=new Bitmap[sdf];
            checkposition=new boolean[sdf];
            Arrays.fill(checkposition,false);
            for(int i=0;i<sdf;i++)
            {
                String l=fo[i].getName();
                String c[]=l.split("~");
                type[i]=c[0];
                name[i]=c[1];

                if(type[i].equals("v"))
                {
                    try {

                        Bitmap b=retriveVideoFrameFromVideo(fo[i].getAbsolutePath());
                        imagefolder[i]=getResizedBitmap(b);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                else if(type[i].equals("p")) {
                    imagefolder[i] = getResizedBitmap(BitmapFactory.decodeFile(fo[i].getAbsolutePath()));

                }
                else if(type[i].equals("a"))
                {
                    imagefolder[i] = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(viewall.this.getResources(), R.drawable.audd), 150, 140, false);
                }
                else if(type[i].equals("t"))
                {
                    imagefolder[i] = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(viewall.this.getResources(), R.drawable.docr), 150, 140, false);
                }
            }

            return null;
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int narrowSize = Math.min(width, height);
        int differ = (int)Math.abs((bm.getHeight() - bm.getWidth())/2.0f);
        width  = (width  == narrowSize) ? 0 : differ;
        height = (width == 0) ? differ : 0;

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, width, height, narrowSize, narrowSize);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm=null;
        }
        return resizedBitmap;
    }

     int xx1=0;
    class adapp extends BaseAdapter
    {
        Context context;
        String[] allname;
        String[] alltype;
        Bitmap[] allimg;
        LayoutInflater inflater;

        public adapp(Context context, String[] allname, String[] alltype, Bitmap[] allimg) {
            this.context = context;
            this.allimg=allimg;
            this.allname=allname;
            this.alltype=alltype;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public int getCount() {
            return type.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.gridshow, null);
            TextView textView = (TextView) convertView
                    .findViewById(R.id.textviewgridview);

            ImageView imageView=(ImageView)convertView
                    .findViewById(R.id.imageviewgrid);
            radioButtons[position]=(CheckBox) convertView
                    .findViewById(R.id.radiobutton);
            TextView yu=(TextView)convertView
                    .findViewById(R.id.mumk);
            LinearLayout listView=(LinearLayout) convertView.findViewById(R.id.xcvm);

            listView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    unhide.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                    else
                    {
                        vibrator.vibrate(200);
                    }
                        for (int i = 0; i < checkposition.length; i++) {
                            if (radioButtons[i] != null) {
                                radioButtons[i].setVisibility(View.VISIBLE);
                            }
                        }
                   onlycheck=1;
                        xx1=1;
                    return false;
                }
            });


            listView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (onlycheck == 1) {
                        if (radioButtons[position].isChecked()) {
                            radioButtons[position].setChecked(false);
                            checkposition[position] = false;
                            change++;
                        } else {
                            radioButtons[position].setChecked(true);
                            checkposition[position] = true;
                            change--;
                        }
                    } else {
                        if (type[position].equals("p")) {
                            Intent i = new Intent(viewall.this, imhjk.class);
                            i.putExtra("pos", positionz);
                            i.putExtra("posi", position);
                            startActivity(i);
                        } else if (type[position].equals("a") || type[position].equals("v")) {
                            Intent i = new Intent(viewall.this, vid.class);
                            i.putExtra("pos", positionz);
                            i.putExtra("posi", position);
                            startActivity(i);
                        } else {
                            Toast.makeText(viewall.this, "You Can't Open This File.", Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            });


            radioButtons[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(radioButtons[position].isChecked())
                    {
                        checkposition[position]=false;
                        change++;
                    }
                    else
                    {
                        checkposition[position]=true;
                        change--;
                    }

                }
            });

            textView.setText(allname[position]);
            imageView.setImageBitmap(allimg[position]);
            if(alltype[position].equals("v"))
            {
                yu.setVisibility(View.VISIBLE);
            }

            if(onlycheck==1)
            {
                radioButtons[position].setVisibility(View.VISIBLE);
            }

            return convertView;
        }

    }

    @SuppressLint("NewApi")
    public static Bitmap retriveVideoFrameFromVideo(String p_videoPath)
            throws Throwable
    {
        Bitmap m_bitmap = null;
        MediaMetadataRetriever m_mediaMetadataRetriever = null;
        try
        {
            m_mediaMetadataRetriever = new MediaMetadataRetriever();
            m_mediaMetadataRetriever.setDataSource(p_videoPath);
            m_bitmap = m_mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception m_e)
        {
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String p_videoPath)"
                            + m_e.getMessage());
        }
        finally
        {
            if (m_mediaMetadataRetriever != null)
            {
                m_mediaMetadataRetriever.release();
            }
        }
        return m_bitmap;
    }

    @Override
    public void onBackPressed() {


        if(xx1==1) {
            xx1=0;
            onlycheck = 0;
            for (int i = 0; i < checkposition.length; i++) {
                if (radioButtons[i] != null) {
                    checkposition[i]=false;
                    radioButtons[i].setVisibility(View.INVISIBLE);
                }
            }
            unhide.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }
}
