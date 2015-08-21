package com.vincent.wearabledemo.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;
import com.vincent.wearabledemo.activity.ListActivity;
import com.vincent.wearabledemo.activity.PathPromptActivity;
import com.vincent.wearabledemo.activity.PickerActivity;
import com.vincent.wearabledemo.activity.WearActivity;

public class DataSyncService extends WearableListenerService {

    private static final String DATA_KEY = "Brack";
    private static final String DATA_PATH = "/demo";
    private static final String DATA_COUNT_PATH = "/count";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("onStartCommand", "flags= " + flags + "startId= " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer)
    {
        for (DataEvent event: dataEventBuffer)
        {
            if (event.getType() == DataEvent.TYPE_CHANGED)
            {
                //DataItem Changed
                DataItem items = event.getDataItem();

                if (items.getUri().getPath().compareTo(DATA_PATH) == 0)
                {
                    DataMap dataMap = DataMapItem.fromDataItem(items).getDataMap();

                    switch (dataMap.getString(DATA_KEY)) {
                        case "map":
                            openDirection();
                            break;

                        case "list":
                            openList();
                            break;

                        case "picker":
                            openPicker();
                            break;
                    }
                }
                if (items.getUri().getPath().compareTo(DATA_COUNT_PATH) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(items).getDataMap();
                    updating(dataMap.getInt(DATA_KEY));
                }
            }
            else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.i("Item Deleted!", "DELETED!!!!!!!");
            }
        }
    }

    private void updating(int count)
    {
        Intent intent = new Intent(this, WearActivity.class);

        Bundle testData = new Bundle();
        testData.putString(DATA_KEY, String.valueOf(count));
        intent.putExtras(testData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Log.i("Update Count", "" + count);
    }

    private void openDirection()
    {
        Intent intent = new Intent(this, PathPromptActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openList()
    {
        Intent intent = new Intent(this, ListActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openPicker()
    {
        Intent intent = new Intent(this, PickerActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
