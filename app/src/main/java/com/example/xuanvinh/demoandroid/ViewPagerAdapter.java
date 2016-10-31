package com.example.xuanvinh.demoandroid;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by XuanVinh on 14/10/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    String[] mtitle={"Tab1","Tab2","Tab3","Tab4"};
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg  = null;
        switch(position)
        {
            case 0:
                frg = new Tab1Fragment();
                break;
            case 1:
                frg = new Tab2Fragment();
                break;
            case 2:
                frg = new Tab3Fragment();
                break;
            case 3:
                frg = new Tab4Fragment();
                break;
        }
        return frg;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mtitle[position];
    }
}
