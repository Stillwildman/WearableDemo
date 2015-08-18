package com.vincent.wearabledemo.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vincent.wearabledemo.R;
import com.vincent.wearabledemo.fragment.AdvancedFeature;
import com.vincent.wearabledemo.fragment.BasicNotificationFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    private Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        this.context = context;
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
                return context.getString(R.string.Notify_Basic);
            case 1:
                return context.getString(R.string.Notify_Advanced);
        }
        return null;
    }
}
