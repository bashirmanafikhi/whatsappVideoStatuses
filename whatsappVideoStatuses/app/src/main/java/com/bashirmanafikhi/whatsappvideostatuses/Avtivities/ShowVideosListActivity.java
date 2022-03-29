package com.bashirmanafikhi.whatsappvideostatuses.Avtivities;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirmanafikhi.whatsappvideostatuses.Adapters.VideoAdapter;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ShowVideosListActivity extends AppCompatActivity {

    private AdView adView;

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
        setContentView(R.layout.activity_show_videos_list);


        final MyVideo myVideo = (MyVideo) getIntent().getSerializableExtra("video");


        ArrayList<MyVideo> myData = (ArrayList<MyVideo>) getIntent().getSerializableExtra("VideosList");
        String title = (String) getIntent().getSerializableExtra("Title");
        setTitle(title);

        // ad
        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // initialization
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_videos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        VideoAdapter myAdapter = new VideoAdapter(recyclerView, this,myData);
        recyclerView.setAdapter(myAdapter);



    }
}
