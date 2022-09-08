package com.bashirmanafikhi.whatsappvideostatuses.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bashirmanafikhi.whatsappvideostatuses.Adapters.NotesAdapter;
import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.MainActivity;
import com.bashirmanafikhi.whatsappvideostatuses.Interfaces.ILoadMore;
import com.bashirmanafikhi.whatsappvideostatuses.Interfaces.VolleyCallBack;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.bashirmanafikhi.whatsappvideostatuses.Adapters.VideoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */

public class TabVideos extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private VideoAdapter myAdapter;
    private ArrayList<MyVideo> myData;
    private RecyclerView recyclerView;
    //private int oldestPostIndex;
    //private final int SCROLE_ITEMS_COUNT = 4;
    //private final int FIRST_ITEMS_COUNT = 6;
    private ArrayList<MyVideo> MyNewDataList;
    private LinearLayout videosCountLayout;
    private TextView videosCount;

    public TabVideos() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_videos, container, false);


        // initialization
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_videos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        myData = new ArrayList<MyVideo>();
        myAdapter = new VideoAdapter(recyclerView, getActivity(), myData);
        recyclerView.setAdapter(myAdapter);
        videosCountLayout = (LinearLayout) view.findViewById(R.id.VideosCountLayout);
        videosCount = (TextView) view.findViewById(R.id.VideosCount);


        MyNewDataList = new ArrayList<>();
        int sectionNumber = getArguments().getInt("section_number", 2);

        //getData(sectionNumber);
        setCategoryName(sectionNumber);


        //videosCount.setText(String.valueOf(MyNewDataList.size()));
        //checkSort();

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColor,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        // this will start on create
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                loadData();

                /*if (MyNewDataList.size() > 0)
                    loadData();
                else
                    loadNotes();*/

            }
        });

        return view;
    }


    String categoryName;

    private void setCategoryName(int sectionNumber) {

        switch (sectionNumber) {
            case 2:
                categoryName = "IslamicVid";
                break;
            case 3:
                categoryName = "LoveVid";
                break;
            case 4:
                categoryName = "ComedyVid";
                break;
            case 5:
                categoryName = "SadVid";
                break;
            case 6:
                categoryName = "RomanticVid";
                break;
            case 7:
                categoryName = "InspirationVid";
                break;
            case 8:
                categoryName = "WrittenVid";
                break;
            case 9:
                categoryName = "WisdomVid";
                break;
            case 10:
                categoryName = "DifferentVid";
                break;
            default:
                categoryName = "";
        }
    }



    @Override
    public void onRefresh() {
        current_page = 1;

        myData.removeAll(myData);
        myData.clear();
        myAdapter.notifyDataSetChanged();
        myAdapter.setLoaded();

        //checkSort();
        loadData();

    }

    private ArrayList<MyVideo> loadData() {


        videosCountLayout.setVisibility(View.VISIBLE);

        recyclerView.setAdapter(myAdapter);

        try {
            getPaginatedVideos(categoryName, current_page, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    current_page++;
                    myAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });


            /*for (int i=0; i<FIRST_ITEMS_COUNT && i<MyNewDataList.size(); i++){
                myData.add(MyNewDataList.get(i));
                oldestPostIndex = i;
            }*/


            onScrole();

        } catch (Exception ex) {
            //Toast.makeText(getActivity(), "أعد المحاولة مرة أخرى", Toast.LENGTH_SHORT).show();
        }

        return myData;
    }

    private void onScrole() {
        //set load more event

        myAdapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if (current_page <= page_limit) {


                    // Adding load more spinner
                    MyVideo loadingItem = new MyVideo();
                    loadingItem.setVideoTitle("?Loading");
                    myData.add(loadingItem);
                    myAdapter.notifyItemInserted(myData.size() - 1);




                    // insert videos
                    getPaginatedVideos(categoryName, current_page, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {

                            current_page++;
                            myAdapter.notifyDataSetChanged();
                            myAdapter.setLoaded();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });


                } else
                    Toast.makeText(getActivity(), R.string.EndOfCategory, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNotes() {
        videosCountLayout.setVisibility(View.GONE);
        String[] myNotes = getResources().getStringArray(R.array.notes_array);
        NotesAdapter adapter = new NotesAdapter(getActivity(), myNotes);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    int current_page = 1;
    int page_limit;
    int videos_count;

    private void getPaginatedVideos(String category, int page, final VolleyCallBack callBack) {

        //final ArrayList<MyVideo> videos = new ArrayList<MyVideo>();

        RequestQueue requestQueue;
        final String websiteUrl = "https://www.videostatuses.com";
        String url = websiteUrl + "/scripts/Videos.php?page=" + page + "&limit=" + 10 + "&category=" + category + "&sortType=" + MainActivity.sortType;
        requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    // Removing load more spinner
                    if (myData.size() > 0) {
                        myData.remove(myData.size() - 1);
                        myAdapter.notifyItemRemoved(myData.size());
                    }


                    JSONArray jsonArray = response.getJSONArray("videos");
                    page_limit = response.getInt("page_limit");
                    videos_count = response.getInt("videos_count");


                    videosCount.setText(String.valueOf(videos_count));


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


                        myData.add(video);
                        //oldestPostIndex = i;


                        //MyData.allVideos.add(video);
                        //videos.add(video);

                    }

                    // insert banner ad
                    MyVideo bannerItem = new MyVideo();
                    bannerItem.setVideoTitle("?Banner");
                    myData.add(bannerItem);
                    myAdapter.notifyItemInserted(myData.size() - 1);



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



    private void checkSort() {
        if (MainActivity.sortType.equals("Random"))
            Collections.shuffle(MyNewDataList);
        else if (MainActivity.sortType.equals("New"))
            Collections.sort(MyNewDataList);
        else if (MainActivity.sortType.equals("Views"))
            Collections.sort(MyNewDataList, MyVideo.VideoViewsComparator);
        else if (MainActivity.sortType.equals("Downloads"))
            Collections.sort(MyNewDataList, MyVideo.VideoDownloadsComparator);
        else if (MainActivity.sortType.equals("Likes"))
            Collections.sort(MyNewDataList, MyVideo.VideoLikesComparator);
    }

}
