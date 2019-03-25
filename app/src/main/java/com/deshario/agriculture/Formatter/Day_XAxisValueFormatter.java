package com.deshario.agriculture.Formatter;

/**
 * Created by Deshario on 8/24/2017.
 */
import com.deshario.agriculture.Config.Deshario_Functions;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Day_XAxisValueFormatter implements IAxisValueFormatter{

    ArrayList<String> mDate = new ArrayList<>();

    @Override
    public String getFormattedValue(float value, AxisBase axis){

        String[] th_months = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
        };

        ArrayList<String> my_months = new ArrayList<>();
        ArrayList<String> my_day = new ArrayList<>();
//        ArrayList<String>[] all_data = Income_Per_Date_Frag.getDate();
        ArrayList<String>[] all_data = getDate();
        my_months = all_data[0];
        my_day = all_data[1];

        //System.out.println("my_months : "+my_months);
        //System.out.println("my_day : "+my_day);


        for(int i=0; i<th_months.length; i++){ // length 12
            mDate.add(i, th_months[i]);
        }

        String stringValue; // value 0-7

        if (mDate.size() >= 0 && value >= 0) {
            if (value < mDate.size()) {
//                stringValue = mDate.get((int) value);
                stringValue = my_day.get((int) value)+" "+my_months.get((int) value);
            } else {
                stringValue = "";
            }
        }else {
            stringValue = "";
        }

        return stringValue;
    }

    public ArrayList<String>[] getDate(){
        ArrayList<String> months = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        Date date = null;
        ArrayList<String> previous_8_days = getPreviousDays();
        for(int i=0; i<previous_8_days.size(); i++){
            String dates = previous_8_days.get(i);
            try{
                DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                date = (Date) parser.parse(dates);
            }catch (Exception e){
                System.out.println("Error : "+e);
            }

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1; //Note: +1 the month for current month
            int day = c.get(Calendar.DAY_OF_MONTH);

            months.add(i, Deshario_Functions.Th_Months(month,false));
            days.add(i,String.valueOf(day));

        }

        ArrayList<String>[] lists = (ArrayList<String>[])new ArrayList[2];
        lists[0] = months;
        lists[1] = days;

        return lists;
    }

    public ArrayList<String> getPreviousDays(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> previous_8days = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        //System.out.println("today : "+sdf.format(cal.getTime()));
        cal.add(Calendar.DAY_OF_YEAR, -8); // get starting date
        for(int i = 0; i<8; i++){ // loop adding one day in each iteration
            cal.add(Calendar.DAY_OF_YEAR, 1);
            String date = sdf.format(cal.getTime());
            previous_8days.add(i,date);
        }
        return previous_8days;
    }
}