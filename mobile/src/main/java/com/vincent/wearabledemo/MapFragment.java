package com.vincent.wearabledemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static  MapFragment newInstance(int sectionNumber)
    {
        MapFragment mapInstance = new MapFragment();

        Bundle arg = new Bundle();
        arg.putInt(ARG_SECTION_NUMBER, sectionNumber);
        mapInstance.setArguments(arg);

        return mapInstance;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
    }
}
