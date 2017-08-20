package com.deshario.agriculture;

/**
 * Created by Deshario on 8/20/2017.
 */

import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class Deshario extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mChart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.deshario_chart);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        //mChart.setBackgroundColor(Color.TRANSPARENT);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(10f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(true); // limit lines are drawn behind data (and not on top)
        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(32); // month size


        mChart.animateXY(2500,2000); // x y
        //mChart.animateXY(3500,4000,Easing.EasingOption.EaseInCirc,Easing.EasingOption.EaseInCirc);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend leg = mChart.getLegend();

        // modify the legend ...
        leg.setForm(LegendForm.SQUARE); // modify the legend

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


// REaltiem
//        setData(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress());
//
//        // redraw
//        mChart.invalidate();



    private void setData(int total_days) {

        ArrayList<Entry> values = new ArrayList<Entry>();
        ArrayList<Entry> values2 = new ArrayList<Entry>();

        for (int i=0; i<total_days; i++) {
            //float val = (float) (Math.random() * range) + 3;
            float val = 0f;
            float val2 = 0f;

            if(i>20){
                val = 40f;
                val2 = 100+i;
            }else{
                val = 50f+i;
                val2 = 80+i;
            }

            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
            values2.add(new Entry(i, val2, getResources().getDrawable(R.mipmap.ic_launcher)));
        }

        LineDataSet set1,setbg;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0){
            setbg = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            setbg.setValues(values);
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }else{
            int pink_color = ContextCompat.getColor(Deshario.this, R.color.pink_graph);
            int info_color = ContextCompat.getColor(Deshario.this, R.color.info_bootstrap);
            int primary_color = ContextCompat.getColor(Deshario.this, R.color.primary_bootstrap);
            int success_color = ContextCompat.getColor(Deshario.this, R.color.success_bootstrap);
            int danger_color = ContextCompat.getColor(Deshario.this, R.color.danger_bootstrap);

            // Create a dataset
            setbg = new LineDataSet(values2, "Background");
            setbg.setDrawIcons(false);
            setbg.setDrawValues(false);
            setbg.enableDashedLine(10f, 5f, 0f);
            setbg.enableDashedHighlightLine(10f, 5f, 0f);
            setbg.setLineWidth(1f);
            setbg.setCircleRadius(3f);
            setbg.setDrawCircleHole(true);
            setbg.setValueTextSize(9f);
            setbg.setFillAlpha(230);
            setbg.setCircleColor(primary_color);
            setbg.setFillColor(pink_color);
            setbg.setColor(primary_color);
            setbg.setDrawFilled(true);
            setbg.setFormLineWidth(1f);
            setbg.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            setbg.setFormSize(15.f);


            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Income");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(9f);
            set1.setFillAlpha(230);
            set1.setCircleColor(danger_color);
            set1.setFillColor(info_color);
            set1.setColor(danger_color);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

//            if (Utils.getSDKInt() >= 18) {
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            }
//            else {
//                set1.setFillColor(Color.BLACK);
//            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(setbg); // add the datasets
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}