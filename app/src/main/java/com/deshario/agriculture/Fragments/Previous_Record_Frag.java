package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Previous_Record_Frag extends Fragment {

    private Context context;
    private ListView list_view;
    private String all_items[] = {"รายได้","ค่าใช้จ่าย","กำไร","หนี้สิน/เงินกู้","ชำระหนี้สิน/เงินกู้","คงเหลือ","บันทึกย่อ","วันที่"};
    private ArrayList<String> values = new ArrayList<>();
    private SimpleAdapter phone_adapter;
    private List<HashMap<String, String>> aList;
    private static List<Records> records;

    public Previous_Record_Frag() {
        setRetainInstance(true);
    }

    TextView date1,date2,note_txt;
    TextView income, expense, profit, debt, pay_debt, total_remain;
    TextView income_val, expense_val, profit_val, debt_val, pay_debt_val, total_remain_val;
    RoundCornerProgressBar prog_income,prog_expense,prog_profit,prog_debt,prog_paydebt,prog_remain;
    ImageButton btn_refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cardview_manual, container, false);
        context = container.getContext();
        bindview(view);
        work();
        return view;
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
        //ArrayList<String> amounts = Records.getCustom("data_amount",record.getCategory());
        //System.out.println("datas size is == "+datas.size());
        //System.out.println("Amount is == "+amounts);

        for(int i=0; i< records.size(); i++){
            Double balance = records.get(i).getData_amount();
            int dbalance = records.get(i).getCategory().getCat_type();
            remain += balance;
        }

        //System.out.println(" Total : "+remain);

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
            // System.out.println("Amounts : "+amounts.get(j));
            total_amounts += Double.valueOf(amounts.get(j));
        }
        // System.out.println("Total Amounts : "+total_amounts);

        // System.out.println("Income : "+income);
        // System.out.println("expenses : "+expenses);
        // System.out.println("profit : "+profit);
        // System.out.println("debt : "+debt);
        // System.out.println("pay_debts : "+pay_debts);
        // System.out.println("remain : "+remain);

        String note = record.getShortnote();
        //String date = today.format(record.getData_recorded());
        Date date = record.getData_recorded();

        // System.out.println("Date : "+remain);

        return new String[]{
                String.valueOf(income), // [0]
                String.valueOf(expenses), // [1]
                String.valueOf(profit), // [2]
                String.valueOf(debt), // [3]
                String.valueOf(pay_debts), // [4]
                String.valueOf(remain), // [5]
                String.valueOf(note), // [6]
                String.valueOf(date) // [7]
        };
    }

    public void bindview(View view){
        date1 = (TextView)view.findViewById(R.id.date1);
        date2 = (TextView)view.findViewById(R.id.date2);
        note_txt = (TextView)view.findViewById(R.id.note);

        income = (TextView) view.findViewById(R.id.item1);
        expense = (TextView) view.findViewById(R.id.item2);
        profit = (TextView) view.findViewById(R.id.item3);
        debt = (TextView) view.findViewById(R.id.item4);
        pay_debt = (TextView) view.findViewById(R.id.item5);
        total_remain = (TextView) view.findViewById(R.id.item6);

        income_val = (TextView) view.findViewById(R.id.amount1);
        expense_val = (TextView) view.findViewById(R.id.amount2);
        profit_val = (TextView) view.findViewById(R.id.amount3);
        debt_val = (TextView) view.findViewById(R.id.amount4);
        pay_debt_val = (TextView) view.findViewById(R.id.amount5);
        total_remain_val = (TextView) view.findViewById(R.id.amount6);

        prog_income = (RoundCornerProgressBar)view.findViewById(R.id.progress1);
        prog_expense = (RoundCornerProgressBar)view.findViewById(R.id.progress2);
        prog_profit = (RoundCornerProgressBar)view.findViewById(R.id.progress3);
        prog_debt = (RoundCornerProgressBar)view.findViewById(R.id.progress4);
        prog_paydebt = (RoundCornerProgressBar)view.findViewById(R.id.progress5);
        prog_remain = (RoundCornerProgressBar)view.findViewById(R.id.progress6);


        btn_refresh = (ImageButton) view.findViewById(R.id.refresh_btn);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work();
            }
        });
    }

    public void work(){
        String thb = "\u0E3F";

        income.setText(all_items[0]);
        expense.setText(all_items[1]);
        profit.setText(all_items[2]);
        debt.setText(all_items[3]);
        pay_debt.setText(all_items[4]);
        total_remain.setText(all_items[5]);

        boolean records_exists  = Records.records_exists();
        System.out.println("Record Exists : "+records_exists);
        if(records_exists == false){ // No record
            String dates[] = getDefault_date();
            String day = dates[0];
            String month = dates[1];
            String year = dates[2];
            income_val.setText(thb+"0.00");
            expense_val.setText(thb+"0.00");
            profit_val.setText(thb+"0.00");
            debt_val.setText(thb+"0.00");
            pay_debt_val.setText(thb+"0.00");
            total_remain_val.setText(thb+"0.00");
            date1.setText(day);
            date2.setText(month+" "+year);
            prog_income.setProgress(0);
            prog_expense.setProgress(0);
            prog_profit.setProgress(0);
            prog_debt.setProgress(0);
            prog_paydebt.setProgress(0);
            prog_remain.setProgress(0);
        }else{
            Records temp = Records.getLatestSingleRecordById();
            String datas [] = calculation(temp);
            String date_ = datas[7];
            Date date = new Date(date_);
            String dates[] = getThaiDate(date);
            String day = dates[0];
            String month = dates[1];
            String year = dates[2];
            income_val.setText(thb+datas[0]);
            expense_val.setText(thb+datas[1]);
            profit_val.setText(thb+datas[2]);
            debt_val.setText(thb+datas[3]);
            pay_debt_val.setText(thb+datas[4]);
            total_remain_val.setText(thb+datas[5]);
            note_txt.setText("บันทึกย่อ : "+datas[6]);
            date1.setText(day);
            date2.setText(month+" "+year);
        }
    }

    private String[] getDefault_date(){
        String full_thaidate = null;
        Date today = Calendar.getInstance().getTime();
        String DATE_FORMAT_NOW = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(today);
        String[] output = stringDate.split("-");

        int _day = Integer.valueOf(output[0]);
        int _month = Integer.valueOf(output[1]);
        int _year = Integer.valueOf(output[2]);

        int day_ = _day;
        String month_ = Th_Months(_month-1,true);
        int year_ = Th_Year(_year);
        full_thaidate = day_+" "+month_+" "+year_;

        return new String[]{
                String.valueOf(day_),
                month_,
                String.valueOf(year_),
                full_thaidate
        };
    }

    public String[] getThaiDate(Date date){ //Sat Jul 29 00:00:00 GMT+07:00 2017
        String full_thai_date = null;
        String DATE_FORMAT_NOW = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(date);
        String[] output = stringDate.split("-");
        //System.out.println("transformed date : "+stringDate);

        int _day = Integer.valueOf(output[0]);
        int _month = Integer.valueOf(output[1]);
        int _year = Integer.valueOf(output[2]);

        int day_ = _day;
        String month_ = Th_Months(_month-1,true);
        int year_ = Th_Year(_year);

        full_thai_date = day_+" "+month_+" "+year_;

        return new String[]{
                String.valueOf(day_),
                month_,
                String.valueOf(year_),
                full_thai_date
        };
    }

    public String Th_Months(int month, boolean full){
        String[] th_months_short = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
        };
        String[] th_months_long = new String[]{
                "มกราคม",
                "กุมภาพันธ์",
                "มีนาคม",
                "เมษายน",
                "พฤษภาคม",
                "มิถุนายน",
                "กรกฎาคม",
                "สิงหาคม",
                "กันยายน",
                "ตุลาคม",
                "พฤศจิกายน",
                "ธันวาคม"
        };
        if(full == true){
            return th_months_long[month];
        }else{
            return th_months_short[month];
        }
    }

    public int Th_Year(int en_year){
        return en_year+543;
    }

}






