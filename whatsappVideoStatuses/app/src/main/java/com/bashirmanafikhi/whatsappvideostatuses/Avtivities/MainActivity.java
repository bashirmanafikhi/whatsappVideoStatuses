package com.bashirmanafikhi.whatsappvideostatuses.Avtivities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bashirmanafikhi.whatsappvideostatuses.Adapters.SectionsPagerAdapter;
import com.bashirmanafikhi.whatsappvideostatuses.Fragments.TabMain;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.MyClass;
import com.bashirmanafikhi.whatsappvideostatuses.Helpers.PermissionCheck;
import com.bashirmanafikhi.whatsappvideostatuses.Interfaces.VolleyCallBack;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyApp;
import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;
import com.bashirmanafikhi.whatsappvideostatuses.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String sortType = "Random";

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    private AdView adView;
    private ActionBarDrawerToggle mToggle;
    private FabSpeedDial fabSpeedDial;
    private DrawerLayout drawer;


    final boolean[] doubleBackToExitPressedOnce = {false};

    @Override
    public void onBackPressed() {

        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
            return;
        }


        // two pressed to exit
        if (doubleBackToExitPressedOnce[0]) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce[0] = true;
        Toast.makeText(this, getString(R.string.ClickAgainToExit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce[0] =false;
            }
        }, 2000);


    }

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
        setContentView(R.layout.activity_main);

        // This needs to be done only once
        MobileAds.initialize(this, "ca-app-pub-1685177955120006~3053789974");

        // banner
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        //PermissionCheck.requestExternalStoragePermissions(this);
        PermissionCheck.readAndWriteExternalStorage(this);


        // navigation drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer ,toolbar , R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(this,
                getString(R.string.between_Videos_native));


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != 0)
                    fabSpeedDial.show();
                else
                    fabSpeedDial.hide();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // get current page position
        int position = mViewPager.getCurrentItem();

        // fabButton options
        fabSpeedDial = (FabSpeedDial) findViewById(R.id.fabButton);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.NewFirstSortButton:
                        setSort(MainActivity.this, "New");
                        break;
                    case R.id.MostViewsSortButton:
                        setSort(MainActivity.this,"Views");
                        break;
                    case R.id.RandomSortButton:
                        setSort(MainActivity.this,"Random");
                        break;
                    case R.id.MostLikedSortButton:
                        setSort(MainActivity.this,"Likes");
                        break;
                    case R.id.MostDownloadSortButton:
                        setSort(MainActivity.this,"Downloads");
                        break;
                }
                return false;
            }

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return super.onPrepareMenu(navigationMenu);

                // هون وقت بينكبس عالزر الرئيسي
            }

        });

    }


    private  void setSort(Context context, String sortType){
        MainActivity.sortType = sortType;
        Toast.makeText(context, R.string.refreshThePage, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do whatever you need. This will be fired only when submitting.

                searchVideos(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Do whatever you need when text changes.
                // This will be fired every time you input any character.
                return false;
            }
        });

        return true;
    }

    private void searchVideos(final String keyword){

        Toast.makeText(this, getString(R.string.searching), Toast.LENGTH_SHORT).show();

        RequestQueue requestQueue;
        final String websiteUrl = "https://www.videostatuses.com";
        String url = websiteUrl + "/scripts/Videos.php?page=1&limit=100&sortType=" + MainActivity.sortType + "&keyword=" + keyword;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<MyVideo> myData = new ArrayList<MyVideo>();

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

                        myData.add(video);
                    }

                    String title = getString(R.string.search_results);
                    Intent intent = new Intent(MainActivity.this, ShowVideosListActivity.class);
                    intent.putExtra("VideosList", myData);
                    intent.putExtra("Title", title);
                    startActivity(intent);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*if (mToggle.onOptionsItemSelected(item))
            return true;*/


        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {

            case R.id.likedVideos:
                MyClass.likedVideos(this);
                return true;
            case R.id.aboutItem:
                MyClass.about(this);
                return true;
            /*case R.id.FacebookItem:
                MyClass.facebook(this);
                return true;*/
            case R.id.ShareItem:
                MyClass.share(this);
                return true;
            case R.id.rateItem:
                MyClass.rate(this);
                return true;
            /*case R.id.youtubeItem:
                MyClass.youtube(this,"UCNKwbJHaPhhnDgkQPjW9apQ");
                return true;
            case R.id.PrivacyPolicyItem:
                MyClass.privacyPolicy(this);
                return true;
            case R.id.FeedBackItem:
                MyClass.feedBack(this);
                return true;*/
            case R.id.aboutProgrammerItem:
                MyClass.AboutProgrammer(this);
                return true;
        }

        /*if (id == R.id.aboutItem) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.aboutItem) {
            MyClass.about(this);
        } else if (id == R.id.likedVideosItem) {
            MyClass.likedVideos(MainActivity.this);
        } else if (id == R.id.ShareItem) {
            MyClass.share(this);
        } else if (id == R.id.rateItem) {
            MyClass.rate(this);
        } else if (id == R.id.aboutProgrammerItem) {
            MyClass.AboutProgrammer(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
