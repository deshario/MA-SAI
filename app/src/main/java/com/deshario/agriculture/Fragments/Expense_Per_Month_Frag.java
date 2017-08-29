package com.deshario.agriculture.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.deshario.agriculture.Deshario_Functions;
import com.deshario.agriculture.Formatter.BottomXValueFormatter;
import com.deshario.agriculture.Formatter.Month_XAxisValueFormatter;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Expense_Per_Month_Frag extends Fragment {

    protected BarChart mChart;
    String toolbar_title;
    public static ArrayList<String> previous_months;
    TextView avg_text;

    public Expense_Per_Month_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.monthly_income_chart, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        Bundle bundle = getArguments();
        toolbar_title = bundle.getString("title2");
        textView.setText(toolbar_title);

        Toolbar toolbar_chart = (Toolbar)view.findViewById(R.id.chart_toolbar);
        toolbar_chart.setBackgroundColor(getResources().getColor(R.color.deep_orange));

        ImageButton img_refresh = (ImageButton)view.findViewById(R.id.my_refresh);
        ImageButton img_settings = (ImageButton)view.findViewById(R.id.my_setting);
        avg_text = (TextView)toolbar_chart.findViewById(R.id.monthly_avg);
        img_refresh.setImageDrawable(Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_refresh_white_24dp),
                getResources().getColor(R.color.deep_orange))
        );
        img_settings.setImageDrawable(Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_settings_white_24dp),
                getResources().getColor(R.color.deep_orange))
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
        work();
        return view;
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
        xAxis.setValueFormatter(new Month_XAxisValueFormatter());


        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setEnabled(false);
        //leftAxis.setTypeface(mTfLight);
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
        setData(previous_months);
    }

    private void setData(ArrayList<String> cur_month) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        float total_summation = 0;
        for(int i=0; i<cur_month.size(); i++){
            String date = cur_month.get(i);
            float summation  = Records.getSumofEachMonth(date,Category.CATEGORY_EXPENSE);
            total_summation += summation;
            //System.out.println(date+" == "+summation);
            yVals1.add(new BarEntry(i,summation));
        }
        double average = Deshario_Functions.getDecimalFormat(total_summation/cur_month.size());
        //System.out.println("Months Size :: "+cur_month.size());
        //System.out.println("Months :: "+cur_month);
        //System.out.println("8 Months Summation :: "+total_summation);
        //System.out.println("8 Months Average :: "+average);
        avg_text.append(" ฿"+average);

        BarDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1,"ค่าใช้จ่ายต่อแต่ละเดือน");
            set1.setDrawIcons(false);
            set1.setValueFormatter(new BottomXValueFormatter());
            //set1.setColors(ColorTemplate.MATERIAL_COLORS);
            set1.setColors(getResources().getColor(R.color.deep_orange));
            set1.setHighlightEnabled(true);
            set1.setHighLightColor(getResources().getColor(R.color.orange));
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

}
