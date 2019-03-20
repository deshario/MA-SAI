package com.deshario.agriculture.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Deshario on 7/10/2017.
 */

@Table(name = "IncomePlan")
public class IncomePlan extends Model {

    @Column(name = "area")
    private double area;

    @Column(name = "item_name")
    private String item_name;

    @Column(name = "income")
    private double income;

    @Column(name = "income_x_area")
    private double income_x_area;

    @Column(name = "income_created")
    private String income_created;

    public IncomePlan() {
        super();
    }

    public IncomePlan(double area, String item_name, double income, double income_x_area, String income_created) {
        super();
        this.area = area;
        this.item_name = item_name;
        this.income = income;
        this.income_x_area = income_x_area;
        this.income_created = income_created;
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

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getIncome_x_area() {
        return income_x_area;
    }

    public void setIncome_x_area(double income_x_area) {
        this.income_x_area = income_x_area;
    }

    public String getIncome_created() {
        return income_created;
    }

    public void setIncome_created(String income_created) {
        this.income_created = income_created;
    }

    /*
       || ======================= Database Functions ======================= ||
   */

    public static List<IncomePlan> getAllIncomePlans() {
        return new Select().from(IncomePlan.class).execute();
    }

    public static IncomePlan getLatestIncomeByDate() {
        return new Select()
            .from(IncomePlan.class)
            .orderBy("income_created DESC")
            .executeSingle();
    }

    public static boolean income_exists(String date) {
        return new Select()
            .from(IncomePlan.class)
            .where("income_created = ?", date)
            .exists();
    }

    public static boolean income_exists() {
        return new Select()
            .from(IncomePlan.class)
            .exists();
    }

    public static List<IncomePlan> getLatestIncomeBySameDate(String Mdate) {
        return new Select()
            .from(IncomePlan.class)
            .where("income_created = ?",Mdate)
            .orderBy("income_created DESC")
            .execute();
    }
}
