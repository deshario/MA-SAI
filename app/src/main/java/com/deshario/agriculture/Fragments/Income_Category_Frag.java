package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deshario.agriculture.Adapters.IncomeCategoryAdapter;
import com.deshario.agriculture.Adapters.PayDebtAdapter;
import com.deshario.agriculture.Deshario_Functions;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.PayDebt;
import com.deshario.agriculture.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Income_Category_Frag extends Fragment{

    String toolbar_title;
    Context context;
    double totalvalue = 0.0;

    private PieChart mChart;
    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    public Income_Category_Frag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.income_category, container, false);
        context = view.getContext();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        Bundle bundle = getArguments();
        toolbar_title = bundle.getString("title1");
        textView.setText(toolbar_title);

        List<Records> records = Records.getSumofEachCatItem("2017-08",1);
        double total = 0.0;
        for(int i=0; i<records.size(); i++){
            Records myrecords = records.get(i);
            total += myrecords.getData_amount();
        }

        // Random colors from size of Records
        System.out.println("Size :: "+records.size());

        totalvalue = total;
        ArrayList<Double> percents = new ArrayList<>();
        for(int i=0; i<records.size(); i++){
            Records myrecords = records.get(i);
            double amount = myrecords.getData_amount();
            String item = myrecords.getCategory().getCat_item();
            int type = myrecords.getCategory().getCat_type();
            double percent = Deshario_Functions.getPercentFromTotal(amount,total);
            System.out.println("I"+i+" : "+item+" = "+amount+" = "+ Deshario_Functions.getDecimalFormat(percent)+"%");
            percents.add(Deshario_Functions.getDecimalFormat(percent));
        }

        mChart = (PieChart)view.findViewById(R.id.chart1);
        work();

        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = (RecyclerView)view.findViewById(R.id.income_category_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new IncomeCategoryAdapter(records,percents,getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void work(){
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        //mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(80f); //58f
        mChart.setTransparentCircleRadius(61f); // 61f
        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);

        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(false);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
//        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

        mChart.setDrawEntryLabels(!mChart.isDrawEntryLabelsEnabled());
        for (IDataSet<?> set : mChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        mChart.invalidate();
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry(100));
//            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
////                    mParties[i % mParties.length],
//                    getResources().getDrawable(R.drawable.star)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {
//        String word = "รายได้ทั้งหมด";
//        SpannableString s = new SpannableString(word+"\n฿"+mytotla);
//        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
//        System.out.println("S Length :: "+s.length());
//        s.setSpan(new RelativeSizeSpan(1.6f), 0, word.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(1.7f), 14, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
//        return s;
        // 14 ร้ายได้ทั้งหมด
        String word = "ร้ายได้ทั้งหมด";
        String thb = "\u0E3F";
        SpannableString s = new SpannableString(word+"\n"+thb+totalvalue);
        s.setSpan(new RelativeSizeSpan(1.8f), 0, word.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.7f), 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
//        s.setSpan(new RelativeSizeSpan(1.4f), word.length()+1, word.length()+2, 0);

        return s;
    }



}
