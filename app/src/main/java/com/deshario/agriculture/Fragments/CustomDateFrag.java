package com.deshario.agriculture.Fragments;


import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.afollestad.materialdialogs.MaterialDialog;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDateFrag extends Fragment implements DatePickerDialog.OnDateSetListener {

    static MaterialDialog.Builder builder;
    static MaterialDialog dialog;
    Calendar now;
    static DatePickerDialog dpd;
    static FragmentManager manager;
    TextView dat;


    public CustomDateFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_custom_date, container, false);
     //   View view = inflater.inflate(R.layout.profile_layout, container, false);
        now = Calendar.getInstance();
        dat = (TextView)view.findViewById(R.id.selected_date);
//
//        new MaterialDialog.Builder(getActivity())
//                .title(R.string.app_name)
//                .content(R.string.app_name)
//                .positiveText(R.string.app_name)
//                .negativeText(R.string.app_name)
//                .show();
        boolean wrapInScrollView = true;
//        builder = new MaterialDialog.Builder(getActivity())
//                .title("Title")
////                .content("content")
//                .customView(R.layout.fragment_custom_date, wrapInScrollView)
//                .positiveText("Agree");

        manager = ((AppCompatActivity)getContext()).getFragmentManager();
        dpd = DatePickerDialog.newInstance(
                CustomDateFrag.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setCancelText("ยกเลิก");
        dpd.setOkText("เลือก");
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
               // dat.setText("กรุณาเลือกวันที่ !");
            }
        });

        return view;
    }

    public static void ask_date(){


//        dialog = builder.build();
//        dialog.show();

        dpd.show(manager, "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat today = DateFormat.getDateInstance(DateFormat.FULL, new Locale("TH")); // DATE
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("TH"));
        Date date = new Date();
        dat.setText(dateFormat.format(now.getTime()));
        String _date_ = dat.getText().toString();
        dat.setText(" "+today.format(now.getTime()));
        //Toast.makeText(getActivity(),"Date : "+_date_,Toast.LENGTH_SHORT).show(); // use for insert
        //System.out.println("Date : "+dateFormat.format(date)); //2017-06-10 19:43:39
        //System.out.println("Date : "+date); //Sat Jun 10 19:43:39 GMT+07:00 2017
    }

    public int Th_Year(int en_year){
        return en_year+543;
    }
}
