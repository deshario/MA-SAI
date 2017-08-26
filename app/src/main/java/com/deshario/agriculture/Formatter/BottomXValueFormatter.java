package com.deshario.agriculture.Formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Deshario on 8/24/2017.
 */

public class BottomXValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public BottomXValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if(value > 0) {
            return "฿"+mFormat.format(value);
        } else {
            //return "฿0.00";
            return "";
        }
    }
}
