package com.deshario.agriculture.Formatter;

/**
 * Created by Deshario on 8/24/2017.
 */
import com.deshario.agriculture.Fragments.BlankChartFragment;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyXAxisValueFormatter implements IAxisValueFormatter{

    ArrayList<String> mDate = new ArrayList<>();

    @Override
    public String getFormattedValue(float value, AxisBase axis){

        String[] th_months = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
        };

        ArrayList<String> my_months = new ArrayList<>();
        ArrayList<String> my_day = new ArrayList<>();
        ArrayList<String>[] all_data = BlankChartFragment.getDate();
        my_months = all_data[0];
        my_day = all_data[1];

        System.out.println("my_months : "+my_months);
        System.out.println("my_day : "+my_day);

        for(int i=0; i<th_months.length; i++){
            mDate.add(i, th_months[i]);
        }

        String stringValue;

        if (mDate.size() >= 0 && value >= 0) {
            if (value < mDate.size()) {
//                stringValue = mDate.get((int) value);
                stringValue = my_months.get((int) value)+" "+my_day.get((int) value);
            } else {
                stringValue = "";
            }
        }else {
            stringValue = "";
        }

        return stringValue;
    }
//
//    public String Th_Months(int month){
//        String[] th_months = new String[] {
//                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
//        };
//        return th_months[month];
//    }
//
////    public void custom_dates(){
////        String[] alldates = null;
////        ArrayList<String> previous_8_days = BlankChartFragment.previous_8days;
////        for(int i=0; i<previous_8_days.size(); i++){
////            String dates = previous_8_days.get(i);
////            String[] all_dates = getDate(dates);
////            //alldates = Arrays.copyOf(all_dates, all_dates.length);
////            System.out.println(i+" : "+all_dates);
////        }
////    }
//
//    public ArrayList<String>[] getDate(){
//        ArrayList<String> months = new ArrayList<>();
//        ArrayList<String> days = new ArrayList<>();
//        Date date = null;
//        ArrayList<String> previous_8_days = BlankChartFragment.previous_8days;
//        for(int i=0; i<previous_8_days.size(); i++){
//            String dates = previous_8_days.get(i);
//            try{
//                DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
//                date = (Date) parser.parse(dates);
//            }catch (Exception e){
//                System.out.println("Error : "+e);
//            }
//
//            Calendar c = Calendar.getInstance();
//            c.setTime(date);
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH) + 1; //Note: +1 the month for current month
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            months.add(i,Th_Months(month));
//            days.add(i,String.valueOf(day));
//
//        }
//
//        ArrayList<String>[] lists = (ArrayList<String>[])new ArrayList[10];
//        lists[0] = months;
//        lists[1] = days;
//
//        return lists;
//    }

}