package com.main.schat.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by XuanVinh on 14/10/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider{
    private int tabIcons[] = {R.drawable.friend, R.drawable.chat, R.drawable.group, R.drawable.setting};
//    private int tabIcons1[] = {R.drawable.friend_fade, R.drawable.chat, R.drawable.group_fade, R.drawable.setting_fade};
//    private int tabIcons2[] = {R.drawable.friend_fade, R.drawable.chat_fade, R.drawable.group, R.drawable.setting_fade};
//    private int tabIcons3[] = {R.drawable.friend_fade, R.drawable.chat_fade, R.drawable.group_fade, R.drawable.setting};
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
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }




}
