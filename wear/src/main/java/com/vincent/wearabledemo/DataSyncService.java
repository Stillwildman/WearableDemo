package com.vincent.wearabledemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class DataSyncService extends WearableListenerService {

    private static final String DATA_KEY = "Brack";
    private static final String DATA_PATH = "/demo";

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
}
