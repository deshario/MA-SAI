package com.deshario.agriculture.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Deshario on 7/10/2017.
 */

@Table(name = "ExpensePlan")
public class ExpensePlan extends Model {

    @Column(name = "area")
    private double area;

    @Column(name = "item_name")
    private String item_name;

    @Column(name = "expense")
    private double expense;

    @Column(name = "expense_x_area")
    private double expense_x_area;

    @Column(name = "expense_created")
    private String expense_created;

    public ExpensePlan() {
        super();
    }

    public ExpensePlan(double area, String item_name, double expense, double expense_x_area, String expense_created) {
        super();
        this.area = area;
        this.item_name = item_name;
        this.expense = expense;
        this.expense_x_area = expense_x_area;
        this.expense_created = expense_created;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getExpense_x_area() {
        return expense_x_area;
    }

    public void setExpense_x_area(double expense_x_area) {
        this.expense_x_area = expense_x_area;
    }

    public String getExpense_created() {
        return expense_created;
    }

    public void setExpense_created(String expense_created) {
        this.expense_created = expense_created;
    }

     /*
        || ======================= Database Functions ======================= ||
    */

    public static List<ExpensePlan> getAllExpensePlans() {
        return new Select().from(ExpensePlan.class).execute();
    }

    public static ExpensePlan getLatestExpenseByDate() {
        ExpensePlan expensePlan = new Select()
                .from(ExpensePlan.class)
                .orderBy("expense_created DESC")
                .executeSingle();
        return expensePlan;
    }


    public static boolean expense_exists(String date) {
        return new Select()
                .from(ExpensePlan.class)
                .where("expense_created = ?", date)
                .exists();
    }

    public static boolean expense_exists() {
        return new Select()
                .from(ExpensePlan.class)
                .exists();
    }
}
