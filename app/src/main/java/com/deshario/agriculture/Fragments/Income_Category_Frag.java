package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Adapters.IncomeCategoryAdapter;
import com.deshario.agriculture.Adapters.PayDebtAdapter;
import com.deshario.agriculture.Deshario_Functions;
import com.deshario.agriculture.Models.Category;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.PayDebt;
import com.deshario.agriculture.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Income_Category_Frag extends Fragment implements DatePickerDialog.OnDateSetListener{

    String toolbar_title;
    Context context;
    double totalvalue = 0.0;

    EditText date_1,date_2;
    String tag_date_1 = "tag_date_1";
    String tag_date_2 = "tag_date_2";
    Calendar cal_date1 = Calendar.getInstance();;
    Calendar cal_date2 = Calendar.getInstance();
    ImageView close2;
    String daterange1,daterange2;

    List<Integer> mycolors = new ArrayList<>();

    private PieChart mChart;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    View line1;

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

        mChart = (PieChart)view.findViewById(R.id.chart1);
        line1 = (View)view.findViewById(R.id.lineafterchart);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.income_category_list);

        ImageButton img_settings = (ImageButton)view.findViewById(R.id.category_settings);
        img_settings.setImageDrawable(Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_settings_white_24dp),
                getResources().getColor(R.color.primary_bootstrap))
        );
        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings();
            }
        });

        manage();

        return view;
    }

    private void settings(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calendar_range, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(view);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.7f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView header = (TextView)view.findViewById(R.id.title);
        Drawable img = Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_date_range_white_24dp),
                getResources().getColor(R.color.default_bootstrap)
        );
        header.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);

        ImageButton cid = (ImageButton)view.findViewById(R.id.dismiss_btn);
        cid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        close2 = (ImageView)view.findViewById(R.id.close2);
        date_1 = (EditText)view.findViewById(R.id.date1);
        date_2 = (EditText)view.findViewById(R.id.date2);
        date_2.setEnabled(false);
        date_2.setFocusable(false);
        Button btn_clear = (Button)view.findViewById(R.id.clear_btn);
        Button btn_save = (Button)view.findViewById(R.id.save_btn);
        date_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal_date1 = Calendar.getInstance();
                DatePickerDialog dpd1= DatePickerDialog.newInstance(
                        Income_Category_Frag.this,
                        cal_date1.get(Calendar.YEAR),
                        cal_date1.get(Calendar.MONTH),
                        cal_date1.get(Calendar.DAY_OF_MONTH)
                );
                dpd1.show(getActivity().getFragmentManager(), tag_date_1);
                dpd1.setCancelText("ยกเลิก");
                dpd1.setOkText("เลือก");
            }
        });
        date_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal_date2 = Calendar.getInstance();
                DatePickerDialog dpd2 = DatePickerDialog.newInstance(
                        Income_Category_Frag.this,
                        cal_date2.get(Calendar.YEAR),
                        cal_date2.get(Calendar.MONTH),
                        cal_date2.get(Calendar.DAY_OF_MONTH)
                );
                dpd2.show(getActivity().getFragmentManager(), tag_date_2);
                dpd2.setCancelText("ยกเลิก");
                dpd2.setOkText("เลือก");
                dpd2.setMinDate(cal_date1);
            }
        });
        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_2.setText("");
                cal_date2.clear();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(daterange1 == null || daterange2 == null){
                    Toasty.info(getActivity(),"วันที่ผิดพลาด", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    search(daterange1,daterange2);
                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_1.setText("");
                date_2.setText("");
                date_2.setEnabled(false);
                date_2.setFocusable(false);
                cal_date1.clear();
                cal_date2.clear();
                daterange1 = null;
                daterange2 = null;
                close2.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void search(String daterange1, String daterange2){
        System.out.println("Search Data From "+daterange1+" TO "+daterange2);
    }

    private void Error_404(){
        String no_data = "ไม่พบข้อมูล";
        mChart.setNoDataText(no_data);
        mChart.setNoDataTextColor(context.getResources().getColor(R.color.default_bootstrap));
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/bangna-new.ttf");
        Paint p = mChart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(80);
        //p.setTypeface(typeface);
        line1.setVisibility(View.INVISIBLE);
    }

    public void manage(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String current_month = sdf.format(calendar.getTime());

        List<Records> records = Records.getSumofEachCatItem(current_month,Category.CATEGORY_INCOME);
        double total = 0.0;
        List<Integer> colors = new ArrayList<>();
        List<Integer> all_colors = Deshario_Functions.getRandomColors();

        for(int i=0; i<records.size(); i++){
            /* ################# Find Total Start ################# */
            Records myrecords = records.get(i);
            total += myrecords.getData_amount();
            /* ################# Find Total End ################### */

            /* ################# Colors Start ################# */
            if(all_colors.size() > records.size()){
                colors.add(i,all_colors.get(i));
            }else{
                System.out.println("Lack of colors");
                colors.add(i,context.getResources().getColor(R.color.default_bootstrap));
            }
            /* ################# Colors End ################# */
        }

        mycolors = colors;
        totalvalue = total;

        ArrayList<Double> percents = new ArrayList<>();
        for(int i=0; i<records.size(); i++){ // Get Percents
            Records myrecords = records.get(i);
            double amount = myrecords.getData_amount();
            double percent = Deshario_Functions.getPercentFromTotal(amount,total);
            //String item = myrecords.getCategory().getCat_item();
            //int type = myrecords.getCategory().getCat_type();
            //System.out.println("I"+i+" : "+item+" = "+amount+" = "+ Deshario_Functions.getDecimalFormat(percent)+"%");
            percents.add(Deshario_Functions.getDecimalFormat(percent));
        }

        if(records.size() != 0 && records.size()> 0){
            work(percents);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new IncomeCategoryAdapter(records,colors,Category.CATEGORY_INCOME,percents,getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }else{
            System.out.println("Records Size :: "+records.size());
            System.out.println("No Data Found");
            Error_404();
        }
    }

    public void work(ArrayList<Double> percents_set){
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

//         mChart.setUnit(" €");
//         mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);

        setData(percents_set);

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
        //mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
        mChart.getData().setDrawValues(false);
    }

    private void setData(ArrayList<Double> my_percents) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i=0; i<my_percents.size(); i++) {
            float data_amount = Deshario_Functions.getfloatValue(my_percents.get(i));
            entries.add(new PieEntry(data_amount));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(2f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(mycolors);
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
        s.setSpan(new RelativeSizeSpan(1.7f), word.length(), s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
//        s.setSpan(new RelativeSizeSpan(1.4f), word.length()+1, word.length()+2, 0);

        return s;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String mytag = view.getTag().toString();
        String month = Deshario_Functions.add_zero_or_not(++monthOfYear);
        String day = Deshario_Functions.add_zero_or_not(dayOfMonth);
        String selected_date = year+"-"+month+"-"+day;
        String sel_month = Deshario_Functions.Th_Months(monthOfYear,true);
        int sel_year = Deshario_Functions.Th_Year(year);
        int sel_day = dayOfMonth;
        String total_date = sel_day+" "+sel_month+" "+sel_year;
        if(mytag == tag_date_1){
            daterange1 = selected_date;
            date_1.setText(total_date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = dateFormat.parse(selected_date);
                cal_date1.setTime(date);
            }catch (ParseException e){e.printStackTrace();}
            date_2.setText("");
            date_2.setEnabled(true);
            date_2.setFocusable(true);
            close2.setVisibility(View.INVISIBLE);
        }
        if(mytag == tag_date_2){
            daterange2 = selected_date;
            date_2.setText(total_date);
            close2.setVisibility(View.VISIBLE);
        }
    }
}
