package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.deshario.agriculture.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankChartFragment extends Fragment {


    public BlankChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_blank_chart, container, false);
        return view;
    }

}
