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
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.vincent.wearabledemo.R;

public class WearActivity extends Activity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private TextView debugText;

    private static final String DATA_KEY = "Brack";
    private static final String DATA_PATH = "/demo";

    private GoogleApiClient gac;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                debugText = (TextView) stub.findViewById(R.id.debugText);
            }
        });

        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    public void testBtnClick(View v)
    {
        //Intent intent = new Intent(this, AmbientActivity.class);
        //Intent intent = new Intent(this, CardActivity.class);
        //Intent intent = new Intent(this, ListActivity.class);
        //Intent intent = new Intent(this, PickerActivity.class);
        //Intent intent = new Intent(this, PathPromptActivity.class);
        Intent intent = new Intent(this, ConfirmActivity.class);

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

    public void textUpdate(final int count)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                debugText.setText(String.valueOf(count));
            }
        });
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
                    textUpdate(dataMap.getInt(DATA_KEY));
                }
            }
            else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.i("Item Deleted!", "DELETED!!!!!!!");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gac.connect();
        Log.d("WearGAC_Status", "GAC Connected!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(gac, this);
        gac.disconnect();
        Log.d("WearGAC_Status", "GAC Disconnected!!");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(gac, this);
        Log.d("WearGAC_Status", "onConnected: " + bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("WearGAC_Status", "onDisconnected: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("WearGAC_Status", "ConnectionFailed: " + connectionResult);
    }
}
