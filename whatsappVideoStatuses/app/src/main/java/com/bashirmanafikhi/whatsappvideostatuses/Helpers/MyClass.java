package com.bashirmanafikhi.whatsappvideostatuses.Helpers;


import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.AboutApp;
import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.AboutProgrammer;
import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.ShowVideosListActivity;
import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.MainActivity;
import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.VideoShowActivity;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.android.youtube.player.YouTubeIntents;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyClass {

    public static void share(Context context){
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "شاهد أحدث حالات الواتس أب ومقاطع الفيديو اليومية وحملها مجانا على هذا التطبيق \nhttps://play.google.com/store/apps/details?id=" +  context.getPackageName());
        sendIntent.setType("text/plain");


        context.startActivity(sendIntent);

        //Toast.makeText(context, R.string.share, Toast.LENGTH_SHORT).show();
    }

    public static void rate(Context context){
        //Toast.makeText(context, R.string.rate, Toast.LENGTH_SHORT).show();

        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }

    }

    public static void goToMahwousVideos(Context context){
        //Toast.makeText(context, R.string.rate, Toast.LENGTH_SHORT).show();

        Uri uri = Uri.parse("market://details?id=com.mahwous.mahwous");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.mahwous.mahwous")));
        }

    }

    public static void goToMahwousQuotes(Context context){
        //Toast.makeText(context, R.string.rate, Toast.LENGTH_SHORT).show();

        Uri uri = Uri.parse("market://details?id=com.mahwous.mahwousquotes");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.mahwous.mahwousquotes")));
        }

    }


    public static void about(Context context){
        //Toast.makeText(context, R.string.NotProgrammedYet, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AboutApp.class);
        context.startActivity(intent);
    }
    public static void likedVideos(Context context){
        SqlDB db = new SqlDB(context);
        ArrayList<MyVideo> myData = db.GetAllFavoriteVideos();
        String title = context.getString(R.string.videosYouLiked);

        Intent intent = new Intent(context, ShowVideosListActivity.class);
        intent.putExtra("VideosList", myData);
        intent.putExtra("Title", title);
        context.startActivity(intent);
    }



    public static void goToRandomVideo(final Context context){


        Toast.makeText(context, "جاري احضار فيديو عشوائي", Toast.LENGTH_SHORT).show();

        final String websiteUrl = "https://www.videostatuses.com";
        String url = websiteUrl + "/scripts/GetRandomVideo.php";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    MyVideo video = new MyVideo();
                    video.setVideoKey(response.getString("Id"));
                    video.setVideoUrl(websiteUrl + "/videos/" + response.getString("VideoUrl"));
                    video.setVideoTitle(response.getString("VideoTitle"));
                    video.setCategory(response.getString("Category"));
                    video.setImageUrl(websiteUrl + "/images/" + response.getString("ImageUrl"));
                    video.setLikesCount(Integer.parseInt(response.getString("LikesCount")));
                    video.setVideoViewsCount(Integer.parseInt(response.getString("ViewsCount")));
                    video.setDownloadsCount(Integer.parseInt(response.getString("DownloadsCount")));


                    Intent intent = new Intent(context, VideoShowActivity.class);
                    intent.putExtra("video", video);
                    context.startActivity(intent);

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });



        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);



    }

    public static void facebook(Context context){
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context,"https://www.facebook.com/amera240/");
        facebookIntent.setData(Uri.parse(facebookUrl));
        context.startActivity(facebookIntent);

    }
    public static void youtube(Context context,String id){
        try {
            Intent intent = YouTubeIntents.createChannelIntent(context,id );
            context.startActivity(intent);
        }catch (Exception ex){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/channel/" + id));
            context.startActivity(intent);
        }

    }

    public static void feedBack(Context context){
        Toast.makeText(context, R.string.NotProgrammedYet, Toast.LENGTH_SHORT).show();

    }
    public static void privacyPolicy(Context context){
        Toast.makeText(context, R.string.NotProgrammedYet, Toast.LENGTH_SHORT).show();

    }
    public static void AboutProgrammer(Context context){
        //Toast.makeText(context, R.string.NotProgrammedYet, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, AboutProgrammer.class);
        context.startActivity(intent);
    }



    //method to get the right URL to use in the intent
    public static String getFacebookPageURL(Context context,String url) {

        String FACEBOOK_URL = url;
        String FACEBOOK_PAGE_ID = "bashir.manafikhi";

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


    public static void addViewsCount(final Context context, final MyVideo myVideo){

        RequestQueue queue = Volley.newRequestQueue(context);  // this = context
        String url = "https://www.videostatuses.com/scripts/increment.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Toast.makeText(context, error+"", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("key", myVideo.getVideoKey());
                params.put("property","views");

                return params;
            }
        };
        queue.add(postRequest);
    }

    public static void addDownloadsCount(final Context context, final MyVideo myVideo){
        RequestQueue queue = Volley.newRequestQueue(context);  // this = context
        String url = "https://www.videostatuses.com/scripts/increment.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Toast.makeText(context, error+"", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("key", myVideo.getVideoKey());
                params.put("property","downloads");

                return params;
            }
        };
        queue.add(postRequest);
    }

    public static void addLikesCount(final Context context, final MyVideo myVideo){
        RequestQueue queue = Volley.newRequestQueue(context);  // this = context
        String url = "https://www.videostatuses.com/scripts/increment.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Toast.makeText(context, error+"", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("key", myVideo.getVideoKey());
                params.put("property","likes");

                return params;
            }
        };
        queue.add(postRequest);
    }

    public static void removeLikesCount(final Context context, final MyVideo myVideo){
        RequestQueue queue = Volley.newRequestQueue(context);  // this = context
        String url = "https://www.videostatuses.com/scripts/decrement.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Toast.makeText(context, error+"", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("key", myVideo.getVideoKey());
                params.put("property","likes");

                return params;
            }
        };
        queue.add(postRequest);
    }



    public static  boolean downloadVid(final Context context, MyVideo myVideo){

        //Toast.makeText(context, R.string.startDownloading,Toast.LENGTH_SHORT).show();



        DownloadManager downloadManager;
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Video حالات واتس أب/");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        //PermissionCheck.requestExternalStoragePermissions(context);

        boolean flag;
        try{
            addDownloadsCount(context, myVideo);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(myVideo.getVideoUrl());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setMimeType("video/mp4");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle(myVideo.getVideoTitle());
            request.setDescription("جاري التحميل حالات فيديو");
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"Video حالات واتس أب/VideoStatus " + myVideo.getVideoKey() +".mp4");
            downloadManager.enqueue(request);


            flag = true;

        }catch (Exception e) {
            Toast.makeText(context, R.string.didNotDownload, Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    public static void shareVid (final Context context, String filePath, final String setpackage){
        MediaScannerConnection.scanFile(context, new String[] { filePath },

                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        shareIntent.setType("video/*");

                        if (setpackage == "whatsapp")
                            shareIntent.setPackage("com.whatsapp");

                        shareIntent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);



                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT, "تمت تحميل هذا الفيديو من تطبيق الأندرويد (حالات واتس أب فيديو)");

                        shareIntent.putExtra(
                                Intent.EXTRA_TEXT, "تمت تحميل هذا الفيديو من تطبيق الأندرويد ( حالات واتس أب فيديو )");
                        shareIntent.putExtra(
                                android.content.Intent.EXTRA_TITLE, "حالات واتس أب فيديو");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

                        context.startActivity(Intent.createChooser(shareIntent,
                                "مشاركة الفيديو.."));

                    }
                });
    }
}
