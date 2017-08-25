package com.deshario.agriculture.Formatter;

/**
 * Created by Deshario on 8/24/2017.
 */
import com.deshario.agriculture.Fragments.Income_Per_Date_Frag;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.util.ArrayList;

public class XAxisValueFormatter implements IAxisValueFormatter{

    ArrayList<String> mDate = new ArrayList<>();

    @Override
    public String getFormattedValue(float value, AxisBase axis){

        String[] th_months = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
        };

        ArrayList<String> my_months = new ArrayList<>();
        ArrayList<String> my_day = new ArrayList<>();
        ArrayList<String>[] all_data = Income_Per_Date_Frag.getDate();
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
}