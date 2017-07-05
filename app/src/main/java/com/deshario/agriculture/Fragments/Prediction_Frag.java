package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.deshario.agriculture.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Prediction_Frag extends Fragment{

    ListView list_view;
    String[] contactlists = new String[]{"รายได้คาดการณ์","ค่าใช้จ่ายคาดการณ์","กำไร - ขาดทุน"};
    String numberslists[] = {"101","102","103"};
    SimpleAdapter phone_adapter;

    public Prediction_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.main_today_frag, container, false);
        list_view = (ListView)view.findViewById(R.id.listview);

        dowork();
        return view;
    }

    public void dowork(){
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        int lengther = contactlists.length;
        for (int i = 0; i < lengther; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", contactlists[i]+" : ");
            hm.put("listview_image", Integer.toString(R.drawable.ic_attach_money_green_48dp));
            hm.put("listview_value", "0 บาท");
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_value"};
        int[] to = {R.id.listview_image, R.id.title, R.id.value};

        phone_adapter = new SimpleAdapter(getContext(), aList, R.layout.main_custom_listview, from, to);
        list_view.setAdapter(phone_adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   caller(position+1);
            }
        });
    }

}
