package com.bashirmanafikhi.whatsappvideostatuses.Adapters;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bashirmanafikhi.whatsappvideostatuses.Fragments.TabMain;
import com.bashirmanafikhi.whatsappvideostatuses.Fragments.TabVideos;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static int currentPage;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        currentPage = position+1;

        switch (position){
            case 0:
                return new TabMain();
            default:
                // the other tabs
                return newInstance(position + 1);
        }
    }

    private TabVideos newInstance(int sectionNumber) {
        TabVideos fragment = new TabVideos();
        Bundle args = new Bundle();
        args.putInt("section_number", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 10;
    }
}