package com.deshario.agriculture.Formatter;

/**
 * Created by Deshario on 8/24/2017.
 */
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class YAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public YAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return "฿ "+mFormat.format(value);
    }
}