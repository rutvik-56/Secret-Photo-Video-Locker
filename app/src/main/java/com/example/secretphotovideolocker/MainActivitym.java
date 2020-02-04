package com.example.secretphotovideolocker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.Arrays;



public class MainActivitym extends AppCompatActivity{
    FloatingActionButton files,audio,video,photo,newfolder,camera,delete,unhide,setting;
    FloatingActionsMenu fme;
    SharedPreferences sharedPreferences;
    int SELECT_PICTURES=11;
    int chooser;
    LinearLayout az;
    CheckBox radioButtons[];
    boolean checkposition[];
    private static final int CAMERA_PHOTO = 111;
    String xxxx[];
    TextView select;
   RelativeLayout fg,fig;
    int change=0;
    Intent intent;
    Vibrator vibrator;
    int haa=0,onlycheck=0;
    String foldername[],folderitems[];
    Bitmap imagefolder[];
    ArrayList<Picture> picturesSelected;
    ListView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainm);
        gridView = (ListView) findViewById(R.id.gridview);
        fme = (FloatingActionsMenu) findViewById(R.id.rtgh);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        fg = (RelativeLayout) findViewById(R.id.ovtp);
        az = (LinearLayout) findViewById(R.id.asdd);
        select = (TextView) findViewById(R.id.sel);

        az.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fme.collapse();
            }
        });


        if (ContextCompat.checkSelfPermission(MainActivitym.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivitym.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    SELECT_PICTURES);
        }
        if (ContextCompat.checkSelfPermission(MainActivitym.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(MainActivitym.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SELECT_PICTURES);
        }
        new Dolist().execute();

        sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String z = sharedPreferences.getString("dir", "");

        if (z.equals("")) {
            z = "Documents`Musics`Pictures`Videos";
            editor.putString("dir", z);
            editor.commit();
            new Dolist().execute();

        }


        newfolder = (FloatingActionButton) findViewById(R.id.folder);
        files = (FloatingActionButton) findViewById(R.id.filez);
        audio = (FloatingActionButton) findViewById(R.id.audio);
        video = (FloatingActionButton) findViewById(R.id.video);
        photo = (FloatingActionButton) findViewById(R.id.photo);
        camera = (FloatingActionButton) findViewById(R.id.camera);
        delete = (FloatingActionButton) findViewById(R.id.delete);
        unhide = (FloatingActionButton) findViewById(R.id.unhide);
        setting = (FloatingActionButton) findViewById(R.id.setho);


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent to=new Intent(MainActivitym.this,Setting.class);
                startActivity(to);*/


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change != 0) {
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    final String storedir = sharedPreferences.getString("dir", "");
                    String dir[] = storedir.split("`");
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(MainActivitym.this);
                            builder.setMessage("Are You Sure you want to Delete This File ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                           progressDialog = new ProgressDialog(MainActivitym.this);
                                            progressDialog.setIndeterminate(true);
                                            progressDialog.setTitle("Please Wait..");
                                            progressDialog.setMessage("Loading...");
                                            progressDialog.setCancelable(false);
                                            progressDialog.show();
                                             new y1().execute();

                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.setTitle("Delete File(s)");
                            alert.show();


                        } else {
                            Toast.makeText(MainActivitym.this, "You Are Not Selected Any Items.", Toast.LENGTH_SHORT).show();

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
                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivitym.this);
                    mBuilder.setTitle("Choose a Folder");
                    mBuilder.setCancelable(false);
                    mBuilder.setSingleChoiceItems(h, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                          chooser=i;
                            progressDialog = new ProgressDialog(MainActivitym.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setTitle("Please Wait..");
                            progressDialog.setMessage("Processing...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            if(i==0)
                            {

                                new xxrt().execute();
                            }
                            else
                            {
                                final Intent chooserIntent = new Intent(MainActivitym.this, DirectoryChooserActivity.class);
                                final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                                        .newDirectoryName("Secret Photo Video Locker")
                                        .allowReadOnlyDirectory(true)
                                        .allowNewDirectoryNameModification(true)
                                        .build();
                                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
                                startActivityForResult(chooserIntent, 1210);
                            }
                        }
                    });

                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();


                } else {
                    Toast.makeText(MainActivitym.this, "You Are Not Selected Any Items.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(videoIntent, "Select Audio"), 234);
            }
        });


        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                    startActivityForResult(
                            intent,
                            340);
            }
        });


