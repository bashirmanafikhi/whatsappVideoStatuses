package com.bashirmanafikhi.whatsappvideostatuses.Adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.VideoShowActivity;
import com.bashirmanafikhi.whatsappvideostatuses.Interfaces.ILoadMore;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.MyClass;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.SqlDB;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_VIDEO = 0, VIEW_TYPE_LOADING = 1, VIEW_TYPE_BANNER = 2;
    private Context ctx;
    private ArrayList<MyVideo> List_Items;
    private ILoadMore loadMore;
    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private SqlDB db;

    private InterstitialAd interstitialAd;














    // Constructor
    public VideoAdapter(Context context, ArrayList<MyVideo> myData) {
        this.List_Items = myData;
        this.ctx = context;
        db = new SqlDB(ctx);

        initializeInterstitial();
    }

    // Constructor
    public VideoAdapter(RecyclerView recyclerView, Context context, ArrayList<MyVideo> myData) {
        this.List_Items = myData;
        this.ctx = context;
        db = new SqlDB(ctx);


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });

        initializeInterstitial();
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_VIDEO) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.card_video, parent, false);
            return new VideoAdapter.VideoViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.card_loading, parent, false);
            return new VideoAdapter.LoadingViewHolder(view);
        } else if (viewType == VIEW_TYPE_BANNER) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.card_banner, parent, false);
            return new VideoAdapter.BannerViewHolder(view);
        }
        return null;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VideoViewHolder) {

            final VideoViewHolder videoHolder = (VideoViewHolder) holder;


            int viewsCount = List_Items.get(position).getVideoViewsCount();
            videoHolder.txtViews.setText(String.valueOf(viewsCount));

            // مو ظابطة مشان موضوع زيادة عدد اللايكات
            /*if (viewsCount > 1000)
                videoHolder.txtViews.setText(viewsCount/1000 + " " + "ألف");
            else
                videoHolder.txtViews.setText(String.valueOf(viewsCount));*/


            videoHolder.txtTitle.setText(String.valueOf(List_Items.get(position).getVideoTitle()));
            Picasso.get()
                    .load(List_Items.get(position).getImageUrl())
                    .placeholder(R.drawable.ic_video_placeholder)
                    .into(videoHolder.thumbnailImageView);


            videoHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + List_Items.get(position).getVideoKey() + ".mp4";
                    File fileWithinMyDir = new File(myFilePath);
                    if (fileWithinMyDir.exists()) {

                        Toast.makeText(ctx, R.string.videoAlreadyExist, Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(ctx, R.string.startDownloading, Toast.LENGTH_SHORT).show();
                        MyClass.downloadVid(ctx, List_Items.get(position));

                    }
                }
            });


            videoHolder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + List_Items.get(position).getVideoKey() + ".mp4";
                    File fileWithinMyDir = new File(myFilePath);
                    if (fileWithinMyDir.exists()) {

                        MyClass.shareVid(ctx, myFilePath, null);

                    } else {

                        Toast.makeText(ctx, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
                        boolean result = MyClass.downloadVid(ctx, List_Items.get(position));
                        if (result) {
                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                public void onReceive(Context ctxt, Intent intent) {
                                    // your code
                                    MyClass.shareVid(ctx, myFilePath, null);
                                }
                            };
                            ctx.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }

                    }
                }
            });


            videoHolder.btnWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String myFilePath = Environment.getExternalStorageDirectory() + "/Video حالات واتس أب/VideoStatus " + List_Items.get(position).getVideoKey() + ".mp4";
                    File fileWithinMyDir = new File(myFilePath);
                    if (fileWithinMyDir.exists()) {

                        MyClass.shareVid(ctx, myFilePath, "whatsapp");

                    } else {
                        Toast.makeText(ctx, R.string.videoWillDownloadFirst, Toast.LENGTH_SHORT).show();
                        boolean result = MyClass.downloadVid(ctx, List_Items.get(position));
                        if (result) {
                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                public void onReceive(Context ctxt, Intent intent) {
                                    // your code
                                    MyClass.shareVid(ctx, myFilePath, "whatsapp");
                                }
                            };
                            ctx.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }

                    }


                }
            });

            int likesCount = List_Items.get(position).getLikesCount();
            videoHolder.txtLikes.setText(String.valueOf(likesCount));

            // مو ظابطة - مشان موضوع زيادة عدد اللايكات
            /*if (likesCount > 1000)
                videoHolder.txtLikes.setText(likesCount/1000 + " " +"ألف");
            else
                videoHolder.txtLikes.setText(String.valueOf(likesCount));*/


            // like button
            boolean ifLiked = db.CheckIfFavorite(List_Items.get(position).getVideoKey());
            if (ifLiked) {
                videoHolder.btnLike.setImageResource(R.drawable.favorite_black);
            } else {
                videoHolder.btnLike.setImageResource(R.drawable.favorite_border_black);
            }


            videoHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean ifLiked = db.CheckIfFavorite(List_Items.get(position).getVideoKey());
                    if (!ifLiked) {
                        //Toast.makeText(ctx, "not exist in liked videos", Toast.LENGTH_SHORT).show();
                        boolean result = db.addFavoriteVideo(List_Items.get(position));
                        if (result) {
                            //Toast.makeText(ctx, "inserted to the table", Toast.LENGTH_SHORT).show();
                            int likesCount = Integer.parseInt(videoHolder.txtLikes.getText().toString());
                            likesCount++;
                            MyClass.addLikesCount(ctx, List_Items.get(position));
                            videoHolder.btnLike.setImageResource(R.drawable.favorite_black);
                            videoHolder.txtLikes.setText(String.valueOf(likesCount));
                        }
                    } else {
                        //Toast.makeText(ctx, "exist in liked videos", Toast.LENGTH_SHORT).show();
                        boolean result = db.removeFavoriteVideoByKey(List_Items.get(position).getVideoKey());
                        if (result) {
                            //Toast.makeText(ctx, "removed from the table", Toast.LENGTH_SHORT).show();
                            int likesCount = Integer.parseInt(videoHolder.txtLikes.getText().toString());
                            if (likesCount >= 1) {
                                likesCount--;
                                MyClass.removeLikesCount(ctx, List_Items.get(position));
                            }
                            videoHolder.btnLike.setImageResource(R.drawable.favorite_border_black);
                            videoHolder.txtLikes.setText(String.valueOf(likesCount));
                        }
                    }

                }
            });


            setAnimation(holder.itemView, position);
            ///////////////

        } else if (holder instanceof LoadingViewHolder) {
            /*if (position==0)
                ((LoadingViewHolder) holder).prograssBar.setVisibility(View.GONE);*/

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.prograssBar.setIndeterminate(true);
        } else if (holder instanceof BannerViewHolder) {

            String nativeAdId = ctx.getResources().getString(R.string.between_Videos_native);
            final RecyclerView.ViewHolder newHolder = holder;

            AdLoader adLoader = new AdLoader.Builder(ctx, nativeAdId)
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            // Show the ad.
                            populateNativeAdView(unifiedNativeAd, ((BannerViewHolder) newHolder).getAdView());


                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());


            //UnifiedNativeAd nativeAd = (UnifiedNativeAd) List_Items.get(position);
            //populateNativeAdView(nativeAd, ((BannerViewHolder) holder).getAdView());


        }
    }


    @Override
    public int getItemCount() {
        return List_Items.size();
        //return (null == List_Items ? List_Items.size() : 0);
    }


    public void setLoaded() {
        isLoading = false;
    }


    @Override
    public int getItemViewType(int position) {

        if (List_Items.get(position).getVideoTitle().equals("?Loading"))
            return VIEW_TYPE_LOADING;
        else if (List_Items.get(position).getVideoTitle().equals("?Banner")) {
            return VIEW_TYPE_BANNER;
        } else {
            return VIEW_TYPE_VIDEO;
        }
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }






    private void initializeInterstitial() {
        interstitialAd = new InterstitialAd(ctx);
        interstitialAd.setAdUnitId(ctx.getString(R.string.interstitial_download_video_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLeftApplication() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void openActivity(int position) {

        if (ctx.getClass().getSimpleName().equals(VideoShowActivity.class.getSimpleName())) {
            ((Activity) ctx).finish();
        }


        Intent intent = new Intent(ctx, VideoShowActivity.class);
        intent.putExtra("video", List_Items.get(position));

        ctx.startActivity(intent);
    }






    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView playButton;
        private final ImageView thumbnailImageView;
        private final TextView txtViews;
        private final TextView txtTitle;
        private final TextView txtLikes;
        private final ImageButton btnDownload;
        private final ImageButton btnWhatsApp;
        private final ImageButton btnShare;
        private final ImageButton btnLike;


        VideoViewHolder(View itemView) {

            super(itemView);
            playButton = (ImageView) itemView.findViewById(R.id.btnYoutube_player);
            playButton.setOnClickListener(this);
            thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbnailImageView);
            txtViews = (TextView) itemView.findViewById(R.id.txtViews);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtLikes = (TextView) itemView.findViewById(R.id.txtLikes);
            btnDownload = (ImageButton) itemView.findViewById(R.id.btnDownload);
            btnWhatsApp = (ImageButton) itemView.findViewById(R.id.btnWhatsapp);
            btnShare = (ImageButton) itemView.findViewById(R.id.btnShare);
            btnLike = (ImageButton) itemView.findViewById(R.id.btnLike);

        }

        @Override
        public void onClick(View v) {

            // Has the interstitial loaded successfully?
            // If it has loaded, perform these actions
            if (interstitialAd.isLoaded()) {
                // Step 1: Display the interstitial
                interstitialAd.show();
                // Step 2: Attach an AdListener
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        // Step 2.1: Load another ad
                        interstitialAd.loadAd(new AdRequest.Builder().build());

                        // Step 2.2: Start the new activity
                        openActivity(getLayoutPosition());
                    }
                });
            }
            // If it has not loaded due to any reason simply load the next activity
            else {
                openActivity(getLayoutPosition());
            }

        }


    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar prograssBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            prograssBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        /*AdView adView;

        BannerViewHolder(View itemView) {
            super(itemView);
            adView = (AdView) itemView.findViewById(R.id.adView);
        }*/

        private UnifiedNativeAdView adView;

        public UnifiedNativeAdView getAdView() {
            return adView;
        }

        BannerViewHolder(View view) {
            super(view);
            adView = (UnifiedNativeAdView) view.findViewById(R.id.ad_view);

            // The MediaView will display a video asset if one is present in the ad, and the
            // first image asset otherwise.
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

            // Register the view used for each individual asset.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }
    }
}