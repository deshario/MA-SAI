package com.deshario.agriculture.Models;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Deshario on 7/2/2017.
 */

@Table(name = "Records")
public class Records extends Model {

    @Column(name = "data_amount")
    private double data_amount;

    @Column(name = "category_id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Category category;

    @Column(name = "data_created", index = true)
    private Date data_recorded;

    @Column(name = "shortnote")
    private String shortnote;

    public Records() {
        super();
    }

    public Records(double data_amount, Category category, Date data_recorded, String shortnote) {
        super();
        this.data_amount = data_amount;
        this.category = category;
        this.data_recorded = data_recorded;
        this.shortnote = shortnote;
    }

    public double getData_amount() {
        return data_amount;
    }

    public void setData_amount(double data_amount) {
        this.data_amount = data_amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getData_recorded() {
        return data_recorded;
    }

    public void setData_recorded(Date data_recorded) {
        this.data_recorded = data_recorded;
    }

    public String getShortnote() {
        return shortnote;
    }

    public void setShortnote(String shortnote) {
        this.shortnote = shortnote;
    }

    public static List<Records> getAllRecords() {
        return new Select().from(Records.class).execute();
    }

    public static boolean records_exists() {
        return new Select()
                .from(Category.class)
                .exists();
    }

    public static Records getSingleRecord(int id) {
        Records record = new Select()
                .from(Records.class) // Specify the table to search
                .where("Id = ?", id) // search criteria
                .executeSingle(); // return only the first match
        return record;
    }

    public static Records getLatestSingleRecord() {
        Records record = new Select()
                .from(Records.class)
                .orderBy("Id DESC")
                .executeSingle();
        return record;
    }

    public static ArrayList<String> getSpecific(String field_name) {
        ArrayList<String> arraylist = new ArrayList<String>();
        String resultRecords = new Select().from(Records.class).toSql();
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        while (resultCursor.moveToNext()) {
            arraylist.add(resultCursor.getString(resultCursor.getColumnIndexOrThrow(field_name)));
        }
        return arraylist;
    }

    public static boolean check_exists(long date) {
        return new Select()
                .from(Records.class)
                .where("data_created = ?", date)
                .exists();
    }

    public static List<Records> joiner(int type) {
        return new Select()
                .from(Records.class)
                .innerJoin(Category.class).as("p")
                .on("u.Id = p.Id")
                .where("p.cat_type = 2")
                .execute();
    }

    public static ArrayList<String> getCustom(String field_name,int type){
        ArrayList<String> arraylist = new ArrayList<String>();
        String resultRecords = new Select()
                .from(Records.class)
                .join(Category.class)
                .on("Records.Id = Categories.Id")
                .where("Categories.Id = ?", type)
                .toSql();

        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        while(resultCursor.moveToNext()) {
            arraylist.add(resultCursor.getString(resultCursor.getColumnIndexOrThrow(field_name)));
        }
        return arraylist;
    }

}
