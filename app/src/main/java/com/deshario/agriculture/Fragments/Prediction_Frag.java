package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.deshario.agriculture.Deshario_Functions;
import com.deshario.agriculture.Models.ExpensePlan;
import com.deshario.agriculture.Models.IncomePlan;
import com.deshario.agriculture.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Prediction_Frag extends Fragment {
    TextView date1,date2,note_txt;
    TextView income, expense, profit;
    TextView income_val, expense_val, profit_val;
    RoundCornerProgressBar prog_income,prog_expense,prog_profit;
    public static ImageButton btn_refresh;
    String thb = "\u0E3F";

    public Prediction_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.prediction_frag, container, false);
        bindview(view);
        work();
        return view;
    }

    public void bindview(View view){
        date1 = (TextView)view.findViewById(R.id.date1);
        date2 = (TextView)view.findViewById(R.id.date2);
        note_txt = (TextView)view.findViewById(R.id.note);

        income = (TextView) view.findViewById(R.id.item1);
        expense = (TextView) view.findViewById(R.id.item2);
        profit = (TextView) view.findViewById(R.id.item3);

        income_val = (TextView) view.findViewById(R.id.amount1);
        expense_val = (TextView) view.findViewById(R.id.amount2);
        profit_val = (TextView) view.findViewById(R.id.amount3);
        prog_income = (RoundCornerProgressBar)view.findViewById(R.id.progress1);
        prog_expense = (RoundCornerProgressBar)view.findViewById(R.id.progress2);
        prog_profit = (RoundCornerProgressBar)view.findViewById(R.id.progress3);

        btn_refresh = (ImageButton) view.findViewById(R.id.refresh_btn);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work();
            }
        });
    }

    private void work(){
        boolean income_e  = IncomePlan.income_exists();
        boolean expense_e  = ExpensePlan.expense_exists();

        if(income_e == true || expense_e == true){
            IncomePlan incomePlan = IncomePlan.getLatestIncomeByDate();
            ExpensePlan expensePlan = ExpensePlan.getLatestExpenseByDate();
            String income,expense;

            if(incomePlan == null){
                income = "0.00";
            }else{
                income = Deshario_Functions.getDecimal2Format(incomePlan.getIncome_x_area());
            }

            if(expensePlan == null){
                expense = "0.00";
            }else{
                expense = Deshario_Functions.getDecimal2Format(expensePlan.getExpense_x_area());
            }

            double Mincome = Double.valueOf(income);
            double Mexpense = Double.valueOf(expense);
            double MTotal = Mincome-Mexpense;

            if(Mincome > Mexpense){
                profit.setText("กำไร");
            }else{
                profit.setText("ขาดทุน");
                profit_val.setTextColor(getResources().getColor(R.color.material_danger));
            }

            income_val.setText(thb+income);
            expense_val.setText(thb+expense);
            profit_val.setText(thb+Deshario_Functions.filterNum(Deshario_Functions.getDecimal2Format(MTotal)));

        }else{
            NO_DATA_EXISTS();
        }
    }

    private void NO_DATA_EXISTS() {
        String dates[] = getDefault_date();
        String day = dates[0];
        String month = dates[1];
        String year = dates[2];
        income_val.setText(thb + "0.00");
        expense_val.setText(thb + "0.00");
        profit_val.setText(thb + "0.00");
        date1.setText(day);
        date2.setText(month + " " + year);
        prog_income.setProgress(0);
        prog_expense.setProgress(0);
        prog_profit.setProgress(0);
    }

    private String[] getDefault_date(){
        String full_thaidate = null;
        Date today = Calendar.getInstance().getTime();
        String DATE_FORMAT_NOW = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(today);
        String[] output = stringDate.split("-");

        int _day = Integer.valueOf(output[0]);
        int _month = Integer.valueOf(output[1]);
        int _year = Integer.valueOf(output[2]);

        int day_ = _day;
        String month_ = Th_Months(_month-1,true);
        int year_ = Th_Year(_year);
        full_thaidate = day_+" "+month_+" "+year_;

        return new String[]{
                String.valueOf(day_),
                month_,
                String.valueOf(year_),
                full_thaidate
        };
    }

    public String Th_Months(int month, boolean full){
        String[] th_months_short = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
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
            return th_months_long[month];
        }else{
            return th_months_short[month];
        }
    }

    public int Th_Year(int en_year){
        return en_year+543;
    }

}
