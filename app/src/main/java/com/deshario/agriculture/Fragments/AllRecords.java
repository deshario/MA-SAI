package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Adapters.RecordAdapter;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllRecords extends Fragment implements FullScreenDialogContent {

    TextView textview;
    List<Records> allRecords;
    LinearLayout linearLayout;
    Records records;


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Records> data;

    public AllRecords() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_cardview, container, false);
        GridView gridview = (GridView)view.findViewById(R.id.gridview);
        List<Records> allItems = Records.getAllRecords();
        RecordAdapter customAdapter = new RecordAdapter(getActivity(),allItems);
        gridview.setAdapter(customAdapter);
        return view;
    }

    public static String getThaiDate(Date date){
        String thai_date = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateFormat thai1 = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("TH"));
//        System.out.println("thai1 "+dateFormat.format(calendar.getTime())); //2560-07-04 19:50:05
        thai_date = thai1.format(calendar.getTime());

        return thai_date;
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
