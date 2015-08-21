package com.vincent.wearabledemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.vincent.wearabledemo.adapter.GridPagerAdapter;
import com.vincent.wearabledemo.R;

public class PickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_layout);

        GridViewPager gridPager = (GridViewPager) findViewById(R.id.gridPager);

        GridPagerAdapter pagerAdapter = new GridPagerAdapter(this, getFragmentManager());
        gridPager.setAdapter(pagerAdapter);
    }

}
