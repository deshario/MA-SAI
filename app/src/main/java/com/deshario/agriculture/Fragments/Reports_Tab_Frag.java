package com.deshario.agriculture.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.vk.dev.android.ExpandableBottomTabBar;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reports_Tab_Frag extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    public static int page;
    Reports_Tab_Frag.MyAdapter myAdapter;
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



    public Reports_Tab_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports_tab, container, false);
        context = view.getContext();
//        System.out.println("Infalte ok");
//        Toast.makeText(getActivity(),"Inflate Done",Toast.LENGTH_SHORT).show();
        //((Main_Tab) getActivity()).setActionBarTitle("title");
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adap();
        return view;
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
                case 0 : return new Charts_Income_Lists_Frag();
                case 1 : return new Charts_Expense_Lists_Frag();
                case 2 : return new Charts_Compensation_Lists_Frag();

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
                    return "รายได้";
                case 1 :
                    return "ค่าใช้จ่าย";
                case 2 :
                    return "ค่าตอบแทน";
            }
            return null;
        }
    }

}
