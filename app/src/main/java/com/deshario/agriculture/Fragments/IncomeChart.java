package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeChart extends Fragment {

    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;


    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;
    int numberOfPoints;
    Calendar cal;
    int maxDay;
    int datas[] = new int[100];
    SimpleDateFormat df;
    int maxval;



    public IncomeChart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
        chart = (LineChartView) rootView.findViewById(R.id.chart);
      //  chart.setOnValueTouchListener(new ValueTouchListener());

        work();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);
        chart.setValueSelectionEnabled(hasLabelForSelected);

        resetViewport();
        return rootView;
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = maxval+100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    public void work(){

        SimpleDateFormat monthonly = new SimpleDateFormat("MMM");
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,2017);
        cal.set(Calendar.MONTH,Calendar.AUGUST);

        maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        numberOfPoints = maxDay;

        System.out.println(monthonly.format(cal.getTime())+" :: "+maxDay+" Days");
        df = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<String> dates = new ArrayList<>();
        for (int b=0; b<maxDay; b++){
            cal.set(Calendar.DAY_OF_MONTH,b+1);
            String date = df.format(cal.getTime());
            dates.add(b,date);
        }

        List<Records> abc = Records.getDataBy_date_n_Type("2017-08",3);
        ArrayList<String> found_dates = new ArrayList<>();
        for(int a=0; a<abc.size(); a++){
            String my_date = abc.get(a).getData_created();
            found_dates.add(a,my_date);
        }

        int month_datas[] = new int[dates.size()];
        for(int c=0; c<dates.size(); c++){
            boolean status = check_exists(found_dates,dates.get(c));
            if(status == true){
                Records reca = Records.getSingleRecordsByDate(dates.get(c));
                System.out.println(dates.get(c)+" : "+status);
                System.out.println("Amount :: "+reca.getData_amount());
                Double d = new Double(reca.getData_amount());
                month_datas[c] = d.intValue();

            }else{
                Double d = new Double(0.0);
                month_datas[c] = d.intValue();
                //System.out.println(dates.get(i)+" : "+status);
            }
        }

        maxval = maxValue(month_datas);
        System.out.println("Max :: "+maxValue(month_datas));

        System.out.println("Values are :: "+Arrays.toString(month_datas));
        List<PointValue> values = new ArrayList<PointValue>();
        for(int d=0; d<month_datas.length; d++){
            datas[d] = month_datas[d];
            values.add(new PointValue(d,  datas[d]));
        }
        int e=0;

        generateData(values);
    }

    private void generateData(List<PointValue> values) {

            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }


        List<Line> lines = new ArrayList<Line>();

            Line line = new Line(values);


            line.setColor(ChartUtils.COLOR_GREEN);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);

            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            //line.setHasGradientToTransparent(hasGradientToTransparent);
            if (pointsHaveDifferentColor){
//                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                line.setPointColor(ChartUtils.COLOR_ORANGE);
            }
            lines.add(line);



        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("จำนวนระยะเวลา : เดือน");
                axisY.setName("จำนวนเงิน : บาท");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    public int maxValue(int array[]) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return Collections.max(list);
    }

    public boolean check_exists(ArrayList datalist,String keyword){
        int index = datalist.indexOf(keyword);
        return (index == -1?false:true);
    }

}
