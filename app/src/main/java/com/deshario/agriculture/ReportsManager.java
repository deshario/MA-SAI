package com.deshario.agriculture;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
    ImageView imgToolIcon;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_charts_tablayout);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mFragmentTransaction.replace(R.id.containerView, new Reports_Tab_Frag());
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        imgToolIcon = (ImageView) myToolbar.findViewById(R.id.toolbar_icon);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setTitle(" รายงาน");
//        getSupportActionBar().setIcon(R.drawable.ic_dashboard_white_24dp);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Main_Frag.tabBarView.resetFocusOnAllTabs();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1){
                 finish();
            }else{
                mFragmentManager.popBackStackImmediate();
                resetTolbar();
                Reports_Tab_Frag.tabLayout.setBackgroundColor(getResources().getColor(R.color.success_bootstrap));
            }
        } else {
            super.onBackPressed();
        }
    }

    public void resetTolbar(){
        imgToolIcon.setImageResource(R.drawable.ic_timeline_white_24dp);
    }



}
