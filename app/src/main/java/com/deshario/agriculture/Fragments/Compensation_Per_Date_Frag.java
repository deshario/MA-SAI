package com.deshario.agriculture.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deshario.agriculture.Config.Deshario_Functions;
import com.deshario.agriculture.Models.Category;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Compensation_Per_Date_Frag extends Fragment {

    public Compensation_Per_Date_Frag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        previousdays();
        return textView;
    }

    private void previousdays(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> previous_8days = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -8); // get starting date
        for(int i = 0; i<8; i++){ // loop adding one day in each iteration
            cal.add(Calendar.DAY_OF_YEAR, 1);
            String date = sdf.format(cal.getTime());
            previous_8days.add(i,date);
        }

        String first_date = previous_8days.get(0);
        String last_date = previous_8days.get(previous_8days.size()-1);

        List<Records> incomes = Records.getDataBetweenDays(first_date,last_date, Category.CATEGORY_INCOME);
        ArrayList<String> found_incomes_dates = new ArrayList<>();
        for(int a=0; a<incomes.size(); a++){
            String my_date = incomes.get(a).getData_created();
            found_incomes_dates.add(a,my_date);
        }

        List<Records> expenses = Records.getDataBetweenDays(first_date,last_date, Category.CATEGORY_EXPENSE);
        ArrayList<String> found_expense_dates = new ArrayList<>();
        for(int a=0; a<expenses.size(); a++){
            String my_date = expenses.get(a).getData_created();
            found_expense_dates.add(a,my_date);
        }

        int incomes_data[] = new int[previous_8days.size()];
        for(int c=0; c<previous_8days.size(); c++){
            boolean status = Deshario_Functions.check_exists(found_incomes_dates,previous_8days.get(c));
            if(status == true){
                Records reca = Records.getSingleRecordsByDate(previous_8days.get(c),Category.CATEGORY_INCOME);
                Double d = new Double(reca.getData_amount());
                incomes_data[c] = d.intValue();
            }else{
                Double d = new Double(0.0);
                incomes_data[c] = d.intValue();
            }
        }

        int expenses_data[] = new int[previous_8days.size()];
        for(int c=0; c<previous_8days.size(); c++){
            boolean status = Deshario_Functions.check_exists(found_expense_dates,previous_8days.get(c));
            if(status == true){
                Records reca = Records.getSingleRecordsByDate(previous_8days.get(c),Category.CATEGORY_EXPENSE);
                Double d = new Double(reca.getData_amount());
                expenses_data[c] = d.intValue();
            }else{
                Double d = new Double(0.0);
                expenses_data[c] = d.intValue();
            }
        }

//        System.out.println("previous_8days : "+previous_8days);
//        System.out.println("found_incomes_dates : "+found_incomes_dates);
//        System.out.println("found_expense_dates : "+found_expense_dates);
//
//        System.out.println("Incomes are :: "+ Arrays.toString(incomes_data));
//        System.out.println("Expenses are :: "+ Arrays.toString(expenses_data));

        getPercent(incomes_data,expenses_data);

    }

    public void getPercent(int incomes_data[], int expenses_data[]){
        int income_length = incomes_data.length;
        for(int i=0; i<income_length; i++){
            int income = incomes_data[i];
            int expense = expenses_data[i];
            int total = (income-expense)*100;
          //  int ans = total/income;
            System.out.println("Income :: "+incomes_data[i]+" Expense :: "+expenses_data[i]);
            System.out.println("Answer"+(i+1)+" :: "+total);
        }
    }


}
