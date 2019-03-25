package com.deshario.agriculture.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deshario.agriculture.AddRecords;
import com.deshario.agriculture.Config.Deshario_Functions;
import com.deshario.agriculture.PayDebt;
import com.deshario.agriculture.R;
import com.deshario.agriculture.ReportsManager;
import com.deshario.agriculture.SettingsActivity;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.vk.dev.android.ExpandableBottomTabBar;

import java.util.Calendar;
import java.util.Objects;

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
            R.drawable.left,
            R.drawable.latest,
            R.drawable.predict,
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myview =  inflater.inflate(R.layout.main_tabbar,null);
        context = myview.getContext();
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
                    case 3: // paydebt
                        startActivity(new Intent(getActivity(),PayDebt.class));
                        break;
                    case 4: // settings
                        startActivity(new Intent(getActivity(), SettingsActivity.class));
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void adap(){
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
                        Prediction_Frag.btn_refresh.performClick();
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(ICONS[0]);
                    Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(ICONS[1]);
                    Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(ICONS[2]);
                }
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


