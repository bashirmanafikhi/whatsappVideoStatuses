package com.bashirmanafikhi.whatsappvideostatuses.Fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bashirmanafikhi.whatsappvideostatuses.Adapters.AppsAdapter;
import com.bashirmanafikhi.whatsappvideostatuses.Avtivities.MainActivity;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.MyClass;
import com.bashirmanafikhi.whatsappvideostatuses.Interfaces.VolleyCallBack;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyApp;
import com.bashirmanafikhi.whatsappvideostatuses.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabMain extends Fragment {


  TextView textViewVideosCount;
  AppsAdapter adapter;
  ArrayList<MyApp> apps;

  public TabMain() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

    setVideosCount();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_tab_main, container, false);




    textViewVideosCount = view.findViewById(R.id.videosCount);


    Button btnShare = view.findViewById(R.id.btnShare);
    btnShare.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MyClass.share(getActivity());
      }
    });



    Button btnMahwousVideos = view.findViewById(R.id.btnMahwousVideos);
    btnMahwousVideos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MyClass.goToMahwousVideos(getActivity());
      }
    });

    /*Button btnMahwousQuotes = view.findViewById(R.id.btnMahwousQuotes);
    btnMahwousQuotes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MyClass.goToMahwousQuotes(getActivity());
      }
    });*/

    TextView btnFacebook = view.findViewById(R.id.btnFacebook);
    btnFacebook.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        gotofacebook();
      }
    });

    TextView btnYoutube = view.findViewById(R.id.btnYoutube);
    btnYoutube.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MyClass.youtube(getActivity(),"UC1wBu4kB5v8HQbru_5Zhg_Q");
      }
    });


    TextView btnTwitter = view.findViewById(R.id.btnTwitter);
    btnTwitter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        gotoUrl("https://twitter.com/mmahwous");
      }
    });

    TextView btnInstagram = view.findViewById(R.id.btnInstagram);
    btnInstagram.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        gotoUrl("https://www.instagram.com/mmahwous");
      }
    });

    TextView btnTiktok = view.findViewById(R.id.btnTiktok);
    btnTiktok.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        gotoUrl("https://www.tiktok.com/@mmahwous");
      }
    });

//    TextView btnGooglePlay = view.findViewById(R.id.btnGooglePlay);
//    btnGooglePlay.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        gotoUrl("https://play.google.com/store/apps/dev?id=6211596474485889451");
//      }
//    });

    TextView btnTelegram = view.findViewById(R.id.btnTelegram);
    btnTelegram.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        gotoUrl("https://t.me/mmahwous");
      }
    });



    Button btnRandom = view.findViewById(R.id.btnRandom);
    btnRandom.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MyClass.goToRandomVideo(getActivity());
      }
    });


    // data to populate the RecyclerView with

//    getApps();
//
//    // set up the RecyclerView
//    adapter = new AppsAdapter(getActivity(), apps);
//
//    RecyclerView recyclerView;
//    recyclerView = view.findViewById(R.id.rvApps);
//    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//    recyclerView.setAdapter(adapter);


    return view;
  }

  public void notifyAppsSetChanged() {
    adapter.notifyDataSetChanged();
  }

  void setVideosCount(){


    final String websiteUrl = "https://www.videostatuses.com";
    String url = websiteUrl + "/scripts/GetVideosCount.php";


    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {

                  textViewVideosCount.setText(getString(R.string.videosCount) + " " + response.trim());

              }
            }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
      }
    });


    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
    requestQueue.add(stringRequest);


  }

  public void gotofacebook() {

    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
    String facebookUrl = MyClass.getFacebookPageURL(getActivity(),"https://www.facebook.com/mmahwous/");
    facebookIntent.setData(Uri.parse(facebookUrl));
    startActivity(facebookIntent);
  }

  public void gotoUrl(String url) {

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  private void getApps() {
    if (apps != null && apps.size() > 0)
      return;

    apps = new ArrayList<MyApp>();


    final String websiteUrl = "https://www.mahwous.com";
    String url = websiteUrl + "/api/apps/getall";


    // Initialize a new RequestQueue instance
    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

    // Initialize a new JsonArrayRequest instance
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONArray>() {
              @Override
              public void onResponse(JSONArray response) {
                // Do something with response

                // Process the JSON
                try{
                  // Loop through the array elements
                  for(int i=0;i<response.length();i++){
                    // Get current json object
                    JSONObject jsonApp = response.getJSONObject(i);
                    MyApp app = new MyApp();

                    // Get the current app (json object) data
                    app.Name = jsonApp.getString("name");
                    app.ImagePath = jsonApp.getString("imagePath");
                    app.Description = jsonApp.getString("description");
                    app.Package = jsonApp.getString("package");
                    app.PlayStoreLink = jsonApp.getString("playStoreLink");
                    app.AppleStoreLink = jsonApp.getString("appleStoreLink");
                    app.PlayStoreOpenedCount = jsonApp.getInt("playStoreOpenedCount");
                    app.AppleStoreOpenedCount = jsonApp.getInt("appleStoreOpenedCount");
                    app.Id = jsonApp.getInt("id");
                    app.Visible = jsonApp.getBoolean("visible");

                    apps.add(app);
                  }
                  adapter.notifyDataSetChanged();

                }catch (JSONException e){
                  e.printStackTrace();
                }
              }
            },
            new Response.ErrorListener(){
              @Override
              public void onErrorResponse(VolleyError error){
                // Do something when error occurred
                error.printStackTrace();
              }
            }
    );

    // Add JsonArrayRequest to the RequestQueue
    requestQueue.add(jsonArrayRequest);

  }
}
