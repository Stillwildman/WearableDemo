package com.vincent.wearabledemo.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.vincent.wearabledemo.fragment.BasicNotificationFragment;
import com.vincent.wearabledemo.R;
import com.vincent.wearabledemo.adapter.SectionsPagerAdapter;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements
        ActionBar.TabListener,
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    SectionsPagerAdapter pagerAdapter;
    ViewPager viewPager;

    private static final String COUNT_KEY = "VincentWear";
    private GoogleApiClient gac;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        pagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                    .setText(pagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        gac = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            Log.i("RequestCode", "" + requestCode);
            Uri uri = data.getData();
            ContentResolver CR = this.getContentResolver();
            try {
                Bitmap image = BitmapFactory.decodeStream(CR.openInputStream(uri));

                BasicNotificationFragment basic = BasicNotificationFragment.instance;
                basic.notification_bigPicSand(image);

                Log.i("ActivityResult", "" + image.getByteCount());
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("File Not Found!", e.getMessage());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}