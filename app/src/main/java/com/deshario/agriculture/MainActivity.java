package com.deshario.agriculture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.deshario.agriculture.Config.Deshario_Functions;
import com.deshario.agriculture.Fragments.AllRecords;
import com.deshario.agriculture.Fragments.Main_Frag;
import com.deshario.agriculture.Fragments.Plan_Root;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.layernet.thaidatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by Deshario on 5/28/2017.
 */

public class MainActivity extends AppCompatActivity implements OnDateSetListener{
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    public FullScreenDialogFragment records_fulldialog;
    Calendar manual;
    String dialogTag;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        sharedPreference = new SharedPreference(this);
        if (sharedPreference.isInitialLaunch()) {
            finish();
            startActivity(new Intent(this,UserLogin.class));
        }

        dialogTag = "dialog";
        if (savedInstanceState != null) {
            records_fulldialog = (FullScreenDialogFragment)getSupportFragmentManager().findFragmentByTag(dialogTag);
            if (records_fulldialog != null) {

            }
        }

        if(savedInstanceState == null) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.containerView, new Main_Frag());
            mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.commit();
        }else{
            Toast.makeText(MainActivity.this, "savedInstanceState not = null", Toast.LENGTH_SHORT).show();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" Smart ROI For Smart Farmer");
        getSupportActionBar().setIcon(R.drawable.ic_dashboard_white_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.layers_icon){
            alldata();
        }
        return super.onOptionsItemSelected(item);
    }

    private void alldata() {
        final Bundle plan_args = new Bundle();
        plan_args.putString(Plan_Root.UNIQUE_NAME, "planning_key");
        records_fulldialog = new FullScreenDialogFragment.Builder(MainActivity.this)
                .setTitle("รายการทั้งหมด")
                .setContent(AllRecords.class, plan_args)
                .build();
        records_fulldialog.show(getSupportFragmentManager(), dialogTag);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        Main_Frag.tabBarView.resetFocusOnAllTabs();

        if (Main_Frag.dialogFragment != null && Main_Frag.dialogFragment.isAdded()) {
            Main_Frag.dialogFragment.onBackPressed();
        }else if(records_fulldialog != null && records_fulldialog.isAdded()){
            records_fulldialog.onBackPressed();
        } else {
            if (doubleBackToExitPressedOnce) {
                //super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toasty.warning(this, "กดอีกครั้งเพื่อออกจากโปรแกรม", Toast.LENGTH_SHORT,true).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        manual.set(Calendar.YEAR, Deshario_Functions.Th_Year(year));
        manual.set(Calendar.MONTH, monthOfYear);
        manual.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat thai = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("TH"));
        Date date = manual.getTime();
        Toasty.normal(MainActivity.this, "วันที่ : " + thai.format(date), Toast.LENGTH_SHORT).show(); // use for insert
    }

    @Override
    protected void onResume() {
        if (sharedPreference.isResetPassword()) {
            finish();
            startActivity(new Intent(this,UserLogin.class));
        }
        super.onResume();
    }
}