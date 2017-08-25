package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deshario.agriculture.Formatter.YAxisValueFormatter;
import com.deshario.agriculture.Formatter.BottomXValueFormatter;
import com.deshario.agriculture.Formatter.XAxisValueFormatter;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankChartFragment extends Fragment {

    protected BarChart mChart;
    String toolbar_title;
    ImageView tool_imageView;

   // int numberOfPoints;
    Calendar cal;
    //int maxDay;
    int datas[] = new int[100];
    SimpleDateFormat df;
    int maxval;
    public static ArrayList<String> previous_8days;


    public BlankChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_blank_chart, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        tool_imageView = (ImageView)toolbar.findViewById(R.id.opt_menu);
        tool_imageView.setImageResource(R.drawable.ic_refresh_white_24dp);
        tool_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChart.invalidate();
                mChart.notifyDataSetChanged();
                while(!mChart.isFullyZoomedOut()){
                    mChart.zoomOut();
                }
                mChart.animateXY(3000,3000);
            }
        });
        Bundle bundle = getArguments();
        toolbar_title = bundle.getString("title1");
        textView.setText(toolbar_title);

        mChart = (BarChart)view.findViewById(R.id.chart1);
        work();

        return view;
    }

    public void work(){

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
       // xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setValueFormatter(new XAxisValueFormatter());
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xLabel.get((int)value);
//            }
//        });

        IAxisValueFormatter custom = new YAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        // leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
//        rightAxis.setDrawGridLines(false);
//        //rightAxis.setTypeface(mTfLight);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(4f);

//       Comment upside
//        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart


        dbwork();

        // mChart.setDrawLegend(false);
    }

    private void setData(int previous_week_data[]) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i=0; i<previous_week_data.length; i++) {
            yVals1.add(new BarEntry(i, previous_week_data[i]));
        }

        BarDataSet set1;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, toolbar_title);
            set1.setDrawIcons(false);
            //set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColors(getResources().getColor(R.color.primary_deshario));
            set1.setValueFormatter(new BottomXValueFormatter());

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f); // font of chart value labels
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            mChart.setData(data);
        }
        mChart.animateXY(3000, 3000);
        mChart.setHighlightFullBarEnabled(true);
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
        //String ha = getCalculatedDate("yyyy-MM-dd", -7);
        //System.out.println("ha :: "+ha);
    }

    public static ArrayList<String>[] getDate(){
        ArrayList<String> months = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        Date date = null;
        ArrayList<String> previous_8_days = previous_8days;
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

            months.add(i,Th_Months(month));
            days.add(i,String.valueOf(day));

        }

        ArrayList<String>[] lists = (ArrayList<String>[])new ArrayList[2];
        lists[0] = months;
        lists[1] = days;

        return lists;
    }

    private void dbwork(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        previous_8days = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        //System.out.println("today : "+sdf.format(cal.getTime()));
        cal.add(Calendar.DAY_OF_YEAR, -8); // get starting date
        for(int i = 0; i<8; i++){ // loop adding one day in each iteration
            cal.add(Calendar.DAY_OF_YEAR, 1);
            String date = sdf.format(cal.getTime());
            previous_8days.add(i,date);
        }

       String first_date = previous_8days.get(0);
       String last_date = previous_8days.get(previous_8days.size()-1);

        List<Records> abc = Records.getLast8DaysData(first_date,last_date,3);
        ArrayList<String> found_dates = new ArrayList<>();
        for(int a=0; a<abc.size(); a++){
            String my_date = abc.get(a).getData_created();
            found_dates.add(a,my_date);
        }

        System.out.println("previous_8days : "+previous_8days);
        System.out.println("found_dates : "+found_dates);

        int previous_week_data[] = new int[previous_8days.size()];
        for(int c=0; c<previous_8days.size(); c++){
            boolean status = check_exists(found_dates,previous_8days.get(c));
            if(status == true){
                Records reca = Records.getSingleRecordsByDate(previous_8days.get(c));
                System.out.println(previous_8days.get(c)+" : "+status);
                System.out.println("Amount :: "+reca.getData_amount());
                Double d = new Double(reca.getData_amount());
                previous_week_data[c] = d.intValue();
            }else{
                Double d = new Double(0.0);
                previous_week_data[c] = d.intValue();
                System.out.println(previous_8days.get(c)+" : "+status);
            }
        }

        maxval = maxValue(previous_week_data);
        System.out.println("Max :: "+maxValue(previous_week_data));

        System.out.println("Values are :: "+Arrays.toString(previous_week_data));
        setData(previous_week_data);
    }

    public boolean check_exists(ArrayList<String> datalist,String keyword){
        int index = datalist.indexOf(keyword);
        if(index <= -1){
            return false;
        }else{
            return true;
        }
    }

    public int maxValue(int array[]) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return Collections.max(list);
    }

    public static String Th_Months(int month){
        String[] th_months = new String[] {
                "ม.ค","ก.พ","มี.ค","เม.ย","พ.ค","มิ.ย","ก.ค","ส.ค","ก.ย","ต.ค","พ.ย","ธ.ค"
        };
        return th_months[month-1]; // index start from 0 so must -1
    }

}