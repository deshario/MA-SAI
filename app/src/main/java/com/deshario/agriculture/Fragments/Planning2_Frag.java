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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deshario.agriculture.AddRecords;
import com.deshario.agriculture.Models.ExpensePlan;
import com.deshario.agriculture.Models.IncomePlan;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Planning2_Frag extends Fragment implements FullScreenDialogContent,
        com.layernet.thaidatetimepicker.date.DatePickerDialog.OnDateSetListener {

    EditText et_area,et_name,et_expense,et_total,et_date;
    Button btn_save_expense;
    String expensedate;
    String SQLITE_DATE_FORMAT = "yyyy-MM-dd";
    Calendar now;
    Context context;
    DecimalFormat desharioformat;
    private FullScreenDialogController dialogController;

    public Planning2_Frag(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.planning_two, container, false);
        context = getContext();
        desharioformat = new DecimalFormat();
        desharioformat.applyPattern("0.00");
        bindview(view);
        work();
        return view;
    }

    public void bindview(View view){
        et_area = (EditText)view.findViewById(R.id.area);
        et_name = (EditText)view.findViewById(R.id.name);
        et_expense = (EditText)view.findViewById(R.id.expense);
        et_total = (EditText)view.findViewById(R.id.area_x_expense);
        et_date = (EditText)view.findViewById(R.id.date_expense);
        btn_save_expense = (Button) view.findViewById(R.id.saveexpense);
    }

    public void work(){
        et_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Double _area = 0.00, _price = 0.00;
                String area = s.toString();
                String price = et_expense.getText().toString();
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
        et_expense.addTextChangedListener(new TextWatcher() {
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
                now = Calendar.getInstance();
                FragmentManager manager = ((AppCompatActivity) context).getFragmentManager();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Planning2_Frag.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(manager, "Datepickerdialog");
                dpd.setCancelText("ยกเลิก");
                dpd.setOkText("เลือก");

                // Disable Specific Dates in Calendar
                List<ExpensePlan> Expenseplans = ExpensePlan.getAllExpensePlans();
                Calendar calendar = null;
                SimpleDateFormat sdf = new SimpleDateFormat(SQLITE_DATE_FORMAT);
                List<Calendar> dates = new ArrayList<>();
                Date date = null;

                for(int i=0; i<Expenseplans.size(); i++){
                    String used_date = Expenseplans.get(i).getExpense_created();
                    //System.out.println("Used Date : "+date);
                    try {
                        date = sdf.parse(used_date);
                        Planning2_Frag obj = new Planning2_Frag();
                        calendar = obj.dateToCalendar(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dates.add(calendar);
                }

                Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
                dpd.setDisabledDays(disabledDays1);
                // Disable Specific Dates in Calendar
            }
        });
        btn_save_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area = et_area.getText().toString();
                String name = et_name.getText().toString();
                String expense = et_expense.getText().toString();
                String total = et_total.getText().toString();
                String date = et_date.getText().toString();
                if(area.isEmpty() || name.isEmpty() || expense.isEmpty() || total.isEmpty() || date.isEmpty()){
                    Toasty.info(context,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                }else{
                    Double _area = Double.parseDouble(area);
                    Double _expense = Double.parseDouble(expense);
                    Double _total =  remove_text(total);
                    String item_name = name;
                    String date_expense = expensedate;

                    boolean date_exists = ExpensePlan.expense_exists(date_expense);
                    if(date_exists == true){
                        Toasty.warning(context," วันที่ที่คุณเลือกไม่ว่าง \n กรุณาเลือกวันที่อื่น",Toast.LENGTH_LONG).show();
                    }else{
                        ExpensePlan expensePlan = new ExpensePlan();
                        expensePlan.setItem_name(item_name);
                        expensePlan.setArea(_area);
                        expensePlan.setExpense(_expense);
                        expensePlan.setExpense_x_area(_total);
                        expensePlan.setExpense_created(date_expense);
                        expensePlan.save();
                        clear_fields();
                        Toasty.success(context,"ค่าใช้จ่ายของคุณถูกบันทึกแล้ว",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void update_value(Double data1, Double data2){
        Double d1 = Double.valueOf(data1);
        Double d2 = Double.valueOf(data2);
        Double answer = d1*d2;
        et_total.setText(String.valueOf(desharioformat.format(answer)));
        et_total.append(" บาท");
    }

    private String default_date(){
        String thai_date = null;
        Date temp_date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(SQLITE_DATE_FORMAT);
        String stringDate = sdf.format(temp_date);

        String[] output = stringDate.split("-");
        expensedate = stringDate;
        System.out.println("SQ DATE OK : "+expensedate);

        int _day = Integer.valueOf(output[2]);
        int _month = Integer.valueOf(output[1]);
        int _year = Integer.valueOf(output[0]);

        int day_ = _day;
        String month_ = Th_Months(_month-1);
        int year_ = Th_Year(_year);
        thai_date = day_+" "+month_+" "+year_;

        return thai_date;
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
        String month = AddRecords.add_zero_or_not(++monthOfYear);
        String day = AddRecords.add_zero_or_not(dayOfMonth);
        String selected_date = year+"-"+month+"-"+day;
        expensedate = selected_date;
        int sel_day = dayOfMonth;
        String sel_month = Th_Months(monthOfYear-1);
        int sel_year = Th_Year(year);
        String total_date = sel_day+" "+sel_month+" "+sel_year;
        System.out.println("SQ DATE SET : "+expensedate);
        et_date.setText(" "+total_date);
    }

    public String Th_Months(int month){
        String[] th_months = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
        };
        return th_months[month];
    }

    public static int Th_Year(int en_year){
        return en_year+543;
    }

    public double remove_text(String textwithnumber){
        String cutter = textwithnumber;
        cutter = cutter.replaceAll("[^\\d.]", "");
        double pure_no = Double.parseDouble(cutter);
        return pure_no;
    }

    private void clear_fields(){
        et_area.setText("");
        et_name.setText("");
        et_expense.setText("");
        et_total.setText("");
        et_date.setText(" "+default_date());
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
