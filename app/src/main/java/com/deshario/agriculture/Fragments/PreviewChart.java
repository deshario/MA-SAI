package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.provider.Settings;
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

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewChart extends Fragment {

    private LineChartView chart;
    private PreviewLineChartView previewChart;
    private LineChartData data;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

    int numberOfPoints;
    Calendar cal;
    int maxDay;
    int datas[] = new int[100];
    SimpleDateFormat df;
    int maxval;

    /**
     * Deep copy of data.
     */
    private LineChartData previewData;


    public PreviewChart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preview_line_chart, container, false);


        chart = (LineChartView) rootView.findViewById(R.id.chart);
        previewChart = (PreviewLineChartView) rootView.findViewById(R.id.chart_preview);

        // Generate data for previewed chart and copy of that data for preview chart.
        work();

        chart.setLineChartData(data);
        // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
        // zoom/scroll is unnecessary.
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);

        previewChart.setZoomEnabled(true);
        previewChart.setScrollEnabled(true);

        previewChart.setLineChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());
        chart.setValueSelectionEnabled(hasLabelForSelected);
        chart.setViewportCalculationEnabled(false);

        previewX(false);

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

        List<Records> abc = Records.getDataBy_date_n_Type("2017-08",1);
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

        int maxval = maxValue(month_datas);
        System.out.println("Max :: "+maxValue(month_datas));

        System.out.println("Values are :: "+Arrays.toString(month_datas));
        List<PointValue> values = new ArrayList<PointValue>();
        for(int d=0; d<month_datas.length; d++){
            datas[d] = month_datas[d];
            values.add(new PointValue(d,  datas[d]));
        }

        generateDefaultData(values);
    }


    private void generateDefaultData(List<PointValue> values) {

        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setHasPoints(false);// too many values so don't draw points.

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        data = new LineChartData(lines);
        data.setAxisXBottom(new Axis());
        data.setAxisYLeft(new Axis().setHasLines(true));

        // prepare preview data, is better to use separate deep copy for preview chart.
        // Set color to grey to make preview area more visible.
        previewData = new LineChartData(data);
        previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);

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

    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        if (animate) {
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        } else {
            previewChart.setCurrentViewport(tempViewport);
        }
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart.
            chart.setCurrentViewport(newViewport);
        }

    }

}
