package com.bashirmanafikhi.whatsappvideostatuses.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bashirmanafikhi.whatsappvideostatuses.Models.MyApp;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private Context context;
    private List<MyApp> apps;
    private LayoutInflater mInflater;
    //private ItemClickListener mClickListener;

    // data is passed into the constructor
    public AppsAdapter(Context context, List<MyApp> data) {

        Collections.shuffle(data);

        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.apps = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_app, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyApp app = apps.get(position);
        holder.myTextView.setText(app.Name);

        Picasso.get()
                .load(apps.get(position).ImagePath)
                //.placeholder(R.drawable.ic_video_placeholder)
                .into(holder.myImageView);



    }

    // total number of rows
    @Override
    public int getItemCount() {
        return apps.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.appName);
            myImageView = itemView.findViewById(R.id.appImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            MyApp app = apps.get(getAdapterPosition());

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(app.PlayStoreLink));
            context.startActivity(intent);
        }
    }

    // convenience method for getting data at click position
    MyApp getItem(int id) {
        return apps.get(id);
    }

    /*// allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }*/
}