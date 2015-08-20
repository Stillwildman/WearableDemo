package com.vincent.wearabledemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

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
