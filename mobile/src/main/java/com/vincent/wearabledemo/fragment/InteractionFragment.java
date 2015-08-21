package com.vincent.wearabledemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.vincent.wearabledemo.R;
import com.vincent.wearabledemo.activity.MapActivity;

public class InteractionFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String DATA_KEY = "Brack";
    private static final String DATA_PATH = "/demo";
    private static final String DATA_COUNT_PATH = "/count";
    private static final String DATA_REFRESH_KEY = "REFRESHING";
    private static int DATA_REFRESH_COUNT = 0;

    private GoogleApiClient gac;
    private int count = 0;

    public static InteractionFragment newInstance(int sectionNumber)
    {
        InteractionFragment fragmentInstance = new InteractionFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragmentInstance.setArguments(args);

        return fragmentInstance;
    }

    public InteractionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_interaction, container, false);

        Button syncBtn = (Button) rootView.findViewById(R.id.syncButton);
        Button mapBtn = (Button) rootView.findViewById(R.id.mapButton);
        Button listBtn = (Button) rootView.findViewById(R.id.wearableListButton);
        Button pickerBtn = (Button) rootView.findViewById(R.id.pickerButton);

        gac = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseCounter();
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                openFunction("map");
                //increaseCounter();
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFunction("list");
            }
        });

        pickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFunction("picker");
            }
        });

        return rootView;
    }

    // Create a data map and put data in it!
    private void increaseCounter()
    {
        if (gac.isConnected())
        {
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(DATA_COUNT_PATH);

            putDataMapReq.getDataMap().putInt(DATA_KEY, count++);

            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            Wearable.DataApi.putDataItem(gac, putDataReq);

            Log.i("DataItemPut!", "" + count);
        }
    }

    private void openFunction(String key)
    {
        if (gac.isConnected())
        {
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(DATA_PATH);

            putDataMapReq.getDataMap().putInt(DATA_REFRESH_KEY, DATA_REFRESH_COUNT++);
            putDataMapReq.getDataMap().putString(DATA_KEY, key);

            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            Wearable.DataApi.putDataItem(gac, putDataReq);

            Log.d("REFRESH COUNT", "" + DATA_REFRESH_COUNT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        gac.connect();
        Log.d("GAC_Status", "GAC Connected!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        gac.disconnect();
        Log.d("GAC_Status", "GAC Disconnected!!");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("GAC_Status", "onConnected: " + bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GAC_Status", "onDisconnected: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GAC_Status", "ConnectionFailed: " + connectionResult);
    }
}
