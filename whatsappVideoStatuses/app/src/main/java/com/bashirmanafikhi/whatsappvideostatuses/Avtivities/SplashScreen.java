package com.bashirmanafikhi.whatsappvideostatuses.Avtivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bashirmanafikhi.whatsappvideostatuses.Interfaces.VolleyCallBack;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.CheckInternetConnection;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashScreen extends Activity {

    private ArrayList<MyVideo> newVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*//Remove title bar
                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
//set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_splash_screen);


        // first time task
        /*final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");
                // first time task
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }*/


        // set test ads to this device
        List<String> testDeviceIds = Arrays.asList("AC3B089F91CBCB424EDC8DF245DB648E");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);


        // internet connection
        CheckInternetConnection connection;
        connection = new CheckInternetConnection(this);
        boolean connectionResult = connection.isConnecting();
        if (!connectionResult) {
            Toast.makeText(this, R.string.InternetConnection, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, R.string.retrievingData, Toast.LENGTH_SHORT).show();
        }


        Thread timer=new Thread()
        {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally
                {
                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        };
        timer.start();


        //removeData();


        /*getPaginatedVideos("LoveVid",1, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                sortCategories();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/


    }

/*
    private void removeData() {
        MyData.comedyVideos.removeAll(MyData.comedyVideos);
        MyData.differentVideos.removeAll(MyData.differentVideos);
        MyData.loveVideos.removeAll(MyData.loveVideos);
        MyData.islamicVideos.removeAll(MyData.islamicVideos);
        MyData.wisdomVideos.removeAll(MyData.wisdomVideos);
        MyData.sadVideos.removeAll(MyData.sadVideos);
        MyData.allVideos.removeAll(MyData.allVideos);

    }





    private void getVideos(final VolleyCallBack callBack) {


        RequestQueue requestQueue;
        final String websiteUrl = "https://www.videostatuses.com";
        String url = websiteUrl + "/scripts/show.php";
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("allVideos");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject responseVideo = jsonArray.getJSONObject(i);
                        MyVideo video = new MyVideo();
                        video.setVideoKey(responseVideo.getString("Id"));
                        video.setVideoUrl(websiteUrl + "/videos/" + responseVideo.getString("VideoUrl"));
                        video.setVideoTitle(responseVideo.getString("VideoTitle"));
                        video.setCategory(responseVideo.getString("Category"));
                        video.setImageUrl(websiteUrl + "/images/" + responseVideo.getString("ImageUrl"));
                        video.setLikesCount(Integer.parseInt(responseVideo.getString("LikesCount")));
                        video.setVideoViewsCount(Integer.parseInt(responseVideo.getString("ViewsCount")));
                        video.setDownloadsCount(Integer.parseInt(responseVideo.getString("DownloadsCount")));

                        MyData.allVideos.add(video);
                    }

                    callBack.onSuccess();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        requestQueue.add(jsonObjectRequest);
    }


    private void getPaginatedVideos(String category, int page, final VolleyCallBack callBack) {

        final ArrayList<MyVideo> videos = new ArrayList<MyVideo>();


        final String websiteUrl = "https://www.videostatuses.com";
        String url = websiteUrl + "/scripts/GetVideosCount.php";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("videos");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject responseVideo = jsonArray.getJSONObject(i);
                        MyVideo video = new MyVideo();
                        video.setVideoKey(responseVideo.getString("Id"));
                        video.setVideoUrl(websiteUrl + "/videos/" + responseVideo.getString("VideoUrl"));
                        video.setVideoTitle(responseVideo.getString("VideoTitle"));
                        video.setCategory(responseVideo.getString("Category"));
                        video.setImageUrl(websiteUrl + "/images/" + responseVideo.getString("ImageUrl"));
                        video.setLikesCount(Integer.parseInt(responseVideo.getString("LikesCount")));
                        video.setVideoViewsCount(Integer.parseInt(responseVideo.getString("ViewsCount")));
                        video.setDownloadsCount(Integer.parseInt(responseVideo.getString("DownloadsCount")));

                        MyData.allVideos.add(video);
                        videos.add(video);

                    }

                    callBack.onSuccess();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });






        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void sortCategories() {

        // sorting the categories
        for (MyVideo v : MyData.allVideos) {

            if (v.getCategory().equals("IslamicVid"))
                MyData.islamicVideos.add(v);
            else if (v.getCategory().equals("LoveVid"))
                MyData.loveVideos.add(v);
            else if (v.getCategory().equals("ComedyVid"))
                MyData.comedyVideos.add(v);
            else if (v.getCategory().equals("SadVid"))
                MyData.sadVideos.add(v);
            else if (v.getCategory().equals("WisdomVid"))
                MyData.wisdomVideos.add(v);
            else if (v.getCategory().equals("DifferentVid"))
                MyData.differentVideos.add(v);
            else if (v.getCategory().equals("RomanticVid"))
                MyData.romanticVideos.add(v);
            else if (v.getCategory().equals("WrittenVid"))
                MyData.writtenVideos.add(v);
            else if (v.getCategory().equals("InspirationVid"))
                MyData.inspirationVid.add(v);
        }
    }*/


}
