package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.deshario.agriculture.Models.Category;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.lang.ref.WeakReference;
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

    public static Context context;
    static ArrayList<String> datas;

    static ListView list_view;
//    String all_items[] = {"รายได้","ค่าใช้จ่าย","กำไร - ขาดทุน","หนี้สิน/เงินกู้","ชำระหนี้สิน/เงินกู้","คงเหลือ"};
    String all_items[] = {"รายได้","ค่าใช้จ่าย","กำไร","หนี้สิน/เงินกู้","ชำระหนี้สิน/เงินกู้","คงเหลือ","บันทึกย่อ","วันที่"};

    ArrayList<String> values = new ArrayList<String>();

    public SimpleAdapter phone_adapter;
    List<HashMap<String, String>> aList;

    public static List<Records> records;
    public List<Records> joiner;
    public List<Records> debt_records;

    public Main_Today_Frag() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_today_frag, container, false);
        context = container.getContext();
        list_view = (ListView)view.findViewById(R.id.listview);
        dowork();
        Calendar cal = Calendar.getInstance();
        System.out.println("Calendar : "+cal.getTime());


        return view;
    }


    public void dowork(){
        boolean records_exists  = Records.records_exists();
        System.out.println("Record Exists : "+records_exists);
        if(records_exists == false){
            values.add(0,"0 บาท");
            values.add(1,"0 บาท");
            values.add(2,"0 บาท");
            values.add(3,"ค่าอุปกรณ์การเกษตร");
            values.add(4,"0 บาท");
            values.add(5,"0 บาท");
            values.add(6,"shortnote");
            values.add(7,""+new Date());
        }else{
            DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH"));
//            Records records = Records.getLatestSingleRecord();
            List<Records> records_list = Records.getAllRecords();
            for(int i=0; i<records_list.size(); i++){
                Records temp = records_list.get(i);
                String datas [] = calculation(temp);
//                System.out.println("Datas Size : "+datas.length);
                values.add(0,datas[0]+" บาท");
                values.add(1,datas[1]+" บาท");
                values.add(2,datas[2]+" บาท");
                values.add(3,datas[3]+" บาท");
                values.add(4,datas[4]+" บาท");
                values.add(5,datas[5]+" บาท");
                values.add(6,datas[6]);
                values.add(7,datas[7]);
            }

//            values.add(0,""+records.getData_amount()+" บาท");
//            values.add(1,""+records.getData_amount()+" บาท");
//            values.add(2,""+(records.getData_amount()-records.getData_amount())+" บาท");
//            values.add(3,"Type:"+records.getCategory().getCat_type());
//            values.add(4,records.getCategory().getCat_item());
//            values.add(5,""+(records.getData_amount()-records.getData_amount())+" บาท");
//            values.add(6,""+records.getShortnote());
//            values.add(7,""+today.format(records.getData_recorded()));
        }

       aList = new ArrayList<HashMap<String, String>>();
        int lengther = all_items.length;
        for (int i = 0; i < lengther; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", all_items[i]+" : ");
            hm.put("listview_image", Integer.toString(R.drawable.ic_attach_money_green_48dp));
            hm.put("listview_value", values.get(i));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_value"};
        int[] to = {R.id.listview_image, R.id.title, R.id.value};

        phone_adapter = new SimpleAdapter(context, aList, R.layout.main_custom_listview, from, to);
        list_view.setAdapter(phone_adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   caller(position+1);
            }
        });

    }

    public static void refresh(){
    }

    public static String[] calculation(Records record){
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH"));
        double income = 0.0; // รายรับ
        double expenses = 0.0; // รายจ่าย
        double profit = 0.0; // กำไร
        double debt = 0.0; // หนี้สิน
        double pay_debts = 0.0; // ชำระหนี้สิน
        double remain = 0.0; // คงเหลือ

        records = Records.getAllRecords();

        Category category = Category.getSingleCategory(2);

        for(int i=0; i< records.size(); i++){
          Double balance = records.get(i).getData_amount();
          int dbalance = records.get(i).getCategory().getCat_type();
            remain += balance;
        }
       // System.out.println(" Total : "+remain);

        int type = record.getCategory().getCat_type();
        boolean cat_topic_1 = false, cat_topic_2 = false, cat_topic_3 = false;
        switch (type){
            case 1:
                cat_topic_1 = true;
                income = 0.0; // ok // ติดลบ
                expenses = 0.0; // ok
                profit = 0.0; // ok
                debt = record.getData_amount(); //
                pay_debts = 0.0; //
                remain = remain; // ok
                break;
            case 2:
                cat_topic_2 = true;
                income = 0.0; // ok
                expenses = record.getData_amount(); // ok
                profit = 0.0; // ok
                debt = 0.0; // ok
                pay_debts = 0.0; // ok
                break;
            case 3:
                cat_topic_3 = true;
                income = record.getData_amount(); // ok
                expenses = 0.0; // ok
                profit = 0.0; //
                debt = 0.0; //
                pay_debts = 0.0; //
                remain = remain; //
                break;
                default:
        }

        System.out.println("Income : "+income);
        System.out.println("expenses : "+expenses);
        System.out.println("profit : "+profit);
        System.out.println("debt : "+debt);
        System.out.println("pay_debts : "+pay_debts);
        System.out.println("remain : "+remain);

        return new String[]{
                String.valueOf(income),
                String.valueOf(expenses),
                String.valueOf(profit),
                String.valueOf(debt),
                String.valueOf(pay_debts),
                String.valueOf(remain),
                String.valueOf(record.getShortnote()),
                String.valueOf(today.format(record.getData_recorded()))
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






