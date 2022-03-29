/*
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

import com.bashirmanafikhi.whatsappvideostatuses.Adapters.VideoAdapter;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.MyClass;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.MyData;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.SqlDB;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class VideoShowActivityWithRewardedBackup extends AppCompatActivity implements RewardedVideoAdListener{

    private AdView adView;
    private RewardedVideoAd mRewardedVideoAd;
    private MyVideo myVideo;
    private int score = 0;

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        adView.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        adView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);

        // show banner
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        // Rewarded Ad
        MobileAds.initialize(this, "ca-app-pub-1685177955120006~3053789974");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(VideoShowActivityWithRewardedBackup.this);
        mRewardedVideoAd.setRewardedVideoAdListener(VideoShowActivityWithRewardedBackup.this);
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
                        MyClass.addLikesCount(VideoShowActivityWithRewardedBackup.this, myVideo);
                        btnLike.setImageResource(R.drawable.favorite_black);
                        txtLikes.setText(String.valueOf(likesCount));
                    }
                } else {
                    boolean result = db.removeFavoriteVideoByKey(myVideo.getVideoKey());
                    if (result) {

                        int likesCount = Integer.parseInt(txtLikes.getText().toString());
                        if (likesCount >= 1) {
                            likesCount--;
                            MyClass.removeLikesCount(VideoShowActivityWithRewardedBackup.this, myVideo);
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

                    Toast.makeText(VideoShowActivityWithRewardedBackup.this, R.string.videoAlreadyExist, Toast.LENGTH_SHORT).show();

                } else {
                    downloadVideo();
                }

            }
        });

        btnWhatapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + myVideo.getVideoKey() + ".mp4";
                File fileWithinMyDir = new File(myFilePath);
                if (fileWithinMyDir.exists()) {

                    MyClass.shareVid(VideoShowActivityWithRewardedBackup.this, myFilePath, "whatsapp");

                } else {
                    Toast.makeText(VideoShowActivityWithRewardedBackup.this, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
                    downloadVideo();

                }

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + myVideo.getVideoKey() + ".mp4";
                File fileWithinMyDir = new File(myFilePath);
                if (fileWithinMyDir.exists()) {
                    MyClass.shareVid(VideoShowActivityWithRewardedBackup.this, myFilePath, null);
                } else {
                    Toast.makeText(VideoShowActivityWithRewardedBackup.this, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
                    downloadVideo();
                }
            }
        });


        if (MyData.allVideos.size() == 0)
            return;




        /////////// Other videos

        ArrayList<MyVideo> otherVideos = new ArrayList<MyVideo>();
        Collections.shuffle(MyData.allVideos);

        int size = MyData.allVideos.size();
        for (int i = 0; i < 30 && i < size; i++) {

            if (i % 6 == 0 && i != 0) {
                MyVideo loadingItem = new MyVideo();
                loadingItem.setVideoTitle("?Banner");
                otherVideos.add(loadingItem);
                continue;
            }

            otherVideos.add(MyData.allVideos.get(i));
        }

        // initialization
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_videos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        VideoAdapter myAdapter = new VideoAdapter(recyclerView, this, otherVideos);
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void downloadVideo(){


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:



                        //Yes button clicked
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        } else {

                            // you can show interstitial here

                            //MyClass.downloadVid(VideoShowActivity.this, myVideo);

                        }

                        MyClass.downloadVid(VideoShowActivityWithRewardedBackup.this, myVideo);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoShowActivityWithRewardedBackup.this);
        builder.setMessage(getString(R.string.AreYouWantToWatchAd)).setPositiveButton(getString(R.string.accept), dialogClickListener)
                .setNegativeButton(getString(R.string.decline), dialogClickListener).show();
    }



    @Override
    public void onRewarded(RewardItem rewardItem) {
        score += rewardItem.getAmount();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();

        loadRewardedVideoAd();

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        //Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        //Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }



    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_download_video),
                new AdRequest.Builder().build());
    }
}
*/
