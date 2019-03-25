package com.deshario.agriculture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.deshario.agriculture.Config.Deshario_Functions;
import com.deshario.agriculture.Fragments.Category_Root;
import com.deshario.agriculture.Fragments.Record_Latest;
import com.deshario.agriculture.Fragments.Record_Previous;
import com.deshario.agriculture.Models.Category;
import com.deshario.agriculture.Models.Records;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by Deshario on 5/31/2017.
 */

public class AddRecords extends AppCompatActivity implements OnDateSetListener {

    public EditText et_amount,et_categories,et_date,et_shortnote;
    public Button cls_btn,save_btn;
    private FullScreenDialogFragment dialogFragment;
    Calendar now;
    int catg_id,item_id;
    String date_4insert;
    String SQLITE_DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record);
        initializtion_data();

        Deshario_Functions.hideSoftInput(AddRecords.this);

        final String dialogTag = "dialog";
        if (savedInstanceState != null) {
            dialogFragment = (FullScreenDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogTag);
            if (dialogFragment != null) { }
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("รายการใหม่");
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        bind_views();

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        et_date.setText(" "+default_date());
        et_date.setEnabled(false);
        et_date.setFocusable(false);

        et_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deshario_Functions.hideKeyboard(AddRecords.this);
                final Bundle args = new Bundle();
                args.putString(Category_Root.EXTRA_NAME, "test");
                dialogFragment = new FullScreenDialogFragment.Builder(AddRecords.this)
                        .setTitle("เลือกหมวดหมู่")
                        .setConfirmButton("ตกลง")
                        .setOnConfirmListener(new FullScreenDialogFragment.OnConfirmListener() {
                            @Override
                            public void onConfirm(@Nullable Bundle result) {
                                String res = result.getString(Category_Root.SELECTED_ANS);
                                catg_id = result.getInt(Category_Root.SELECTED_CATG); // cat id
                                item_id = result.getInt(Category_Root.ITEM_ID);
                                //Toasty.info(AddRecords.this,res+" : "+catg_id+"\n item_id : "+item_id,Toast.LENGTH_SHORT).show();
                                et_categories.setText(" "+res+" : "+ Category_Root.getTitle(catg_id));

                                et_date.setEnabled(true);
                                et_date.setFocusable(true);
                            }
                        })
                        .setOnDiscardListener(new FullScreenDialogFragment.OnDiscardListener() {
                            @Override
                            public void onDiscard() {
                                //Toast.makeText(AddRecords.this,"onDiscard",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setContent(Category_Root.class, args)
                        .build();

                dialogFragment.show(getSupportFragmentManager(), dialogTag);
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddRecords.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setCancelText("ยกเลิก");
                dpd.setOkText("เลือก");

                // Disable Specific Dates in Calendar
                List<Records> allRecords = Records.getAllRecordsFromCatType(catg_id+1);
                Calendar calendar = null;
                SimpleDateFormat sdf = new SimpleDateFormat(SQLITE_DATE_FORMAT);
                List<Calendar> dates = new ArrayList<>();
                Date date = null;

                for(int i=0; i<allRecords.size(); i++){
                    String used_date = allRecords.get(i).getData_created();
                    //System.out.println("Used Date : "+date);
                    try {
                        date = sdf.parse(used_date);
                        calendar = Deshario_Functions.dateToCalendar(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dates.add(calendar);
                }

                Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
                //dpd.setDisabledDays(disabledDays1);

            }
        });

        cls_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddRecords.this,"RESET",Toast.LENGTH_SHORT).show();
                //myCalendar.clear();
                et_date.setText(" "+default_date());
                et_categories.setText("");
                et_amount.setText("");
                et_shortnote.setText("");
                et_date.setEnabled(false);
                et_date.setFocusable(false);
                // et_date.setText(default_date());

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _amount = et_amount.getText().toString();
                String cat = et_categories.getText().toString();
                String _date = et_date.getText().toString();
                String shortnote = et_shortnote.getText().toString();

                if(_amount.isEmpty() || cat.isEmpty() || _date.isEmpty() || shortnote.isEmpty()){
                    Toasty.info(AddRecords.this,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                }else{
                    Double amount = Double.parseDouble(_amount);
                    String selected_date = date_4insert;
//                    boolean date_taken = Records.isExists_From_Category(selected_date,catg_id+1);
//                    if(date_taken == true){
//                        Toasty.warning(AddRecords.this," วันที่ที่คุณเลือกไม่ว่าง \n กรุณาเลือกวันที่อื่น",Toast.LENGTH_LONG).show();
//                    }else{
                        Category category = Category.getSingleCategory(item_id);
                        Records records = new Records();
                        records.setCategory(category);
                        records.setData_amount(amount);
                        records.setData_created(selected_date);
                        records.setShortnote(shortnote);

//                        System.out.println("getCat_topic :: "+records.getCategory().getCat_topic());
//                        System.out.println("getCat_item :: "+records.getCategory().getCat_item());
//                        System.out.println("getCat_type :: "+records.getCategory().getCat_type());
//                        System.out.println("getData_amount :: "+records.getData_amount());
//                        System.out.println("getData_created :: "+records.getData_created());
//                        System.out.println("getShortnote :: "+records.getShortnote());

                        records.save();

                        //boolean status = Records.check_exists();
                        //if(status == true){
                        cls_btn.performClick();
                        Toasty.success(AddRecords.this,"รายการของคุณถูกบันทึกแล้ว",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        Record_Previous.btn_refresh.performClick();
                        Record_Latest.btn_refresh.performClick();
                        //}
//                    }
                }
            }
        });

    }

    public void bind_views(){
        et_amount = (EditText)findViewById(R.id.amount);
        et_categories = (EditText)findViewById(R.id.categories);
        et_date = (EditText)findViewById(R.id.cur_date);
        et_shortnote = (EditText)findViewById(R.id.shortnote);
        cls_btn = (Button)findViewById(R.id.cleardata);
        save_btn = (Button)findViewById(R.id.savedata);
    }

    public void initializtion_data(){
        boolean exists = new Select()
                .from(Category.class)
                .exists();
        if(exists){
            System.out.println("Data Ready");
        }else{
            System.out.println("Intiliazing Data !");
            //
            ActiveAndroid.beginTransaction();
            try {
                String[] items1 = new String[] {
                        "เงินกู้ยืม ธกส","สิ้นเชือเกษตรกร","กองทุนเงินล้าน","สิ้นเชือ ธ.ออมสิน","เงินกู้ยืมอาคารบ้านเรือน ธกส"
                };
                String[] items2 = new String[] {
                        "ค่าอุปกรณ์การเกษตร",
                        "ค่าสารเคมีกำจัดศัตรูพืช",
                        "ค่าปุ่ยบำรุ่ง",
                        "ค่าเมล็ดพันธุ์",
                        "ค่าแรงงานเพาะปลูก",
                        "ค่าแรงงานเก็บเกียว",
                };
                String[] items3 = new String[] {
                        "ขายกล้วย",
                        "ขายฟางข้าว",
                        "ขายเมล็ดพันธุ์",
                        "ขายต้นกล้า",
                        "ขายข้าวโพด",
                        "ขายผักสวนครัว",
                };

                // Category 1
                for (int i = 0; i < items1.length; i++) {
                    Category category1 = new Category();
                    category1.setCat_topic("เงินกู้/หนี้สิ้น");
                    category1.setCat_item(items1[i]);
                    category1.setCat_type(1);
                    category1.save();
                }
                // Category 2
                for (int i = 0; i < items2.length; i++) {
                    Category category2 = new Category();
                    category2.setCat_topic("ค่าใช้จ่าย");
                    category2.setCat_item(items2[i]);
                    category2.setCat_type(2);
                    category2.save();
                }
                // Category 3
                for (int i = 0; i < items3.length; i++) {
                    Category category3 = new Category();
                    category3.setCat_topic("รายได้");
                    category3.setCat_item(items3[i]);
                    category3.setCat_type(3);
                    category3.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            };
        }
        //
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private String default_date(){
        String thai_date = null;
        Date temp_date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(SQLITE_DATE_FORMAT);
        String stringDate = sdf.format(temp_date);

        String[] output = stringDate.split("-");
        date_4insert = stringDate;
        System.out.println("SQ DATE OK : "+date_4insert);

        int _day = Integer.valueOf(output[2]);
        int _month = Integer.valueOf(output[1]); // jan = 1
        int _year = Integer.valueOf(output[0]);

        int day_ = _day;
        String month_ = Deshario_Functions.Th_Months(_month,false);
        int year_ = Deshario_Functions.Th_Year(_year);
        thai_date = day_+" "+month_+" "+year_;

        return thai_date;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth){
        String month = Deshario_Functions.add_zero_or_not(++monthOfYear);
        String day = Deshario_Functions.add_zero_or_not(dayOfMonth);
        String selected_date = year+"-"+month+"-"+day;
        date_4insert = selected_date;
        int sel_day = dayOfMonth;
        String sel_month = Deshario_Functions.Th_Months(monthOfYear,false);
        int sel_year = Deshario_Functions.Th_Year(year);
        String total_date = sel_day+" "+sel_month+" "+sel_year;
        System.out.println("SQ DATE SET : "+date_4insert);
        et_date.setText(" "+total_date);
    }

}
