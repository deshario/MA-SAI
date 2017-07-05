package com.deshario.agriculture.Fragments;


import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.time.RadialPickerLayout;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Planning2_Frag extends Fragment implements FullScreenDialogContent,
        com.layernet.thaidatetimepicker.date.DatePickerDialog.OnDateSetListener {

    EditText et_area,et_price,et_ans,et_date;
    String field1,field;
    Calendar now;
    Context mcontext;
    DecimalFormat desharioformat;
    private FullScreenDialogController dialogController;

    public Planning2_Frag(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.planning_two, container, false);

        mcontext = getContext();
        desharioformat = new DecimalFormat();
        desharioformat.applyPattern("0.00");

        et_area = (EditText)view.findViewById(R.id.area);
        et_price = (EditText)view.findViewById(R.id.guess_price);
        et_ans = (EditText)view.findViewById(R.id.answer);
        et_date = (EditText)view.findViewById(R.id.date_guess);

        et_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Double _area = 0.00, _price = 0.00;
                String area = s.toString();
                String price = et_price.getText().toString();
                if(price.isEmpty()){
                    price = "0";
                }else{
                    _price = Double.parseDouble(price);
                }
                if(area.isEmpty()){
                    area = "0";
                }else{
                    _area = Double.parseDouble(area);
                }
                update_value(_area,_price);
            }
        });
        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Double _area = 0.00, _price = 0.00;
                String price = s.toString();
                String area = et_area.getText().toString();
                if(area.isEmpty()){
                    area = "0";
                }else{
                    _area = Double.parseDouble(area);
                }
                if(price.isEmpty()){
                    price = "0";
                }else{
                    _price = Double.parseDouble(price);
                }
                update_value(_area,_price);
            }
        });

        et_date.setText(" "+default_date());

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"TEST",Toast.LENGTH_SHORT).show();
                now = Calendar.getInstance();
                FragmentManager manager = ((AppCompatActivity)mcontext).getFragmentManager();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Planning2_Frag.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(manager, "Datepickerdialog");
                dpd.setCancelText("ยกเลิก");
                dpd.setOkText("เลือก");
            }
        });

        return view;
    }

    public void update_value(Double data1, Double data2){
        Double d1 = Double.valueOf(data1);
        Double d2 = Double.valueOf(data2);
        Double answer = d1*d2;
        //Toast.makeText(getActivity(),"d1:"+data1+"\nd2:"+new_item,Toast.LENGTH_SHORT).show();
        et_ans.setText(String.valueOf(desharioformat.format(answer)));
        et_ans.append(" บาท");
    }

    private String default_date(){
        now = Calendar.getInstance();
        String date_current;
        now.set(Calendar.YEAR, Th_Year(now.get(Calendar.YEAR)));
        now.set(Calendar.MONTH, now.get(Calendar.MONTH));
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        date_current = today.format(now.getTime());
        //now = Calendar.getInstance();  // Reset Year
        return date_current;
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;
    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        Main_Frag.tabBarView.resetFocusOnAllTabs();
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        Main_Frag.tabBarView.resetFocusOnAllTabs();
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, Th_Year(year));
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("TH"));
        Date date = new Date();
        et_date.setText(dateFormat.format(now.getTime()));
        String _date_ = et_date.getText().toString();
        et_date.setText(" "+today.format(now.getTime()));
        //Toast.makeText(getActivity(),"Date : "+_date_,Toast.LENGTH_SHORT).show(); // use for insert
        //System.out.println("Date : "+dateFormat.format(date)); //2017-06-10 19:43:39
        //System.out.println("Date : "+date); //Sat Jun 10 19:43:39 GMT+07:00 2017
    }


    public static int Th_Year(int en_year){
        return en_year+543;
    }
}
