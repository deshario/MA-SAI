package com.deshario.agriculture;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.deshario.agriculture.Fragments.Categories_Tab_Frag;
import com.deshario.agriculture.Fragments.Main_Frag;
import com.deshario.agriculture.Fragments.Main_Today_Frag;
import com.deshario.agriculture.Models.Category;
import com.deshario.agriculture.Models.Records;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.layernet.thaidatetimepicker.time.RadialPickerLayout;
import com.layernet.thaidatetimepicker.time.TimePickerDialog;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.layernet.thaidatetimepicker.date.DatePickerDialog.OnDateSetListener;

/**
 * Created by Deshario on 5/31/2017.
 */

public class AddRecords extends AppCompatActivity implements OnDateSetListener {

    public EditText et_amount,et_categories,et_date,et_shortnote;
    public Button cls_btn,save_btn;
    private FullScreenDialogFragment dialogFragment;
    Calendar now;
    int item_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record);
        initializtion_data();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        final String dialogTag = "dialog";
        if (savedInstanceState != null) {
            dialogFragment = (FullScreenDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogTag);
            if (dialogFragment != null) {
                //records_fulldialog.setOnConfirmListener(this);
                //records_fulldialog.setOnDiscardListener(this);
            }
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("รายการใหม่");
        //getSupportActionBar().setIcon(R.drawable.ic_arrow_back_white_24dp);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        initializtion();

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        et_date.setText(" "+default_date());

        et_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide KeyBoard
                hideKeyboard(AddRecords.this);
                //
                final Bundle args = new Bundle();
                args.putString(Categories_Tab_Frag.EXTRA_NAME, "test");
                dialogFragment = new FullScreenDialogFragment.Builder(AddRecords.this)
                        .setTitle("เลือกหมวดหมู่")
                        .setConfirmButton("ตกลง")
                        .setOnConfirmListener(new FullScreenDialogFragment.OnConfirmListener() {
                            @Override
                            public void onConfirm(@Nullable Bundle result) {
                                String res = result.getString(Categories_Tab_Frag.SELECTED_ANS);
                                int catg = result.getInt(Categories_Tab_Frag.SELECTED_CATG);
                                item_id = result.getInt(Categories_Tab_Frag.ITEM_ID);
                                //Toast.makeText(AddRecords.this,res+" : "+catg+"\n item_id : "+item_id,Toast.LENGTH_SHORT).show();
                                et_categories.setText(" "+res+" : "+ Categories_Tab_Frag.getTitle(catg));
                            }
                        })
                        .setOnDiscardListener(new FullScreenDialogFragment.OnDiscardListener() {
                            @Override
                            public void onDiscard() {
                                //Toast.makeText(AddRecords.this,"onDiscard",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setContent(Categories_Tab_Frag.class, args)
                        .build();

                dialogFragment.show(getSupportFragmentManager(), dialogTag);
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(AddRecords.this,Thaidate.class));
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
            }
        });

//        et_shortnote.setText(note());

        cls_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddRecords.this,"RESET",Toast.LENGTH_SHORT).show();
                //myCalendar.clear();
                et_date.setText(" "+default_date());
                et_categories.setText("");
                et_amount.setText("");
                et_shortnote.setText("");
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
                    Toast.makeText(AddRecords.this,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                }else{
                    Double amount = Double.parseDouble(_amount);
                    Date selected_date = now.getTime();
                    //Toast.makeText(AddRecords.this,"Amount : "+amount+"\nCategory : "+cat+"\nDate : "+_date+"\nShortnote : "+shortnote,Toast.LENGTH_SHORT).show();

                    Category category = Category.getSingleCategory(item_id);
                    Records records = new Records();
                    records.setCategory(category);
                    records.setData_amount(amount);
                    records.setData_recorded(selected_date);
                    records.setShortnote(shortnote);
                    records.save();
                    cls_btn.performClick();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(selected_date);
                    long datetime = calendar.getTimeInMillis();
                    //System.out.println("Date in Long is :: "+datetime);
                    boolean status = Records.check_exists(datetime);
                    if(status == true){
                        Toast.makeText(AddRecords.this,"รายการของคุณถูกบันทึกแล้ว",Toast.LENGTH_SHORT).show();
//                        Main_Today_Frag.swiper.setRefreshing();
                        onBackPressed();
                    }

                }
            }
        });


    }

    public void initializtion(){
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

    public String note(){
        String nowdate = null;
        String strCurrentDate= "2017-06-29";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date newDate = null;
        try {

            DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
//            DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM); // DATE


            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

            String inputDateStr = strCurrentDate;
            Date date_oo = inputFormat.parse(inputDateStr);
            String outputDateStr = today.format(date_oo);
            System.out.println("outputDateStr = "+outputDateStr);

            nowdate = outputDateStr;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nowdate;
    }

    //@Override
    public void onConfirm(@Nullable Bundle result) {
        //fullName.setText(result.getString(SurnameFragment.RESULT_FULL_NAME));
    }

    //@Override
    public void onDiscard() {
        //Toast.makeText(MainActivity.this, R.string.dialog_discarded, Toast.LENGTH_SHORT).show();
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
        Main_Frag.tabBarView.resetFocusOnAllTabs();
    }

    public void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

//    private void updateLabel() {
//        String myFormat = "dd/MM/yy"; //In which you need put here //"MM/dd/yy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//        //DateFormat df_medium = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT); // DATETIME
//        DateFormat mydate = DateFormat.getDateInstance(DateFormat.FULL, Locale.UK); // DATE
//        //DateFormat mydate = DateFormat.getDateInstance(DateFormat.LONG, new Locale("th")); // THAI DATE
//
//        //Toast.makeText(getActivity(),sdf.format(myCalendar.getTime()),Toast.LENGTH_SHORT).show();
//        et_date.setText(sdf.format(myCalendar.getTime()));
//        String data = et_date.getText().toString();
//        et_date.setText(mydate.format(myCalendar.getTime()));
//        et_date.setText(data);
//    }
//
    private String default_date(){
        DateFormat mydate = DateFormat.getDateInstance(DateFormat.LONG, new Locale("th")); // THAI DATE
        now = Calendar.getInstance();
        String date_current;
        now.set(Calendar.YEAR, Th_Year(now.get(Calendar.YEAR)));
//        now.set(Calendar.YEAR, now.get(Calendar.YEAR));
        now.set(Calendar.MONTH, now.get(Calendar.MONTH));
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        date_current = today.format(now.getTime());
        return date_current;
    }

//    @Override
//    public void onClick(View v) {
//        Toast.makeText(AddRecords.this,"HAHAHA",Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, Th_Year(year));
//        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("TH"));
        Date date = new Date();
        et_date.setText(dateFormat.format(now.getTime()));
        String _date_ = et_date.getText().toString();
        et_date.setText(" "+today.format(now.getTime()));
       // Toast.makeText(AddRecords.this,"Date : "+_date_,Toast.LENGTH_SHORT).show(); // use for insert
        //System.out.println("Date : "+dateFormat.format(date)); //2017-06-10 19:43:39
        //System.out.println("Date : "+date); //Sat Jun 10 19:43:39 GMT+07:00 2017
    }

    public String Th_Months(int month){
        String[] th_months = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
        };
        return th_months[month];
    }

    public int Th_Year(int en_year){
        return en_year+543;
    }

}
