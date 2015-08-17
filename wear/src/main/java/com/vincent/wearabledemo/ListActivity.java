package com.vincent.wearabledemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;

public class ListActivity extends Activity implements WearableListView.ClickListener {

    String[] dataSet = {"Sample Item 01", "Sample Item 02", "Sample Item 03", "Sample Item 04", "Sample Item 05"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_layout);

        WearableListView listView = (WearableListView) findViewById(R.id.wearable_list);

        listView.setAdapter(new ListAdapter(this, dataSet));
        listView.setClickListener(this);
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder)
    {
        Integer tag = (Integer) viewHolder.itemView.getTag();
        Log.i("What the Hell", "" + tag);
    }

    @Override
    public void onTopEmptyRegionClick() {}

}
