package com.bashirmanafikhi.whatsappvideostatuses.Helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.MainActivity;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FBMessagingService extends FirebaseMessagingService {

    public FBMessagingService() {
        super();
    }


    private static final String TAG = "messagingService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("FCM Notification");
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());


        Log.d(TAG, "From: " + remoteMessage.getFrom());
        /*

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "key, " + key + " value " + value);
        }*/

        //Log.d("myData", "From: " + remoteMessage.getFrom());



    }
}
