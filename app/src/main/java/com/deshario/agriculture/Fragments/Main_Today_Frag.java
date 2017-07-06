package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.deshario.agriculture.Adapters.CustomListAdapter;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Today_Frag extends Fragment {

    public Context context;
    static ArrayList<String> datas;

    ListView list_view;
//    String all_items[] = {"รายได้","ค่าใช้จ่าย","กำไร - ขาดทุน","หนี้สิน/เงินกู้","ชำระหนี้สิน/เงินกู้","คงเหลือ"};
    String all_items[] = {"รายได้","ค่าใช้จ่าย","กำไร","หนี้สิน/เงินกู้","ชำระหนี้สิน/เงินกู้","คงเหลือ","บันทึกย่อ","วันที่"};

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    ArrayList<String> values = new ArrayList<>();

    public SimpleAdapter phone_adapter;
    List<HashMap<String, String>> aList;

    public static List<Records> records;

    public Main_Today_Frag() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_today_frag, container, false);
//        View view = inflater.inflate(R.layout.cardview_main, container, false);
        context = container.getContext();
        list_view = (ListView)view.findViewById(R.id.listview);
        dowork();

//        Records aa = Records.getLatestSingleRecord();
//        System.out.println("AA SIZE :: "+aa.getData_amount());
//        mRecyclerView = (RecyclerView)view.findViewById(R.id.rvAllUsers);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(context);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new CustomListAdapter(getDataSet());
//        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void dowork(){
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH"));
        boolean records_exists  = Records.records_exists();
        String thb = "\u0E3F";
        System.out.println("Record Exists : "+records_exists);
        if(records_exists == false){
            values.add(0,thb+" "+"0 บาท");
            values.add(1,thb+" "+"0 บาท");
            values.add(2,thb+" "+"0 บาท");
            values.add(3,"ค่าอุปกรณ์การเกษตร");
            values.add(4,thb+" "+"0 บาท");
            values.add(5,thb+" "+"0 บาท");
            values.add(6,"shortnote");
            values.add(7,""+today.format(new Date()));
        }else{
//            List<Records> records_list = Records.getAllRecords();
            //for(int i=0; i<records_list.size(); i++){
                Records temp = Records.getLatestSingleRecord();
                String datas [] = calculation(temp);
                //System.out.println("Datas Size : "+datas.length);
                values.add(0,thb+" "+datas[0]+" บาท");
                values.add(1,thb+" "+datas[1]+" บาท");
                values.add(2,thb+" "+datas[2]+" บาท");
                values.add(3,thb+" "+datas[3]+" บาท");
                values.add(4,thb+" "+datas[4]+" บาท");
                values.add(5,thb+" "+datas[5]+" บาท");
                values.add(6,datas[6]);
                values.add(7,datas[7]);

           // }
//            ArrayList<String> amounts = Records.getCustom("data_amount",1);
//            double total_amounts = 0.0;
//            for(int j=0; j<amounts.size(); j++){
//                System.out.println("Amounts : "+amounts.get(j));
//                total_amounts += Double.valueOf(amounts.get(j));
//            }
//            System.out.println("Total Amounts : "+total_amounts);
        }

       aList = new ArrayList<HashMap<String, String>>();
        int lengther = all_items.length;
        for (int i = 0; i < lengther; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", all_items[i]);
            // hm.put("listview_image", Integer.toString(R.drawable.ic_attach_money_green_48dp));
            hm.put("listview_image", Integer.toString(R.drawable.ic_data_usage_black_24dp));
            hm.put("listview_value", values.get(i));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_value"};
        // int[] to = {R.id.listview_image, R.id.title, R.id.value};
        int[] to = {R.id.icon, R.id.itemname, R.id.amount};

//        CustomListAdapter aa = new CustomListAdapter(context,aList,R.layout.custom_main_card, from, to);
        // phone_adapter = new SimpleAdapter(context, aList, R.layout.main_custom_listview, from, to);
        phone_adapter = new SimpleAdapter(context, aList, R.layout.custom_main_card, from, to);
        list_view.setAdapter(phone_adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   caller(position+1);
            }
        });

    }


    public static String[] calculation(Records record){
        ArrayList<String> amounts = new ArrayList<String>();
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH"));
        double income = 0.0; // รายรับ
        double expenses = 0.0; // รายจ่าย
        double profit = 0.0; // กำไร
        double debt = 0.0; // หนี้สิน
        double pay_debts = 0.0; // ชำระหนี้สิน
        double remain = 0.0; // คงเหลือ

        records = Records.getAllRecords();
//        ArrayList<String> amounts = Records.getCustom("data_amount",record.getCategory());
        //System.out.println("datas size is == "+datas.size());
      //  System.out.println("Amount is == "+amounts);

        for(int i=0; i< records.size(); i++){
          Double balance = records.get(i).getData_amount();
          int dbalance = records.get(i).getCategory().getCat_type();
            remain += balance;
        }
       // System.out.println(" Total : "+remain);

        int type = record.getCategory().getCat_type();
        boolean cat_topic_1 = false, cat_topic_2 = false, cat_topic_3 = false;


        double total_amounts = 0.0;



        switch (type){
            case 1:
                //amounts = Records.getCustom("data_amount",1);
                income = 0.0; // ok // ติดลบ
                expenses = 0.0; // ok
                profit = income-expenses; // ok
                debt = record.getData_amount(); //
                pay_debts = 0.0; //
                remain = debt-pay_debts; // ok
                break;
            case 2:
              //  amounts = Records.getCustom("data_amount",2);
                income = 0.0; // ok
                expenses = record.getData_amount(); // ok
                profit = income-expenses; // ok
                debt = 0.0; // ok
                pay_debts = 0.0; // ok
                remain = debt-pay_debts; // ok
                break;
            case 3:
               // amounts = Records.getCustom("data_amount",3);
                income = record.getData_amount(); // ok
                expenses = 0.0; // ok
                profit = income-expenses; //
                debt = 0.0; //
                pay_debts = 0.0; //
                remain = debt-pay_debts; // ok
                break;
                default:
        }

        for(int j=0; j<amounts.size(); j++){
//            System.out.println("Amounts : "+amounts.get(j));
            total_amounts += Double.valueOf(amounts.get(j));
        }
//        System.out.println("Total Amounts : "+total_amounts);

       // System.out.println("Income : "+income);
       // System.out.println("expenses : "+expenses);
       // System.out.println("profit : "+profit);
       // System.out.println("debt : "+debt);
       // System.out.println("pay_debts : "+pay_debts);
       // System.out.println("remain : "+remain);

        String note = record.getShortnote();
        String date = today.format(record.getData_recorded());

       // System.out.println("Date : "+remain);

        return new String[]{
                String.valueOf(income),
                String.valueOf(expenses),
                String.valueOf(profit),
                String.valueOf(debt),
                String.valueOf(pay_debts),
                String.valueOf(remain),
                String.valueOf(note),
                String.valueOf(date)
        };
    }

    public void calculation(){
        Double income = 0.0; // รายรับ
        Double expenses = 0.0; // รายจ่าย
        Double profit = 0.0; // กำไร
        Double debt = 0.0; // หนี้สิน
        Double Pay_debts = 0.0; // ชำระหนี้สิน
        Double remain = 0.0; // คงเหลือ
    }

}






