package com.deshario.agriculture.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deshario.agriculture.Config.Deshario_Functions;
import com.deshario.agriculture.Formatter.BottomXValueFormatter;
import com.deshario.agriculture.Formatter.Day_XAxisValueFormatter;
import com.deshario.agriculture.Formatter.YAxisValueFormatter;
import com.deshario.agriculture.Models.Category;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Income_Per_Date_Frag extends Fragment {

    protected BarChart mChart;
    String toolbar_title;
    public static ArrayList<String> myDays;

    public Income_Per_Date_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.daily_income_chart, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        Bundle bundle = getArguments();
        toolbar_title = bundle.getString("title1");
        textView.setText(toolbar_title);
        ImageButton img_refresh = (ImageButton)view.findViewById(R.id.my_refresh);
        img_refresh.setImageDrawable(Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_refresh_white_24dp),
                getResources().getColor(R.color.material_primary))
        );
        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChart.invalidate();
                mChart.notifyDataSetChanged();
                while(!mChart.isFullyZoomedOut()){
                    mChart.zoomOut();
                }
                mChart.animateXY(1000,2000);
            }
        });

        mChart = (BarChart)view.findViewById(R.id.chart1);
        dbwork();
        return view;
    }

    private void dbwork(){
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
        myDays = previous_8days;

        String first_date = previous_8days.get(0);
        String last_date = previous_8days.get(previous_8days.size()-1);

        List<Records> abc = Records.getDataBetweenDays(first_date,last_date, Category.CATEGORY_INCOME);
        System.out.println(""+Category.CATEGORY_INCOME);
        ArrayList<String> found_dates = new ArrayList<>();
        for(int a=0; a<abc.size(); a++){
            String my_date = abc.get(a).getData_created();
            found_dates.add(a,my_date);
        }

        System.out.println("previous_8days : "+previous_8days);
        System.out.println("found_dates : "+found_dates);

        int previous_week_data[] = new int[previous_8days.size()];
        for(int c=0; c<previous_8days.size(); c++){
            boolean status = Deshario_Functions.check_exists(found_dates,previous_8days.get(c));
            if(status == true){
                Records reca = Records.getSingleRecordsByDate(previous_8days.get(c),Category.CATEGORY_INCOME);
                System.out.println(previous_8days.get(c)+" : "+reca.getData_amount());
                Double d = new Double(reca.getData_amount());
                previous_week_data[c] = d.intValue();
            }else{
                Double d = new Double(0.0);
                previous_week_data[c] = d.intValue();
                System.out.println(previous_8days.get(c)+" : "+0.0);
            }
        }

        System.out.println("Max :: "+Deshario_Functions.maxValue(previous_week_data));
        System.out.println("Values are :: "+Arrays.toString(previous_week_data));

        work(previous_week_data);
    }

    public void work(int previous_week_data[]){
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
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new Day_XAxisValueFormatter());

        YAxis leftAxis = mChart.getAxisLeft();
        // leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(new YAxisValueFormatter());
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

        setData(previous_week_data);
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
            set1 = new BarDataSet(yVals1,"รายได้ต่อแต่ละวัน");
            set1.setDrawIcons(false);
            set1.setValueFormatter(new BottomXValueFormatter());
            //set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColors(getResources().getColor(R.color.material_primary));
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
        mChart.animateXY(1000, 2000);
        mChart.setHighlightFullBarEnabled(true);
    }

    public static ArrayList<String>[] getDate(){
        ArrayList<String> months = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        Date date = null;
        ArrayList<String> previous_8_days = getPreviousDayssize();
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

            months.add(i,Deshario_Functions.Th_Months(month,false));
            days.add(i,String.valueOf(day));

        }

        ArrayList<String>[] lists = (ArrayList<String>[])new ArrayList[2];
        lists[0] = months;
        lists[1] = days;

        return lists;
    }

    public static ArrayList<String> getPreviousDayssize(){
        return myDays;
    }


}
