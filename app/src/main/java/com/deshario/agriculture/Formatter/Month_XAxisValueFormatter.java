package com.deshario.agriculture.Formatter;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Deshario on 8/26/2017.
 */

public class Month_XAxisValueFormatter implements IAxisValueFormatter {

    protected String[] mMonths = new String[]{
            "ม.ค","ก.พ","มี.ค","เม.ย","พ.ค","มิ.ย","ก.ค","ส.ค","ก.ย","ต.ค","พ.ย","ธ.ค"
    };

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int days = (int) value;
        return mMonths[days];
    }


}
