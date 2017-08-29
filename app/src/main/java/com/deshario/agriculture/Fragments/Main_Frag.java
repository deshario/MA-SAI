package com.deshario.agriculture.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.AddRecords;
import com.deshario.agriculture.Deshario_Functions;
import com.deshario.agriculture.Main_Tab;
import com.deshario.agriculture.PayDebt;
import com.deshario.agriculture.ReportsManager;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.vk.dev.android.ExpandableBottomTabBar;
import com.deshario.agriculture.R;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

/**
 * Created by Deshario on 4/17/2017.
 */

public class Main_Frag extends Fragment {


    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    public static int page;
    MyAdapter myAdapter;
    public static ExpandableBottomTabBar tabBarView;
    public static FullScreenDialogFragment dialogFragment;
    Boolean first = true;
    Calendar manual;
    Context context;

    final int[] ICONS = new int[] {
//            R.mipmap.ic_launcher,
            R.drawable.left,
            R.drawable.latest,
            R.drawable.predict,
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myview =  inflater.inflate(R.layout.main_tabbar,null);
        context = myview.getContext();
//        System.out.println("Infalte ok");
//        Toast.makeText(getActivity(),"Inflate Done",Toast.LENGTH_SHORT).show();
        //((Main_Tab) getActivity()).setActionBarTitle("title");
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        viewPager = (ViewPager) myview.findViewById(R.id.viewpager);

        final String dialogTag = "dialog";
        if (savedInstanceState != null) {
            dialogFragment = (FullScreenDialogFragment) getFragmentManager().findFragmentByTag(dialogTag);
            if (dialogFragment != null) {
                //records_fulldialog.setOnConfirmListener(this);
                //records_fulldialog.setOnDiscardListener(this);
            }
        }


        adap();

        tabBarView = (ExpandableBottomTabBar)myview.findViewById(R.id.bottom_tabs);
        //tabBarView.setEnabled(false);
        //tabBarView.setSelected(false);
        tabBarView.resetFocusOnAllTabs();



        tabBarView.setOnTabClickedListener(new ExpandableBottomTabBar.OnTabClickedListener() {
            @Override
            public void onTabClicked(View view, int pos) {
                tabBarView.resetFocusOnAllTabs();
                switch (pos){
                    case 0:
                        startActivity(new Intent(getActivity(), ReportsManager.class));
                        break;
                    case 1:
                        Intent intent = new Intent(getActivity(),AddRecords.class);
                        startActivity(intent);
                        break;
                    case  2:
                        final Bundle plan_args = new Bundle();
                        plan_args.putString(Planning_Tab_Frag.UNIQUE_NAME, "planning_key");
                        dialogFragment = new FullScreenDialogFragment.Builder(getActivity())
                                .setTitle("การวางแผน")
                                //.setConfirmButton("Confirm")
                                .setContent(Planning_Tab_Frag.class, plan_args)
                                .build();

                        dialogFragment.show(getFragmentManager(), dialogTag);
                        break;
                    case 3:
                        final Bundle profile_args = new Bundle();
                        profile_args.putString(Profile_Frag.UNIQUE_NAME, "profile_key");
                        dialogFragment = new FullScreenDialogFragment.Builder(getActivity())
                                .setTitle("ข้อมูลส่วนตัว")
                                //.setConfirmButton("ตกลง")
                                .setContent(Profile_Frag.class, profile_args)
                                .build();

                        dialogFragment.show(getFragmentManager(), dialogTag);
                        break;
                    case 4: // More Button
                        tabBarView.resetFocusOnAllTabs();
                        break;
                    case 5: // paydebt
                        startActivity(new Intent(getActivity(),PayDebt.class));
                        break;
                    case 6: // calendar
                        break;
                    case 7:
                        about_application();
                        break;
                    case 8:
                        getActivity().finish();
                        break;
                    default:
                }
            }
        });
        return myview;
    }

    @Override
    public void onResume() {
        tabBarView.resetFocusOnAllTabs();
        Deshario_Functions.setTint(
                getResources().getDrawable(R.drawable.ic_settings_white_24dp),
                getResources().getColor(R.color.default_bootstrap));
        super.onResume();
    }

    public void about_application(){
// api 16
//        Dialog alertDialog = new Dialog(context);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.setContentView(R.layout.about_app);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.show();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.about_app, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(view);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageButton imageButton = (ImageButton)view.findViewById(R.id.close_modal);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tabBarView.resetFocusOnAllTabs();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void adap(){
        //Set an Apater for the View Pager
        myAdapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(myAdapter);
        viewPager.setCurrentItem(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //if (first && positionOffset == 0 && positionOffsetPixels == 0){
                 //   onPageSelected(1); // default must be 0 || lefttab is 0 so...
                 //   first = false;
                //}
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        Previous_Record_Frag.btn_refresh.performClick();
                        break;
                    case 1:
                        Latest_Record_Frag.btn_refresh.performClick();
                        break;
                    case 2:
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        // SetupWithViewPager doesn't works without the runnable
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.getTabAt(0).setIcon(ICONS[0]);
                tabLayout.getTabAt(1).setIcon(ICONS[1]);
                tabLayout.getTabAt(2).setIcon(ICONS[2]);
            }
        });

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        // Return fragment with respect to Position
        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new Previous_Record_Frag();
                case 1 : return new Latest_Record_Frag();
                case 2 : return new Prediction_Frag();

            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        // Returns the title of the tab according to the position.
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :
                    return "ก่อนหน้า";
                case 1 :
                    return "ล่าสุด";
                case 2 :
                    return "คาดการณ์";
            }
            return null;
        }
    }

}


