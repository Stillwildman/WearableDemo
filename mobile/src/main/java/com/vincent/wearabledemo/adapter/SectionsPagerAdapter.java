package com.vincent.wearabledemo.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vincent.wearabledemo.fragment.AdvancedFeature;
import com.vincent.wearabledemo.fragment.BasicNotificationFragment;
import com.vincent.wearabledemo.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return BasicNotificationFragment.newInstance(position + 1);

            case 1:
                return AdvancedFeature.newInstance(position + 1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return getString(R.string.Notify_Basic);
            case 1:
                return getString(R.string.Notify_Advanced);
        }
        return null;
    }
}
