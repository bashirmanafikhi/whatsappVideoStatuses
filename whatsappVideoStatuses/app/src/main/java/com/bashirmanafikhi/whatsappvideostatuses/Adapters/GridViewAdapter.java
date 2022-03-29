package com.bashirmanafikhi.whatsappvideostatuses.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.VideoShowActivity;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    List<MyVideo> listSource;
    Context mContext;


    public GridViewAdapter(List<MyVideo> listSource, Context mContext) {
        this.listSource = listSource;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listSource.size();
    }

    @Override
    public Object getItem(int i) {
        return listSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ImageView imageView;
        if(view == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,200));
            //imageView.setPadding(5,5,5,5);
            imageView.setBackgroundColor(mContext.getResources().getColor(R.color.primaryTextColor));

            // showing the image of the video
            Picasso.get()
                    .load(listSource.get(i).getImageUrl())
                    //.placeholder(R.drawable.ic_video_placeholder)
                    .into(imageView);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, VideoShowActivity.class);
                    intent.putExtra("video", listSource.get(i));
                    mContext.startActivity(intent);

                    ((Activity)mContext).finish();
                }
            });
        }else{
            imageView = (ImageView) view;
        }
        return imageView;
    }
}
