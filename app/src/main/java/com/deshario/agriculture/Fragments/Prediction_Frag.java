package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.deshario.agriculture.Deshario_Functions;
import com.deshario.agriculture.Models.ExpensePlan;
import com.deshario.agriculture.Models.IncomePlan;
import com.deshario.agriculture.ProgressBarAnimation;
import com.deshario.agriculture.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Prediction_Frag extends Fragment {
    TextView date1,date2,note_txt,empty_record;
    TextView income, expense, profit;
    TextView income_val, expense_val, profit_val;
    View dividerTitle;
    RoundCornerProgressBar prog_income,prog_expense,prog_profit;
    public static ImageButton btn_refresh;
    String thb = "\u0E3F";
    RelativeLayout col1, col2;

    public Prediction_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.prediction_frag, container, false);
        bindview(view);
        work();
        return view;
    }

    public void bindview(View view){
        col1 = (RelativeLayout)view.findViewById(R.id.col1);
        col2 = (RelativeLayout)view.findViewById(R.id.col2);
        dividerTitle = (View)view.findViewById(R.id.titleDivider);
        empty_record = (TextView)view.findViewById(R.id.nodata);
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
        boolean income_exist  = IncomePlan.income_exists();
        boolean expense_exist  = ExpensePlan.expense_exists();

        if(income_exist || expense_exist){
            String latestIncomeDate = IncomePlan.getLatestIncomeByDate().getIncome_created();
            String latestExpenseDate = ExpensePlan.getLatestExpenseByDate().getExpense_created();

            String maxdate = Deshario_Functions.getMaxDate(latestIncomeDate,latestExpenseDate);
            String dates[] = Deshario_Functions.getThaiDate(maxdate);
            String day = dates[0];
            String month = dates[1];
            String year = dates[2];

            final List<IncomePlan> tempIncomePlans = IncomePlan.getLatestIncomeBySameDate(maxdate);
            final List<ExpensePlan> tempExpensePlans = ExpensePlan.getLatestExpenseBySameDate(maxdate);

            IncomePlan finalIncomePlan = manageIncomePlans(tempIncomePlans);
            ExpensePlan finalExpensePlan = manageExpensePlans(tempExpensePlans);

            double Mincome = finalIncomePlan.getIncome_x_area();
            double Mexpense = finalExpensePlan.getExpense_x_area();
            double MTotal = Mincome-Mexpense;

            if(Mincome >= Mexpense){
                profit.setText("กำไร");
                profit.setTextColor(getResources().getColor(R.color.material_primary));
                profit_val.setTextColor(getResources().getColor(R.color.material_primary));
            }else{
                profit.setText("ขาดทุน");
                profit.setTextColor(getResources().getColor(R.color.material_danger));
                profit_val.setTextColor(getResources().getColor(R.color.material_danger));
            }

            setData(income_val,prog_income,Mincome);
            setData(expense_val,prog_expense,Mexpense);
            setData(profit_val,prog_profit,Double.valueOf(Deshario_Functions.filterNum(Deshario_Functions.getDecimal2Format(MTotal))));

            date1.setText(day);
            date2.setText(month+" "+year);

            date1.setVisibility(View.VISIBLE);
            date2.setVisibility(View.VISIBLE);
            note_txt.setVisibility(View.VISIBLE);
            dividerTitle.setVisibility(View.VISIBLE);
            empty_record.setVisibility(View.GONE);

            col1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(),"SHOW LIST1",Toast.LENGTH_SHORT).show();
                    for (int i=0; i<tempIncomePlans.size(); i++){
                        System.out.println("tempIncomePlans :: "+tempIncomePlans.get(i).getIncome_x_area());
                    }
                }
            });

            col2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getActivity(),"SHOW LIST2",Toast.LENGTH_SHORT).show();
                    for (int i=0; i<tempExpensePlans.size(); i++){
                        System.out.println("tempExpensePlans :: "+tempExpensePlans.get(i).getExpense_x_area());
                    }
                }
            });

        }else{
            NO_DATA_EXISTS();
        }
    }

    private void NO_DATA_EXISTS() {
        date1.setVisibility(View.GONE);
        date2.setVisibility(View.GONE);
        note_txt.setVisibility(View.GONE);
        dividerTitle.setVisibility(View.GONE);
        empty_record.setVisibility(View.VISIBLE);
        income_val.setText(thb + "0.00");
        expense_val.setText(thb + "0.00");
        profit.setTextColor(getResources().getColor(R.color.material_primary));
        profit_val.setTextColor(getResources().getColor(R.color.material_primary));
        profit_val.setText(thb + "0.00");
        prog_income.setProgress(0);
        prog_expense.setProgress(0);
        prog_profit.setProgress(0);
    }

    private static IncomePlan manageIncomePlans(List<IncomePlan> incomePlans){
        IncomePlan totalIncomePlan = new IncomePlan();
        double totalIncome  = 0.0;
        double totalArea  = 0.0;
        double totalIncomeArea  = 0.0;
        for(int i=0; i<incomePlans.size(); i++){
            String itemName = incomePlans.get(i).getItem_name();
            String itemCreated = incomePlans.get(i).getIncome_created();
            double area = incomePlans.get(i).getArea();
            double income = incomePlans.get(i).getIncome();
            double finalIncomeArea = income*area;
            totalIncomeArea = totalIncomeArea + finalIncomeArea;
            if(income != 0.0){
                totalIncome = totalIncome+income;
            }
            if(area != 0.0){
                totalArea = totalArea+area;
            }
            totalIncomePlan.setItem_name(itemName);
            totalIncomePlan.setIncome(totalIncome);
            totalIncomePlan.setArea(totalArea);
            totalIncomePlan.setIncome_x_area(totalIncomeArea);
            totalIncomePlan.setIncome_created(itemCreated);
        }
        return totalIncomePlan;
    }

    private static ExpensePlan manageExpensePlans(List<ExpensePlan> expensePlans){
        ExpensePlan totalExpensePlan = new ExpensePlan();
        double totalArea  = 0.0;
        double totalExpense  = 0.0;
        double totalExpenseArea  = 0.0;
        for(int i=0; i<expensePlans.size(); i++){
            String itemName = expensePlans.get(i).getItem_name();
            String itemCreated = expensePlans.get(i).getExpense_created();
            double area = expensePlans.get(i).getArea();
            double expense = expensePlans.get(i).getExpense();
            double finalExpenseArea = expense*area;
            totalExpenseArea = totalExpenseArea + finalExpenseArea;

            if(expense != 0.0){
                totalExpense = totalExpense+expense;
            }
            if(area != 0.0){
                totalArea = totalArea+area;
            }
            totalExpensePlan.setItem_name(itemName);
            totalExpensePlan.setExpense(totalExpense);
            totalExpensePlan.setArea(totalArea);
            totalExpensePlan.setExpense_x_area(totalExpenseArea);
            totalExpensePlan.setExpense_created(itemCreated);
        }
        return totalExpensePlan;
    }

    private void setData(TextView textView, RoundCornerProgressBar bar, double data){
        textView.setText(thb+Deshario_Functions.getDecimal2Format(Double.valueOf(data)));
        bar.setProgressColor(getResources().getColor(R.color.material_primary));
        if(data <= 0){
            bar.setProgress(0);
        }else{
            ProgressBarAnimation anim = new ProgressBarAnimation(bar, 0, 100);
            anim.setDuration(1000);
            bar.startAnimation(anim);
        }
    }

    private void setData(TextView textView, RoundCornerProgressBar bar, double data, int color){
        textView.setText(thb+Deshario_Functions.getDecimal2Format(Double.valueOf(data)));
        bar.setProgressColor(color);
        if(data <= 0){
            bar.setProgress(0);
        }else{
            ProgressBarAnimation anim = new ProgressBarAnimation(bar, 0, 100);
            anim.setDuration(1000);
            bar.startAnimation(anim);
        }
    }

}
