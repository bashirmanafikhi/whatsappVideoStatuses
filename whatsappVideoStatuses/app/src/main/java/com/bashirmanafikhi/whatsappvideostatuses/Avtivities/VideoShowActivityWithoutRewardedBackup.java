
/*
package com.bashirmanafikhi.whatsappvideostatuses.Avtivities;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;



public class VideoShowActivityWithoutRewardedBackup extends AppCompatActivity {

    private AdView adView;
    private MyVideo myVideo;
    private int score = 0;

    @Override
    protected void onResume() {
        adView.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
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
                        MyClass.addLikesCount(VideoShowActivityWithoutRewardedBackup.this, myVideo);
                        btnLike.setImageResource(R.drawable.favorite_black);
                        txtLikes.setText(String.valueOf(likesCount));
                    }
                } else {
                    boolean result = db.removeFavoriteVideoByKey(myVideo.getVideoKey());
                    if (result) {

                        int likesCount = Integer.parseInt(txtLikes.getText().toString());
                        if (likesCount >= 1) {
                            likesCount--;
                            MyClass.removeLikesCount(VideoShowActivityWithoutRewardedBackup.this, myVideo);
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

                    Toast.makeText(VideoShowActivityWithoutRewardedBackup.this, R.string.videoAlreadyExist, Toast.LENGTH_SHORT).show();

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

                    MyClass.shareVid(VideoShowActivityWithoutRewardedBackup.this, myFilePath, "whatsapp");

                } else {
                    Toast.makeText(VideoShowActivityWithoutRewardedBackup.this, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
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
                    MyClass.shareVid(VideoShowActivityWithoutRewardedBackup.this, myFilePath, null);
                } else {
                    Toast.makeText(VideoShowActivityWithoutRewardedBackup.this, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
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

    private void downloadVideo() {

        MyClass.downloadVid(VideoShowActivityWithoutRewardedBackup.this, myVideo);

    }

}
*/
