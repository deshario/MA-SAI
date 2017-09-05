package com.deshario.agriculture;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Deshario on 8/26/2017.
 */

public class Deshario_Functions {

    // Get Colored Drawable
    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    // Find Max Value from array
    public static int maxValue(int array[]){
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        return Collections.max(list);
    }

     /* === Input Functions ===
    * 1. hideSoftInput
    * 2. hideKeyboard
    * */

    public static void hideSoftInput(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View v = ((Activity)context).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /* === DateTime Functions ===
    * 1. Get Thai Months Name from month no
    * 2. Convert Normal Year To Buddhist Year
    * */

//    public static String Th_Months(int month){
//        String[] th_months = new String[]{ //index start from 0
//                "ม.ค","ก.พ","มี.ค","เม.ย","พ.ค","มิ.ย","ก.ค","ส.ค","ก.ย","ต.ค","พ.ย","ธ.ค"
//        };
//        return th_months[month-1];
//    }

    public static String Th_Months(int month, boolean full){
        String[] th_months_short = new String[] {
                "ม.ค","ก.พ","มี.ค","เม.ย","พ.ค","มิ.ย","ก.ค","ส.ค","ก.ย","ต.ค","พ.ย","ธ.ค"
        };
        String[] th_months_long = new String[]{
                "มกราคม",
                "กุมภาพันธ์",
                "มีนาคม",
                "เมษายน",
                "พฤษภาคม",
                "มิถุนายน",
                "กรกฎาคม",
                "สิงหาคม",
                "กันยายน",
                "ตุลาคม",
                "พฤศจิกายน",
                "ธันวาคม"
        };
        if(full == true){
            return th_months_long[month-1];
        }else{
            return th_months_short[month-1];
        }
    }

    public static int Th_Year(int en_year){
        return en_year+543;
    }

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String add_zero_or_not(int number){
        String num = null;
        if(number < 10){
            num = "0"+number;
            return num;
        }else{
            num = ""+number;
            return num;
        }
    }

    public static boolean check_exists(ArrayList<String> datalist,String keywordtosearch){
        int index = datalist.indexOf(keywordtosearch);
        if(index <= -1){
            return false;
        }else{
            return true;
        }
    }

    public static double getPercentFromTotal(double myvalue,double total){
        return (myvalue/total)*100;
    }

    public static double getDecimalFormat(double data){
        DecimalFormat desharioformat = new DecimalFormat();
        desharioformat.applyPattern("0.00");
        return Double.valueOf(desharioformat.format(data));
    }

    public static ArrayList<Integer> getRandomColors(){
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

        return colors;
    }

    public static float getfloatValue(double myvalue) {
        return (float)myvalue;
    }

}
