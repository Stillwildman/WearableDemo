package com.vincent.wearabledemo.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vincent.wearabledemo.R;

public class AdvancedFeature extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static AdvancedFeature newInstance(int sectionNumber)
    {
        AdvancedFeature fragmentInstance = new AdvancedFeature();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragmentInstance.setArguments(args);

        return fragmentInstance;
    }

    public AdvancedFeature() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_advanced_feature, container, false);

        return rootView;
    }
}
