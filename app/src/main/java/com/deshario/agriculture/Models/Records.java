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
    private Date data_created;

    @Column(name = "data_updated", index = true)
    private Date data_updated;

    @Column(name = "shortnote")
    private String shortnote;

    public Records() {
        super();
    }

    public Records(double data_amount, Category category, Date data_created, Date data_updated, String shortnote) {
        super();
        this.data_amount = data_amount;
        this.category = category;
        this.data_created = data_created;
        this.data_updated = data_updated;
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

    public Date getData_created() {
        return data_created;
    }

    public void setData_created(Date data_created) {
        this.data_created = data_created;
    }

    public Date getData_updated() {
        return data_updated;
    }

    public void setData_updated(Date data_updated) {
        this.data_updated = data_updated;
    }

    public String getShortnote() {
        return shortnote;
    }

    public void setShortnote(String shortnote) {
        this.shortnote = shortnote;
    }

    /*
        || ======================= Database Functions ======================= ||
    */

    public static List<Records> getAllRecords() {
        return new Select().from(Records.class).execute();
    }

    public static boolean records_exists() {
        return new Select()
                .from(Records.class)
                .exists();
    }

    public static Records getSingleRecord(int id) {
        Records record = new Select()
                .from(Records.class) // Specify the table to search
                .where("Id = ?", id) // search criteria
                .executeSingle(); // return only the first match
        return record;
    }

    public static Records getLatestRecordById() {
        Records record = new Select()
                .from(Records.class)
                .orderBy("Id DESC")
                .executeSingle();
        return record;
    }

    public static Records getLatestRecordByDate() {
        Records record = new Select()
                .from(Records.class)
                .orderBy("data_created DESC")
                .executeSingle();
        return record;
    }

    public static boolean previous_records_exists(long latestdate) {
        return new Select()
                .from(Records.class)
                .where("data_created < "+latestdate)
                .orderBy("data_created DESC")
                .limit(1)
                .exists();
    }

    public static Records getPreviousRecord(long latestdate){
        //select * from Records where data_created < 1499792400000 order by data_created desc limit 1;
        Records record = new Select()
                .from(Records.class)
                .where("data_created < "+latestdate)
                .orderBy("data_created DESC")
                .limit(1)
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

    public static boolean check_updated(long date) {
        return new Select()
                .from(Records.class)
                .where("data_updated = ?", date)
                .exists();
    }

    public static List<Records> getSpecificRecordsByType(int type) { // Get Records by specific category type
        return new Select()
                .from(Records.class)
                .innerJoin(Category.class)
                .on("Records.category_id = Categories.Id")
                .where("Categories.cat_type = "+type+" AND data_amount > 0")
                // We can use Multi where
                //.where("Categories.cat_type = "+type)
                //.where("data_amount > 0") //data_amount
                .execute();

    }

    public static List<Records> getSpecificRecordsByItem(Category category) { // Get Records by specific category type
        return new Select()
                .from(Records.class)
                .innerJoin(Category.class)
                .on("Records.category_id = Categories.Id")
                .where("Categories.cat_item = ?",category.getCat_item())
                .execute();
    }

    //public static ArrayList<String> getCustom(String field_name, Category category){
    //select * from Records INNER JOIN Categories ON(Records.category_id=Categories.Id) where Categories.cat_type=3;
    public static ArrayList<String> getCustom(String field_name, int type){
        ArrayList<String> arraylist = new ArrayList<String>();
        String resultRecords = new Select()
                .from(Records.class)
                .innerJoin(Category.class)
                .on("Records.category_id = Categories.Id")
                .where("Categories.cat_type = "+type)
                .toSql();
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        while(resultCursor.moveToNext()) {
            arraylist.add(resultCursor.getString(resultCursor.getColumnIndexOrThrow(field_name)));
        }
        return arraylist;
    }

}
