package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.deshario.agriculture.Adapters.RecordAdapter;
import com.deshario.agriculture.Models.DateValue;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        List<Records> abc = Records.getDataBy_date_n_Type("2017-08",1);
        Calendar calendar = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,2017);
        cal.set(Calendar.MONTH,Calendar.AUGUST);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("NumDays = "+maxDay);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        boolean alldates[] = new boolean[maxDay];
        ArrayList<DateValue> dateValues = new ArrayList<>();
        DateValue dv;
        // 0 1 2 3 4 5 6 7 8 9 10
        // 1 2 3 4

        ArrayList<String> dates = new ArrayList<>();
        for (int i=0; i<maxDay; i++){
            dv = new DateValue();
            cal.set(Calendar.DAY_OF_MONTH,i+1);
            String date = df.format(cal.getTime());
            dates.add(i,date);
        }

        ArrayList<String> found_dates = new ArrayList<>();
        for(int i=0; i<abc.size(); i++){
            String my_date = abc.get(i).getData_created();
            found_dates.add(i,my_date);
        }

        boolean data[][] = new boolean[dates.size()][found_dates.size()];
        String [][] list = new String[dates.size()][found_dates.size()];

        for(int i=0; i<dates.size(); i++){
            for(int j=0; j<found_dates.size(); j++){
                if(dates.get(i).equals(found_dates.get(j))){
                    System.out.println(i+":"+j+": "+dates.get(i) +" == "+found_dates.get(j));
                    data[i][j] = true;
                    list[i][j] = found_dates.get(j);
                }else{
                    data[i][j] = false;
                    list[i][j] = dates.get(i);
                    System.out.println(i+":"+j+": "+dates.get(i)+" Not Found !");
                }
            }
        }



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
