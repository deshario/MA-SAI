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
import com.deshario.agriculture.Formatter.Years_XAxisValueFormatter;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Expense_Per_Year_Frag extends Fragment {

    protected BarChart mChart;
    String toolbar_title;
    TextView avg_text;

    public Expense_Per_Year_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.yearly_income_chart, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        Bundle bundle = getArguments();
        toolbar_title = bundle.getString("title2");
        textView.setText(toolbar_title);

        Toolbar toolbar_chart = (Toolbar)view.findViewById(R.id.chart_toolbar);
        toolbar_chart.setBackgroundColor(getResources().getColor(R.color.deep_orange));

        ImageButton img_refresh = (ImageButton)view.findViewById(R.id.my_refresh);
        avg_text = (TextView)toolbar_chart.findViewById(R.id.yearly_avg);
        img_refresh.setImageDrawable(Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_refresh_white_24dp),
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
        xAxis.setValueFormatter(new Years_XAxisValueFormatter());

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
        ArrayList<String> previous_7years = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        //System.out.println("Current year :: "+sdf.format(calendar.getTime()));
        calendar.add(Calendar.YEAR,-7); // 7 years ago
        for(int i=0; i<7; i++){
            calendar.add(Calendar.YEAR,1);
            String date = sdf.format(calendar.getTime());
            previous_7years.add(i,date);
        }
        setData(previous_7years);
    }

    private void setData(ArrayList<String> previous_7years) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        float total_summation = 0;
        for(int i=0; i<previous_7years.size(); i++){
            String year = previous_7years.get(i);
            double total = Records.getSumofEachYear(year, Category.CATEGORY_EXPENSE);
            total_summation += total;
            System.out.println(year+" == "+total);
            yVals1.add(new BarEntry(i,Deshario_Functions.getfloatValue(total)));
        }
        double average = Deshario_Functions.getDecimalFormat(total_summation/previous_7years.size());
        //System.out.println("Years size :: "+previous_7years.size());
        System.out.println("Years :: "+previous_7years);
        //System.out.println("7 Year Summation :: "+total_summation);
        //System.out.println("7 Year Average :: "+average);
        avg_text.append(" ฿"+average);

        BarDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1,"รายได้ต่อแต่ละปี");
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
