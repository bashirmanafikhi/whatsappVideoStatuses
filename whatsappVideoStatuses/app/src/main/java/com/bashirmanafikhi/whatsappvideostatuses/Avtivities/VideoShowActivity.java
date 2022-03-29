package com.bashirmanafikhi.whatsappvideostatuses.Avtivities;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bashirmanafikhi.whatsappvideostatuses.Adapters.VideoAdapter;
import com.bashirmanafikhi.whatsappvideostatuses.Interfaces.VolleyCallBack;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.MyClass;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.SqlDB;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class VideoShowActivity extends AppCompatActivity /*implements RewardedVideoAdListener*/{

    private RewardedVideoAd mRewardedVideoAd;
    private MyVideo myVideo;
    private ArrayList<MyVideo> otherVideos;

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);

        // Rewarded Ad
        MobileAds.initialize(this, getString(R.string.rewarded_download_video));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(VideoShowActivity.this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardedVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                MyClass.downloadVid(VideoShowActivity.this, myVideo);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

                loadRewardedVideoAd();
            }
        });
        loadRewardedVideoAd();

        myVideo = (MyVideo) getIntent().getSerializableExtra("video");
        setTitle(myVideo.getVideoTitle());

        // add views count
        MyClass.addViewsCount(this, myVideo);


        // 22
        VideoView vv = (VideoView) findViewById(R.id.myVideoView);
        Uri videoUri = Uri.parse(myVideo.getVideoUrl());
        vv.setVideoURI(videoUri);

        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                        pb.setVisibility(View.GONE);
                        mp.start();
                    }
                });
            }
        });


        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vv);

        vv.setMediaController(mediaController);

        vv.start();
        //vv.stopPlayback();


        TextView titleTextView = (TextView) findViewById(R.id.videoTitleTextView);
        titleTextView.setText(myVideo.getVideoTitle());

        TextView txtViews = (TextView) findViewById(R.id.txtViews);
        txtViews.setText(String.valueOf(myVideo.getVideoViewsCount()));

        TextView txtDownloadsCount = (TextView) findViewById(R.id.txtDownloadsCount);
        txtDownloadsCount.setText(String.valueOf(myVideo.getDownloadsCount()));

        final TextView txtLikes = (TextView) findViewById(R.id.txtLikes);
        txtLikes.setText(String.valueOf(myVideo.getLikesCount()));


        ImageButton btnDownload = (ImageButton) findViewById(R.id.btnDownload);
        ImageButton btnWhatapp = (ImageButton) findViewById(R.id.btnWhatsapp);
        ImageButton btnShare = (ImageButton) findViewById(R.id.btnShare);
        final ImageButton btnLike = (ImageButton) findViewById(R.id.btnLike);


        final SqlDB db = new SqlDB(this);
        int likesCount = myVideo.getLikesCount();
        // like button
        boolean ifLiked = db.CheckIfFavorite(myVideo.getVideoKey());
        if (ifLiked) {
            btnLike.setImageResource(R.drawable.favorite_black);
        } else {
            btnLike.setImageResource(R.drawable.favorite_border_black);
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ifLiked = db.CheckIfFavorite(myVideo.getVideoKey());
                if (!ifLiked) {
                    boolean result = db.addFavoriteVideo(myVideo);
                    if (result) {
                        int likesCount = Integer.parseInt(txtLikes.getText().toString());
                        likesCount++;
                        MyClass.addLikesCount(VideoShowActivity.this, myVideo);
                        btnLike.setImageResource(R.drawable.favorite_black);
                        txtLikes.setText(String.valueOf(likesCount));
                    }
                } else {
                    boolean result = db.removeFavoriteVideoByKey(myVideo.getVideoKey());
                    if (result) {

                        int likesCount = Integer.parseInt(txtLikes.getText().toString());
                        if (likesCount >= 1) {
                            likesCount--;
                            MyClass.removeLikesCount(VideoShowActivity.this, myVideo);
                        }
                        btnLike.setImageResource(R.drawable.favorite_border_black);
                        txtLikes.setText(String.valueOf(likesCount));
                    }
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + myVideo.getVideoKey() + ".mp4";
                File fileWithinMyDir = new File(myFilePath);
                if (fileWithinMyDir.exists()) {
                    Toast.makeText(VideoShowActivity.this, R.string.videoAlreadyExist, Toast.LENGTH_SHORT).show();
                } else {
                    downloadVideo(v);
                }
            }
        });

        btnWhatapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + myVideo.getVideoKey() + ".mp4";
                File fileWithinMyDir = new File(myFilePath);
                if (fileWithinMyDir.exists()) {

                    MyClass.shareVid(VideoShowActivity.this, myFilePath, "whatsapp");

                } else {
                    Toast.makeText(VideoShowActivity.this, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
                    downloadVideo(v);
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + myVideo.getVideoKey() + ".mp4";
                File fileWithinMyDir = new File(myFilePath);
                if (fileWithinMyDir.exists()) {
                    MyClass.shareVid(VideoShowActivity.this, myFilePath, null);
                } else {
                    Toast.makeText(VideoShowActivity.this, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
                    downloadVideo(v);
                }
            }
        });


        /////////// Other videos

        otherVideos = new ArrayList<MyVideo>();
        //Collections.shuffle(MyData.allVideos);

        // insert videos
        getPaginatedVideos(myVideo.getCategory(), new VolleyCallBack() {
            @Override
            public void onSuccess() {

                int size = otherVideos.size();
                for (int i = 0; i < size; i++) {

                    if (i % 6 == 0 && i != 0) {
                        MyVideo loadingItem = new MyVideo();
                        loadingItem.setVideoTitle("?Banner");
                        otherVideos.add(i, loadingItem);

                        //continue;
                    }

                    //otherVideos.add(otherVideos.get(i));
                }


                // initialization
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_videos);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VideoShowActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);

                final VideoAdapter myAdapter = new VideoAdapter(recyclerView, VideoShowActivity.this, otherVideos);
                recyclerView.setAdapter(myAdapter);

            }
        });



    }


    private void getPaginatedVideos(String category, final VolleyCallBack callBack) {

        final ArrayList<MyVideo> videos = new ArrayList<MyVideo>();

        RequestQueue requestQueue;
        final String websiteUrl = "http://www.roseshamia.com";
        String url = websiteUrl + "/NewScripts/Videos.php?page=" + 1 + "&limit=" + 50 + "&category=" + category;
        requestQueue = Volley.newRequestQueue(VideoShowActivity.this);

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


                        otherVideos.add(video);

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

    public void showRewardedVideo() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            // you can show interstitial here
            MyClass.downloadVid(VideoShowActivity.this, myVideo);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void downloadVideo(View v){

        // show dialog first
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        showRewardedVideo();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoShowActivity.this);
        builder.setMessage(getString(R.string.AreYouWantToWatchAd)).setPositiveButton(getString(R.string.accept), dialogClickListener)
                .setNegativeButton(getString(R.string.decline), dialogClickListener).show();
    }


    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_download_video),
                new AdRequest.Builder().build());
    }
}
