package com.deshario.agriculture.Models;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deshario on 7/2/2017.
 */

@Table(name = "Categories")
public class Category extends Model {

    @Column(name = "cat_topic")
    public String cat_topic;

    @Column(name = "cat_item")
    public String cat_item;

    @Column(name = "cat_type")
    public int cat_type;

    public static int CATEGORY_DEBTS = 1;
    public static int CATEGORY_EXPENSE = 2;
    public static int CATEGORY_INCOME = 3;

    public String getCat_topic() {
        return cat_topic;
    }

    public void setCat_topic(String cat_topic) {
        this.cat_topic = cat_topic;
    }

    public String getCat_item() {
        return cat_item;
    }

    public void setCat_item(String cat_item) {
        this.cat_item = cat_item;
    }

    public int getCat_type() {
        return cat_type;
    }

    public void setCat_type(int cat_type) {
        this.cat_type = cat_type;
    }

    public Category(){
        super();
    }

    public Category(String cat_topic, String cat_item, int cat_type) {
        super();
        this.cat_topic = cat_topic;
        this.cat_item = cat_item;
        this.cat_type = cat_type;
    }

     /*
        || ======================= Database Functions ======================= ||
    */

    // Used to return items from another table based on the foreign key
    public List<Records> records() {
        return getMany(Records.class, "Category");
    }

    public static List<Category> getAllCategory(){
        return new Select().from(Category.class).execute();
    }

    public static ArrayList<String> getItemsbyTopic(String field_name){
        ArrayList<String> arraylist = new ArrayList<String>();
        String resultRecords = new Select().from(Category.class).toSql();
        Cursor resultCursor = Cache.openDatabase().rawQuery(resultRecords, null);
        while(resultCursor.moveToNext()) {
            arraylist.add(resultCursor.getString(resultCursor.getColumnIndexOrThrow(field_name)));
        }
        return arraylist;
    }

    public static Category getSingleCategory(int id){
        Category category = new Select()
                .from(Category.class) // Specify the table to search
                .where("Id = ?", id) // search criteria
                .executeSingle(); // return only the first match
        return category;
    }

    public static Category getSingleCategory(String item){
        Category category = new Select()
                .from(Category.class)
                .where("cat_item = ?", item)
                .executeSingle();
        return category;
    }

    public static List<Category> getItembyTopic(int type){
        return new Select().from(Category.class).where("cat_type = ?", type).execute();
    }

    public static List<Category> getCategoryType(){
        return new Select().from(Category.class).where("cat_type = ?", 1).execute();
    }

    public static boolean check_exists(String itemname){
       return new Select()
                .from(Category.class)
                .where("cat_item = ?", itemname)
                .exists();
    }


}
