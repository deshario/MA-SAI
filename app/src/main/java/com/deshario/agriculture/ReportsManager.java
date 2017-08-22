package com.deshario.agriculture;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.deshario.agriculture.Fragments.Charts_Tab_Frag;
import com.deshario.agriculture.Fragments.Main_Frag;
import com.deshario.agriculture.Fragments.Reports_Tab_Frag;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

import java.util.Calendar;

/**
 * Created by Deshario on 8/21/2017.
 */

public class ReportsManager extends AppCompatActivity {
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Toolbar myToolbar;

    public Toolbar getMyToolbar() {
        return myToolbar;
    }

    public void setMyToolbar(Toolbar myToolbar) {
        this.myToolbar = myToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_charts_tablayout);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new Reports_Tab_Frag());
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" รายงาน");
        getSupportActionBar().setIcon(R.drawable.ic_dashboard_white_24dp);

    }

    @Override
    public void onBackPressed() {
        finish();
        Main_Frag.tabBarView.resetFocusOnAllTabs();
    }
}
