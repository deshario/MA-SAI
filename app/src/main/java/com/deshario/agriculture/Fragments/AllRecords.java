package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.deshario.agriculture.Adapters.RecordAdapter;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllRecords extends Fragment implements FullScreenDialogContent {

    GridView gridview;
    List<Records> allItems;
    RecordAdapter customAdapter;
    Context context;

    public AllRecords() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_cardview, container, false);
        context = container.getContext();
        gridview = (GridView)view.findViewById(R.id.gridview);
        allItems = Records.getAllRecords();
        customAdapter = new RecordAdapter(context,allItems);
        gridview.setAdapter(customAdapter);
        return view;
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }
}
