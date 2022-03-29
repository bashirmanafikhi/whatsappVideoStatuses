package com.bashirmanafikhi.whatsappvideostatuses.Helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Tushar on 6/30/2017.
 */

public class PermissionCheck{
    public static boolean readAndWriteExternalStorage(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }else{
            return true;
        }

    }

    public static boolean audioRecord(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 2);
            return false;
        }else{
            return true;
        }

    }

    public static boolean readAndWriteContacts(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 3);
            return false;
        }else{
            return true;
        }

    }

    public static boolean vibrate(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.VIBRATE}, 4);
            return false;
        }else{
            return true;
        }

    }

    public static boolean sendSms(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.SEND_SMS}, 5);
            return false;
        }else{
            return true;
        }

    }



    //====================
    public static void requestExternalStoragePermissions(Context context) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions(context) && hasWritePermissions(context)) {
            return;
        }

        ActivityCompat.requestPermissions((AppCompatActivity) context,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1); // your request code
    }

    public static boolean hasReadPermissions(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean hasWritePermissions(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    //Just like this you can implement rest of the permissions.
}
