package com.vincent.wearabledemo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vincent.wearabledemo.R;

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
