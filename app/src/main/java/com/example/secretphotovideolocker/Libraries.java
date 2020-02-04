package com.example.secretphotovideolocker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.support.v4.app.ActivityCompat;
import android.widget.Toast;



public class Libraries {
    //permission
    public static void requestPermissionStorage(final Activity context) {
        //lay hinh tu dien thoai
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //the second show request permission
            AlertDialog.Builder builderExplain = new AlertDialog.Builder(context);
            builderExplain.setCancelable(false);
            builderExplain.setMessage("This function requires access to storage");
            builderExplain.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE);

                }
            });
            builderExplain.show();
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ConstantDataManager.PERMISSION_REQUEST_CODE_EXTERNAL_STORAGE);
        }
    }

    public static void requestPermissionStorageDeny(final Activity context) {
        //deny
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //press not allow
            Toast.makeText(context, "You must allow usage of storage access to use this function", Toast.LENGTH_SHORT).show();
        } else {
            //press never show again

            AlertDialog.Builder builderExplain = new AlertDialog.Builder(context);
            builderExplain.setCancelable(false);
            builderExplain.setMessage("This function requires access to storage\n\nDo you want to enable storage access?");
            builderExplain.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    context.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

                }
            });
            builderExplain.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builderExplain.show();
        }
    }
}
