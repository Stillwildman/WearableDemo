package com.vincent.wearabledemo.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.vincent.wearabledemo.R;

public class WearActivity extends Activity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String COUNT_KEY = "VincentWear";

    private GoogleApiClient gac;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

            }
        });

        gac = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void testBtnClick(View v)
    {
        //Intent intent = new Intent(this, AmbientActivity.class);
        //Intent intent = new Intent(this, CardActivity.class);
        //Intent intent = new Intent(this, ListActivity.class);
        Intent intent = new Intent(this, PickerActivity.class);

        startActivity(intent);

        //showMyNotification(intent);
    }

    private void showMyNotification(Intent intent)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity
                (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*Notification.WearableExtender notifExtender = new Notification.WearableExtender()
                .setDisplayIntent(pendingIntent)
                .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE);*/

        Notification notifBuilder = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle("Customization!")
                .extend(new Notification.WearableExtender()
                        .setContentIcon(android.R.drawable.ic_menu_camera)
                        .setDisplayIntent(pendingIntent)
                        .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE))
                .build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifBuilder);

        Log.i("Notify!!!", "DONE!!!");
    }

    // Create a data map and put data in it!
    private void increaseCounter()
    {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");

        putDataMapReq.getDataMap().putInt(COUNT_KEY, count++);

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(gac, putDataReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gac.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(gac, this);
        gac.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(gac, this);
        Log.d("GAC Status", "onConnected: " + bundle);
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
                if (items.getUri().getPath().compareTo("/count") == 0)
                {
                    DataMap dataMap = DataMapItem.fromDataItem(items).getDataMap();
                    updateCount(dataMap.getInt(COUNT_KEY));
                }

            }
            else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.i("Item Deleted!", "DELETED!!!!!!!");
            }
        }
    }

    private void updateCount(int count)
    {
        Log.i("Update Count", "" + count);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GAC Status", "onDisconnected: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GAC Status", "ConnectionFailed: " + connectionResult);
    }
}
