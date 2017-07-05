package com.deshario.agriculture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Deshario on 6/16/2017.
 */

public class Cal_Date extends AppCompatActivity {

    TextView txt1,txt2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_date);
        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);

        String dateInString = "16/06/2017";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date_current,new_date_current;

        Calendar c = Calendar.getInstance(); // Get Calendar Instance
        Date date = c.getTime();
//        try {
        // c.setTime(sdf.parse(dateInString));
        c.set(Calendar.YEAR, c.get(Calendar.YEAR));
        c.set(Calendar.MONTH, c.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));

        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        date_current = today.format(date);

//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        String formatted = sdf.format(date);
        try {
            System.out.println("Parse : "+sdf.parse(formatted));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txt1.setText("Current : "+date_current+"\nFormatted : "+formatted);
        c.add(Calendar.DATE, 20);  // add 45 days
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        date = c.getTime();
        Date resultdate = new Date(c.getTimeInMillis());   // Get new time
        dateInString = sdf.format(resultdate);
        System.out.println("Formatted Date : "+dateInString);
        new_date_current = today.format(date);
        txt2.setText("New : "+new_date_current+" (+ 20 Days)");


        // ============
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat _format = new SimpleDateFormat(pattern);
        Date d_date_ = null;
        try {
//            d_date_ = _format.parse("12/31/2006");
            d_date_ = _format.parse(formatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("D_Date : "+d_date_.getTime());

        String date_ex = today.format(d_date_);

        System.out.println("Compiled : "+date_ex);
        // ============




        // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date_ok = new Date();
        //ContentValues initialValues = new ContentValues();
//        initialValues.put("date_created", dateFormat.format(date_ok));
        System.out.println("SQLITE DATE : "+dateFormat.format(date_ok));
    }
}