video.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent= new Intent(MainActivitym.this, Gallery.class);
        intent.putExtra("title","Select media");
        intent.putExtra("mode",3);
        intent.putExtra("maxSelection",3);
        startActivityForResult(intent,108);

    }
});

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int MY_PERMISSIONS_REQUEST_CAMERA=0;
                // Here, this is the current activity
                if (ContextCompat.checkSelfPermission(MainActivitym.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivitym.this, Manifest.permission.CAMERA))
                    {

                    }
                    else
                    {
                        ActivityCompat.requestPermissions(MainActivitym.this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );

                    }
                }
                else
                {
                    Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(chooserIntent, CAMERA_PHOTO);
                }


            }
        });


        newfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor =sharedPreferences.edit();
                final String[] storedir = {sharedPreferences.getString("dir", "")};
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.newfolder, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivitym.this);
                alert.setTitle("Create New Folder");
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @SuppressLint({"NewApi", "StaticFieldLeak"})
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText e=(EditText)alertLayout.findViewById(R.id.nan);
                        final String o=e.getText().toString();
                        if(o.equals(""))
                        {
                            e.setError("Enter Folder Name");
                        }
                        else {

                            new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object[] objects) {

                                    storedir[0] = o+"`"+storedir[0];
                                    String []d=storedir[0].split("`");
                                     Arrays.sort(d,String.CASE_INSENSITIVE_ORDER);
                                     String xx=String.join("`",d);
                                    editor.putString("dir",xx);
                                    editor.commit();
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object o) {

                                    Toast.makeText(getBaseContext(), "Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    overridePendingTransition(0, 0);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    super.onPostExecute(o);

                                }
                            }.execute();
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


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (ContextCompat.checkSelfPermission(MainActivitym.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //ask for permission
                    ActivityCompat.requestPermissions(MainActivitym.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            SELECT_PICTURES);
                }
                else
                {
                    Intent intent = new Intent(MainActivitym.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });


        if(sharedPreferences.getString("check", "").equals("1"))
        {
            intent=getIntent();
            picturesSelected=intent.getParcelableArrayListExtra("listpicture");
            two();
            editor.putString("check","0");
            editor.commit();
        }
    }


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


    boolean etra[];


    class xxrt extends AsyncTask
    {

        String dir[];

        @Override
        protected void onPreExecute() {
            etra=new boolean[checkposition.length];
            createFolder("Secret Photo Video Locker");
            final String storedir = sharedPreferences.getString("dir", "");
            dir = storedir.split("`");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            new y11().execute();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            for(int i=0;i<checkposition.length;i++) {
                 etra[i]=checkposition[i];
                if(checkposition[i]) {
                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File xc = wrapper.getDir(dir[i], Context.MODE_PRIVATE);
                    String Path;
                    File[] z=xc.listFiles();

                    if(chooser==0) {
                        createFolderx(dir[i]);
                        Path = "/storage/emulated/0/Secret Photo Video Locker/" + dir[i];

                        for(int j=0;j<z.length;j++)
                        {
                            String[] d=z[j].getName().split("~");
                            copyFile(z[j],new File(Path,d[1]+""));
                        }
                    }
                    else
                    {
                        createFoldery(dir[i]);
                        Path =PATH+"/"+ dir[i];

                        for(int j=0;j<z.length;j++)
                        {
                            String[] d=z[j].getName().split("~");
                            copyFile(z[j],new File(Path,d[1]+""));
                        }
                    }
                }
            }

            return null;
        }
    }
    public void createFoldery(String fname){
        String myfolder=PATH+"/"+fname;
        File f=new File(myfolder);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(this, myfolder+" can't be created.", Toast.LENGTH_SHORT).show();

            }
    }

    ProgressDialog progressDialog;
    int fi;

    void two()
    {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String[] storedir = {sharedPreferences.getString("dir", "")};
        final String s[]=storedir[0].split("`");
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivitym.this);
        mBuilder.setTitle("Choose a Folder");
        mBuilder.setCancelable(false);
        mBuilder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fi=i;
                dialogInterface.dismiss();
                progressDialog = new ProgressDialog(MainActivitym.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new DoSomeTask().execute();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    public void createFolder(String fname){
        String myfolder=Environment.getExternalStorageDirectory()+"/"+fname;
        File f=new File(myfolder);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(this, myfolder+" can't be created.", Toast.LENGTH_SHORT).show();
            }
    }

    public void createFolderx(String fname){
        String myfolder=Environment.getExternalStorageDirectory()+"/Secret Photo Video Locker/"+fname;
        File f=new File(myfolder);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(this, myfolder+" can't be created.", Toast.LENGTH_SHORT).show();

            }
    }

    void one()
    {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String[] storedir = {sharedPreferences.getString("dir", "")};
        final String s[]=storedir[0].split("`");
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivitym.this);
        mBuilder.setTitle("Choose a Folder");
        mBuilder.setCancelable(false);
        mBuilder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fi=i;
                dialogInterface.dismiss();
                progressDialog = new ProgressDialog(MainActivitym.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new DoSomeTaskimage().execute();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    void three()
    {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String[] storedir = {sharedPreferences.getString("dir", "")};
        final String s[]=storedir[0].split("`");
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivitym.this);
        mBuilder.setTitle("Choose a Folder");
        mBuilder.setCancelable(false);
        mBuilder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fi=i;
                dialogInterface.dismiss();
                progressDialog = new ProgressDialog(MainActivitym.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new DoSomeTaskvideo().execute();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    void fourth()
    {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String[] storedir = {sharedPreferences.getString("dir", "")};
        final String s[]=storedir[0].split("`");
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivitym.this);
        mBuilder.setTitle("Choose a Folder");
        mBuilder.setCancelable(false);
        mBuilder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fi=i;
                dialogInterface.dismiss();
                progressDialog = new ProgressDialog(MainActivitym.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new DoSomeTaskaudio().execute();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    void fifth()
    {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String[] storedir = {sharedPreferences.getString("dir", "")};
        final String s[]=storedir[0].split("`");
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivitym.this);
        mBuilder.setTitle("Choose a Folder");
        mBuilder.setCancelable(false);
        mBuilder.setSingleChoiceItems(s, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fi=i;
                dialogInterface.dismiss();
                progressDialog = new ProgressDialog(MainActivitym.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setTitle("Please Wait..");
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new DoSomeTasktext().execute();

            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    Uri uu;
    String PATH="";
    ArrayList<String> selectionResult;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_PHOTO)
        {
            if(data!=null) {
                uu = data.getData();
                one();
            }

        }
       else if(requestCode==108)
        {
            if(data!=null) {
                    selectionResult=data.getStringArrayListExtra("result");
                three();
            }
        }
        else if(requestCode==234)
        {
            if(data!=null) {
                uu = data.getData();
                fourth();
            }
        }
        else if(requestCode==340)
        {
            if(data!=null) {
                uu = data.getData();
                fifth();
            }
        }
        else  if (requestCode == 1210) {
            if(data!=null)
            {
                PATH=data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                new xxrt().execute();
            } else {

            }
        }
        else if(requestCode==107)
        {
            if(data!=null)
            {
                PATH=data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                new xxrt().execute();
            } else {

            }
        }

    }



    class DoSomeTaskaudio extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            final String[] storedir = {sharedPreferences.getString("dir", "")};
            final String s[]=storedir[0].split("`");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                File newfile;
                String xxx=getPathx(getBaseContext(),uu);
                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                File directory = wrapper.getDir(s[fi], Context.MODE_PRIVATE);
                String z[]=xxx.split("/");
                String storepath = sharedPreferences.getString(s[fi], "");

                if(storepath.equals(""))
                {
                    editor.putString(s[fi],xxx);
                    editor.commit();
                }
                else
                {
                    storepath=storepath+"`"+xxx;
                    editor.putString(s[fi],storepath);
                    editor.commit();
                }


                editor.putString(s[fi],"list");
                editor.commit();
                File file = new File(directory,z[z.length-1]);
                try {

                    File currentFile = new File(xxx);
                    String fileName = z[z.length-1];

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    newfile = new File(directory, "a~"+fileName);

                    if(currentFile.exists()){

                        InputStream in = new FileInputStream(currentFile);
                        OutputStream out = new FileOutputStream(newfile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        File filex = new File(xxx);
                        boolean deleted = delete(getBaseContext(),filex);
                    }else{
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Object o) {

            new Dolist().execute();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }

    class DoSomeTasktext extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            final String[] storedir = {sharedPreferences.getString("dir", "")};
            final String s[]=storedir[0].split("`");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                File newfile;
                String xxx=getPathx(getBaseContext(),uu);
                ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                File directory = wrapper.getDir(s[fi], Context.MODE_PRIVATE);
                String z[]=xxx.split("/");
                String storepath = sharedPreferences.getString(s[fi], "");
                if(storepath.equals(""))
                {
                    editor.putString(s[fi],xxx);
                    editor.commit();
                }
                else
                {
                    storepath=storepath+"`"+xxx;
                    editor.putString(s[fi],storepath);
                    editor.commit();
                }


                File file = new File(directory,z[z.length-1]);
                try {
                    File currentFile = new File(xxx);
                    String fileName = z[z.length-1];
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    newfile = new File(directory, "t~"+fileName);

                    if(currentFile.exists()){

                        InputStream in = new FileInputStream(currentFile);
                        OutputStream out = new FileOutputStream(newfile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        File filex = new File(xxx);
                        boolean deleted = delete(getBaseContext(),filex);
                    }else{
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Object o) {
            new Dolist().execute();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }

    class DoSomeTaskvideo extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            final String[] storedir = {sharedPreferences.getString("dir", "")};
            final String s[]=storedir[0].split("`");



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                for(int i=0;i<selectionResult.size();i++) {
                    File newfile;
                    String xxx = selectionResult.get(i);
                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File directory = wrapper.getDir(s[fi], Context.MODE_PRIVATE);
                    String z[] = xxx.split("/");
                    String storepath = sharedPreferences.getString(s[fi], "");
                    if (storepath.equals("")) {
                        editor.putString(s[fi], xxx);
                        editor.commit();
                    } else {
                        storepath = storepath + "`" + xxx;
                        editor.putString(s[fi], storepath);
                        editor.commit();
                    }


                    File file = new File(directory, z[z.length - 1]);
                    try {

                        File currentFile = new File(xxx);
                        String fileName = z[z.length - 1];

                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        newfile = new File(directory, "v~" + fileName);

                        if (currentFile.exists()) {

                            InputStream in = new FileInputStream(currentFile);
                            OutputStream out = new FileOutputStream(newfile);
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            in.close();
                            out.close();
                            File filex = new File(xxx);
                            boolean deleted = delete(getBaseContext(), filex);
                        } else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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
            new Dolist().execute();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }



    class DoSomeTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            final String[] storedir = {sharedPreferences.getString("dir", "")};
            final String s[]=storedir[0].split("`");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                int counta=picturesSelected.size();
                xxxx = new String[counta];
                for (int il = 0; il < counta; il++) {


                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    Uri imageUri=Uri.fromFile(new File(picturesSelected.get(il).getPath()+""));

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    xxxx[il] =picturesSelected.get(il).getPath()+"";
                    File directory = wrapper.getDir(s[fi], Context.MODE_PRIVATE);
                    String z[]=xxxx[il].split("/");
                    File file = new File(directory,"p~"+z[z.length-1]);
                    String storepath = sharedPreferences.getString(s[fi], "");
                    if(storepath.equals(""))
                    {
                        editor.putString(s[fi],xxxx[il]);
                        editor.commit();
                    }
                    else
                    {
                        storepath=storepath+"`"+xxxx[il];
                        editor.putString(s[fi],storepath);
                        editor.commit();
                    }

                    if (!file.exists()) {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                            File filex = new File(xxxx[il]);
                            boolean deleted = delete(getBaseContext(),filex);

                        } catch (IOException e) {
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
            new Dolist().execute();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }


    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[] {
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    class DoSomeTaskimage extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            final SharedPreferences.Editor editor = sharedPreferences.edit();
            final String[] storedir = {sharedPreferences.getString("dir", "")};
            final String s[]=storedir[0].split("`");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uu);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File directory = wrapper.getDir(s[fi], Context.MODE_PRIVATE);
                    String ds=getPathx(getBaseContext(),uu);
                    String z[]=ds.split("/");
                String storepath = sharedPreferences.getString(s[fi], "");
                if(storepath.equals(""))
                {
                    editor.putString(s[fi],ds);
                    editor.commit();
                }
                else
                {
                    storepath=storepath+"`"+ds;
                    editor.putString(s[fi],storepath);
                    editor.commit();
                }

                File file = new File(directory,"p~"+z[z.length-1]);
                    if (!file.exists()) {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                            File filex = new File(ds);
                            boolean deleted = delete(getBaseContext(),filex);

                        } catch (IOException e) {
                            e.printStackTrace();
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

            new Dolist().execute();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }
    }





    class Dolist extends AsyncTask {

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        protected Object doInBackground(Object[] objects) {
            sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor =sharedPreferences.edit();
            String z= sharedPreferences.getString("dir", "");
            String df[]=z.split("`");
            foldername=new String[df.length];
            foldername=z.split("`");

             ContextWrapper cvw = new ContextWrapper(getApplicationContext());
              int f=foldername.length;
              checkposition=new boolean[f];
              radioButtons=new CheckBox[f];
              Arrays.fill(checkposition,false);
              folderitems=new String[f];
              imagefolder=new Bitmap[f];

            for (int i = 0; i < f; i++) {
                File direvctory = cvw.getDir(foldername[i], Context.MODE_PRIVATE);
                File[] imageLists = direvctory.listFiles();
                folderitems[i] = imageLists.length + "";
                String zx = folderitems[i];

                if (!(zx.equals("0"))) {
                    String dfg[]=imageLists[0].getName().split("~");
                    if(dfg[0].equals("v"))
                    {
                        try {

                            Bitmap b=retriveVideoFrameFromVideo(imageLists[0].getPath());


                            imagefolder[i]=getResizedBitmap(b);
                            /*imagefolder[i]=Bitmap.createScaledBitmap(
                                    b, 120, 120, false);*/

                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                    else if(dfg[0].equals("p")) {
                        imagefolder[i] = getResizedBitmap(BitmapFactory.decodeFile(imageLists[0].getAbsolutePath()));
                    /*Bitmap.createScaledBitmap(
                                BitmapFactory.decodeFile(imageLists[0].getAbsolutePath()), 120, 120, false);*/
                    }
                    else if(dfg[0].equals("a"))
                    {
                        imagefolder[i] = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(MainActivitym.this.getResources(), R.drawable.audd), 120, 120, false);
                    }
                    else if(dfg[0].equals("t"))
                    {
                        imagefolder[i] = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(MainActivitym.this.getResources(), R.drawable.docr), 120, 120, false);
                    }

                } else {
                    imagefolder[i] = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(MainActivitym.this.getResources(), R.drawable.images), 120, 120, false);
                }
                }

            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Object o) {
               haa=1;
                gridView.removeAllViewsInLayout();
                gridView.setAdapter(new ImageAdapter(getBaseContext(), foldername, folderitems, imagefolder));
            super.onPostExecute(o);
        }
    }

    int zxc=0;
    int zr=0;

    class y1 extends AsyncTask
    {
        SharedPreferences.Editor editor;
        String dir[];
        @Override
        protected void onPreExecute() {
           editor = sharedPreferences.edit();
           zxc=1;
            final String storedir = sharedPreferences.getString("dir", "");
            dir = storedir.split("`");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            new Dolist().execute();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            for(int i=0;i<checkposition.length;i++) {

                if(checkposition[i]) {
                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File xc = wrapper.getDir(dir[i], Context.MODE_PRIVATE);
                    File[] z=xc.listFiles();
                    for (int j=0;j<z.length;j++)
                    {
                        z[j].delete();
                    }
                    SharedPreferences settings = MainActivitym.this.getSharedPreferences("main", Context.MODE_PRIVATE);
                    settings.edit().remove(dir[i]).commit();
                    xc.delete();
                }

            }
            String finals="";
            for(int i=0;i<checkposition.length;i++)
            {
                if(!checkposition[i])
                {
                    if(!finals.equals("")) {
                        finals =finals+"`"+dir[i];
                    }
                  else
                    {
                        finals=dir[i];
                    }

                }
            }

            editor.putString("dir",finals);
            editor.commit();
            return null;
        }
    }


    class y11 extends AsyncTask
    {
        SharedPreferences.Editor editor;
        String dir[];
        @Override
        protected void onPreExecute() {
            editor = sharedPreferences.edit();
            zxc=1;
            final String storedir = sharedPreferences.getString("dir", "");
            dir = storedir.split("`");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            new Dolist().execute();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            for(int i=0;i<checkposition.length;i++) {

                if(etra[i]) {
                    ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                    File xc = wrapper.getDir(dir[i], Context.MODE_PRIVATE);
                    File[] z=xc.listFiles();
                    for (int j=0;j<z.length;j++)
                    {
                        z[j].delete();
                    }
                    SharedPreferences settings = MainActivitym.this.getSharedPreferences("main", Context.MODE_PRIVATE);
                    settings.edit().remove(dir[i]).commit();
                    xc.delete();
                }

            }
            String finals="";
            for(int i=0;i<checkposition.length;i++)
            {
                if(!etra[i])
                {
                    if(!finals.equals("")) {
                        finals =finals+"`"+dir[i];
                    }
                    else
                    {
                        finals=dir[i];
                    }

                }
            }

            editor.putString("dir",finals);
            editor.commit();
            return null;
        }
    }


    public class ImageAdapter extends BaseAdapter {
         Context context;
         String[] folder;
        String[] dis;
        Bitmap[] im;
        LayoutInflater inflater;


        public ImageAdapter(Context context, String[] folder,String[] dis,Bitmap[] im) {
            this.context = context;
            this.folder=folder;
            this.dis=dis;
            this.im=im;
            inflater = (LayoutInflater.from(context));
        }

        @SuppressLint({"SetTextI18n", "ViewHolder"})
        public View getView(final int position, View convertView, ViewGroup parent) {

                convertView = inflater.inflate(R.layout.gridlist, null);


                TextView imageView = (TextView) convertView
                        .findViewById(R.id.foldername);
                TextView imageViewz = (TextView) convertView
                        .findViewById(R.id.folderitem);
                ImageView imageView1=(ImageView)convertView
                        .findViewById(R.id.imageofgrid);

                radioButtons[position]=(CheckBox)convertView
                .findViewById(R.id.radiobutton);
                imageView.setText(foldername[position]);
                imageViewz.setText( folderitems[position] + " File(s)");
                imageView1.setImageBitmap(imagefolder[position]);

              LinearLayout listView=(LinearLayout) convertView.findViewById(R.id.xcv);

              listView.setOnLongClickListener(new View.OnLongClickListener() {
                  @Override
                  public boolean onLongClick(View v) {
                      fme.collapseImmediately();
                      if (Build.VERSION.SDK_INT >= 26) {
                          vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                      }
                      else
                      {
                          vibrator.vibrate(200);
                      }
                      fme.setVisibility(View.INVISIBLE);
                      unhide.setVisibility(View.VISIBLE);
                      delete.setVisibility(View.VISIBLE);
                      if(onlycheck==0) {
                          for (int i = 0; i < checkposition.length; i++) {
                              if (radioButtons[i] != null) {
                                  radioButtons[i].setVisibility(View.VISIBLE);
                              }
                          }
                      }
                      x=1;
                      onlycheck=1;
                      return false;
                  }
              });


               listView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       fme.collapse();
                           if (onlycheck == 1 ) {
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
                               Intent i = new Intent(MainActivitym.this, viewall.class);
                               i.putExtra("posi", position + "");
                               startActivity(i);
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

            if(onlycheck==1)
            {
                radioButtons[position].setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return folder.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
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
    protected void onResume() {
        new Dolist().execute();
        super.onResume();

    }

    int x=0;
    @Override
    public void onBackPressed() {

        if(x==1) {
            x=0;
            fme.setVisibility(View.VISIBLE);
            unhide.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
            onlycheck = 0;
            for (int i = 0; i < checkposition.length; i++) {
                if (radioButtons[i] != null) {
                    checkposition[i]=false;
                    radioButtons[i].setVisibility(View.INVISIBLE);
                }
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NewApi")
    public static String getPathx(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}

