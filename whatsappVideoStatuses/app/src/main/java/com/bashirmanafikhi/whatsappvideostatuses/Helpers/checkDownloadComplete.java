package com.bashirmanafikhi.whatsappvideostatuses.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class checkDownloadComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "تم التحميل", Toast.LENGTH_SHORT).show();


        // there is a wrong in this code! the application is been closed when end the downloading
        /*String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = manager.query(query);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        String file = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                        Toast.makeText(context, "تم التحميل بنجاح", Toast.LENGTH_SHORT).show();
                    } else {
                        int message = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                        Toast.makeText(context, "فشل التحميل!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }*/
    }

}
