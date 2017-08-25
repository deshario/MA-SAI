package com.deshario.agriculture.Models;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
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

    @Column(name = "data_created")
    private String data_created;

    @Column(name = "data_updated")
    private String data_updated;

    @Column(name = "shortnote")
    private String shortnote;

    public Records() {
        super();
    }

    public Records(double data_amount, Category category, String data_created, String data_updated, String shortnote) {
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

    public String getData_created() {
        return data_created;
    }

    public void setData_created(String data_created) {
        this.data_created = data_created;
    }

    public String getData_updated() {
        return data_updated;
    }

    public void setData_updated(String data_updated) {
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

    public static boolean records_exists(String date) {
        return new Select()
                .from(Records.class)
                .where("data_created = ?",date)
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

    public static boolean previous_records_exists(String latestdate) {
        //select * from Records where datetime("data_created") < datetime("2017-08-11") order by data_created desc limit 1;
        return new Select()
                .from(Records.class)
                //.where("datetime('data_created') < datetime('?')",latestdate)
                .where("data_created < ?",latestdate)
                .orderBy("data_created DESC")
                .limit(1)
                .exists();
    }

    public static Records getPreviousRecord(String latestdate){
        //select * from Records where data_created < 1499792400000 order by data_created desc limit 1;
        Records record = new Select()
                .from(Records.class)
                //.where("datetime('data_created') < ?",latestdate)
                .where("data_created < ?",latestdate)
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

    public static boolean check_exists(Records records) {
        return new Select()
                .from(Records.class)
                .where("Id = ?", records.getId())
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

    public static List<Records> getDataBy_date_n_Type(String date, int catg_type){
        // Ex : date = '2017-09' && catg_type = 1
        List<Records> records = new ArrayList<>();
        String deshario = "SELECT * FROM Records JOIN Categories ON Records.category_id = Categories.Id " +
                "WHERE strftime('%Y-%m', data_created) = '"+date+"' AND Categories.cat_type = "+catg_type+" ORDER BY data_created ASC";
        Cursor resultCursor = Cache.openDatabase().rawQuery(deshario, null);
        while(resultCursor.moveToNext()) {
            Records found_records = new Records();
            Category category = new Category();
            category.setCat_topic(resultCursor.getString(resultCursor.getColumnIndexOrThrow("cat_topic")));
            category.setCat_item(resultCursor.getString(resultCursor.getColumnIndexOrThrow("cat_item")));
            category.setCat_type(resultCursor.getInt(resultCursor.getColumnIndexOrThrow("cat_type")));

            found_records.setData_amount(resultCursor.getDouble(resultCursor.getColumnIndexOrThrow("data_amount")));
            found_records.setShortnote(resultCursor.getString(resultCursor.getColumnIndexOrThrow("shortnote")));
            found_records.setData_created(resultCursor.getString(resultCursor.getColumnIndexOrThrow("data_created")));
            found_records.setCategory(category);
            records.add(found_records);
        }
        return records;
    }


    public static List<Records> getDataBetweenDays(String firstdate, String lastdate, int catg_type){
        List<Records> records = new ArrayList<>();
//        String deshario = "SELECT * FROM Records JOIN Categories ON Records.category_id = Categories.Id " +
//                "WHERE data_created BETWEEN datetime('now', '-8 days') AND datetime('now', 'localtime') " +
//                "AND Categories.cat_type = "+catg_type;
        String deshario = "SELECT * FROM Records JOIN Categories ON Records.category_id = Categories.Id " +
                "WHERE data_created BETWEEN '"+firstdate+"' AND '"+lastdate+"' AND Categories.cat_type = "+catg_type;
        Cursor resultCursor = Cache.openDatabase().rawQuery(deshario, null);
        while(resultCursor.moveToNext()) {
            Records found_records = new Records();
            Category category = new Category();
            category.setCat_topic(resultCursor.getString(resultCursor.getColumnIndexOrThrow("cat_topic")));
            category.setCat_item(resultCursor.getString(resultCursor.getColumnIndexOrThrow("cat_item")));
            category.setCat_type(resultCursor.getInt(resultCursor.getColumnIndexOrThrow("cat_type")));

            found_records.setData_amount(resultCursor.getDouble(resultCursor.getColumnIndexOrThrow("data_amount")));
            found_records.setShortnote(resultCursor.getString(resultCursor.getColumnIndexOrThrow("shortnote")));
            found_records.setData_created(resultCursor.getString(resultCursor.getColumnIndexOrThrow("data_created")));
            found_records.setCategory(category);
            records.add(found_records);
        }
        return records;
    }

    public static List<Records> getDataBetweenMonths(String firstdate, String lastdate, int catg_type){
        List<Records> records = new ArrayList<>();
                //SELECT * FROM Records WHERE strftime('%Y-%m', data_created) between '2017-05' and '2017-07'
        String deshario = "SELECT * FROM Records JOIN Categories ON Records.category_id = Categories.Id " +
                "WHERE strftime('%Y-%m', data_created) BETWEEN '"+firstdate+"' AND '"+lastdate+"' AND Categories.cat_type = "+catg_type;
        Cursor resultCursor = Cache.openDatabase().rawQuery(deshario, null);
        while(resultCursor.moveToNext()) {
            Records found_records = new Records();
            Category category = new Category();
            category.setCat_topic(resultCursor.getString(resultCursor.getColumnIndexOrThrow("cat_topic")));
            category.setCat_item(resultCursor.getString(resultCursor.getColumnIndexOrThrow("cat_item")));
            category.setCat_type(resultCursor.getInt(resultCursor.getColumnIndexOrThrow("cat_type")));

            found_records.setData_amount(resultCursor.getDouble(resultCursor.getColumnIndexOrThrow("data_amount")));
            found_records.setShortnote(resultCursor.getString(resultCursor.getColumnIndexOrThrow("shortnote")));
            found_records.setData_created(resultCursor.getString(resultCursor.getColumnIndexOrThrow("data_created")));
            found_records.setCategory(category);
            records.add(found_records);
        }
        return records;
    }

    public static Records getSingleRecordsByDate(String date){
        return new Select()
                .from(Records.class)
                .innerJoin(Category.class)
                .on("Records.category_id = Categories.Id")
                .where("data_created = ?",date)
                .orderBy("data_created ASC")
                .executeSingle();
    }

}
