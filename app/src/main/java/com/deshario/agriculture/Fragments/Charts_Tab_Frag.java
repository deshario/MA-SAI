package com.deshario.agriculture.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.deshario.agriculture.R;
import com.vk.dev.android.ExpandableBottomTabBar;

/**
 * Created by Deshario on 4/17/2017.
 */

public class Charts_Tab_Frag extends Fragment implements FullScreenDialogContent {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    public static int page;
    public static int pageno;
    MyAdapter myAdapter;

    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String SELECTED_ANS = "SELECTED_ANS";
    public static final String SELECTED_CATG = "SELECTED_CATG";
    private FullScreenDialogController dialogController;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Fragment frag;

    public static ExpandableBottomTabBar tabBarView;


    final int[] ICONS = new int[] {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myview =  inflater.inflate(R.layout.charts_tab_view,null);

        //  Toast.makeText(getActivity(),"Inflate Done",Toast.LENGTH_SHORT).show();
        // ((MainActivity) getActivity()).setActionBarTitle("");
      //  tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
       viewPager = (ViewPager) myview.findViewById(R.id.viewpager);
        //tabLayout.setBackgroundColor(getResources().getColor(R.color.custom_selected));



      //  adap();
        tabBarView = (ExpandableBottomTabBar)myview.findViewById(R.id.bottom_tabs);
        tabBarView.resetFocusOnAllTabs();
        tabBarView.setSelectedTab(0);
        change_frag(new LineColumn());
        tabBarView.setOnTabClickedListener(new ExpandableBottomTabBar.OnTabClickedListener() {
            @Override
            public void onTabClicked(View view, int tabPos) {
                switch (tabPos) {
                    case 0:
                        frag = new LineColumn();
                        //Toast.makeText(getActivity(),"Page 0",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
//                        frag = new LineChart();
                        frag = new LineColumn();
                        //Toast.makeText(getActivity(),"Page 1",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
//                        frag = new EmptyFragment();
                        frag = new LineColumn();
                        //Toast.makeText(getActivity(),"Page 2",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                change_frag(frag);
            }
        });


        return myview;
    }

    public void change_frag(Fragment fragment){
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.hello,fragment);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    public void adap(){
        //Set an Apater for the View Pager
        myAdapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(myAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                pageno = position;
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

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialog) {
//        String data = Categories1_Frag.selected;
//        Bundle result = new Bundle();
//        if(data == null) {
//            Toast.makeText(getActivity(),"กรุณาเลือกรายการจากหมวดหมู่",Toast.LENGTH_SHORT).show();
//        }else{
//            result.putString(SELECTED_ANS, data);
//            result.putInt(SELECTED_CATG, pageno);
//            dialog.confirm(result);
//        }
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        Main_Frag.tabBarView.resetFocusOnAllTabs();
        return false;
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
                case 0 : return new LineChart();
                case 1 : return new LineColumn();
                case 2 : return new EmptyFragment();

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
                    return "ค่าใช้จ่าย";
                case 1 :
                    return "รายได้";
                case 2 :
                    return "ค่าตอบแทน";
            }
            return null;
        }


    }

}


