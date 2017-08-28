package com.deshario.agriculture.Formatter;

import com.deshario.agriculture.Fragments.Income_Per_Year_Frag;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Deshario on 8/29/2017.
 */

public class Years_XAxisValueFormatter implements IAxisValueFormatter {

    String[] Years = getPrevious_years().toArray(new String[getPrevious_years().size()]);

    private ArrayList<String> getPrevious_years(){
        ArrayList<String> seven_years_ago = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        calendar.add(Calendar.YEAR,-7); // 7 years ago
        for(int i=0; i<7; i++){
            calendar.add(Calendar.YEAR,1);
            String date = sdf.format(calendar.getTime());
            seven_years_ago.add(i,date);
        }
        return seven_years_ago;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int no = (int) value;
        return Years[no];
    }


}
