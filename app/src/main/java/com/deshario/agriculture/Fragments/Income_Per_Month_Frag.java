package com.deshario.agriculture.Fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.deshario.agriculture.Deshario_Functions;
import com.deshario.agriculture.Formatter.BottomXValueFormatter;
import com.deshario.agriculture.Formatter.Month_XAxisValueFromatter;
import com.deshario.agriculture.Formatter.YAxisValueFormatter;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Income_Per_Month_Frag extends Fragment {

    protected BarChart mChart;
    String toolbar_title;
    public static ArrayList<String> previous_months;
    TextView avg_text;

    public Income_Per_Month_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.monthly_income_chart, container, false);


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        Bundle bundle = getArguments();
        toolbar_title = bundle.getString("title1");
        textView.setText(toolbar_title);

        Toolbar toolbar_chart = (Toolbar)view.findViewById(R.id.chart_toolbar);
        ImageButton img_refresh = (ImageButton)view.findViewById(R.id.my_refresh);
        ImageButton img_settings = (ImageButton)view.findViewById(R.id.my_setting);
        avg_text = (TextView)toolbar_chart.findViewById(R.id.monthly_avg);
        img_refresh.setImageDrawable(Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_refresh_white_24dp),
                getResources().getColor(R.color.primary_bootstrap))
        );
        img_settings.setImageDrawable(Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_settings_white_24dp),
                getResources().getColor(R.color.primary_bootstrap))
        );
        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChart.invalidate();
                mChart.notifyDataSetChanged();
                while(!mChart.isFullyZoomedOut()){
                    mChart.zoomOut();
                }
                mChart.animateXY(1000,3000);
            }
        });

        mChart = (BarChart)view.findViewById(R.id.chart1);
        work();
//        getPreviousMonths();
        return view;
    }

    public void getPreviousMonths(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        previous_months = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String today = sdf.format(cal.getTime());
        int current_month_index = Calendar.getInstance().get(Calendar.MONTH)+1; // August = 7
        cal.set(Calendar.MONTH,Calendar.JANUARY);
        for(int i=0; i<current_month_index; i++){ // loop adding one day in each iteration
            String date = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH,1);
            previous_months.add(i,date);
        }
        System.out.println("Previous Months :: "+previous_months);
    }

    public void work(){
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be drawn
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
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new Month_XAxisValueFromatter());

        IAxisValueFormatter custom = new YAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setEnabled(false);
//         leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

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
    }

    private void dbwork(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        previous_months = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String today = sdf.format(cal.getTime());
        int current_month_index = Calendar.getInstance().get(Calendar.MONTH)+1; // August = 7
        cal.set(Calendar.MONTH,Calendar.JANUARY);
        for(int i=0; i<current_month_index; i++){ // loop adding one day in each iteration
            String date = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH,1);
            previous_months.add(i,date);
        }

        String first_date = previous_months.get(0);
        String last_date = previous_months.get(previous_months.size()-1);

//        System.out.println("FirstDate :: "+first_date); // 2017-08
//        System.out.println("LastDate :: "+last_date); // 2017-01

       // setData(new int[]{10,20,30,40,50,60,70,80,90,100,110,120});
       setData(previous_months);

        // Next thing to do is get sum of each month


//        ArrayList<String> found_dates = new ArrayList<>();
//        for(int a=0; a<abc.size(); a++){
//            String my_date = abc.get(a).getData_created();
//            found_dates.add(a,my_date);
//        }
//
//        System.out.println("previous_months : "+ previous_months);
//        System.out.println("found_dates : "+found_dates);
//
//        int previous_week_data[] = new int[previous_months.size()];
//        for(int c = 0; c< previous_months.size(); c++){
//            boolean status = check_exists(found_dates, previous_months.get(c));
//            if(status == true){
//                Records reca = Records.getSingleRecordsByDate(previous_months.get(c));
//                System.out.println(previous_months.get(c)+" : "+status);
//                System.out.println("Amount :: "+reca.getData_amount());
//                Double d = new Double(reca.getData_amount());
//                previous_week_data[c] = d.intValue();
//            }else{
//                Double d = new Double(0.0);
//                previous_week_data[c] = d.intValue();
//                System.out.println(previous_months.get(c)+" : "+status);
//            }
//        }
//
//        System.out.println("Max :: "+maxValue(previous_week_data));
//
//        System.out.println("Values are :: "+Arrays.toString(previous_week_data));
//        setData(previous_week_data);
    }

    private void setData(ArrayList<String> cur_month) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        float total_summation = 0;
        for(int i=0; i<cur_month.size(); i++){
            String date = cur_month.get(i);
            float summation  = Records.getSumofEachMonth(date,3);
            total_summation += summation;
            //System.out.println(date+" == "+summation);
            yVals1.add(new BarEntry(i,summation));
        }
        System.out.println("month size :: "+cur_month.size());
        System.out.println("total_summation :: "+total_summation);
        System.out.println("total_summation Average :: "+total_summation/cur_month.size());
        avg_text.append(" ฿"+total_summation/cur_month.size());

        BarDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1,"รายได้ต่อแต่ละเดือน");
            set1.setDrawIcons(false);
            set1.setValueFormatter(new BottomXValueFormatter());
            //set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColors(getResources().getColor(R.color.primary_deshario));
            set1.setHighlightEnabled(true);
            set1.setHighLightColor(getResources().getColor(R.color.success_bootstrap));
            set1.setHighLightAlpha(200);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f); // font of chart value labels
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            mChart.setData(data);
        }
        mChart.animateXY(1000, 3000);
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
        ArrayList<String> previous_8_days = previous_months;
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

            months.add(i,Deshario_Functions.Th_Months(month));
            days.add(i,String.valueOf(day));

        }

        ArrayList<String>[] lists = (ArrayList<String>[])new ArrayList[2];
        lists[0] = months;
        lists[1] = days;

        return lists;
    }


}
