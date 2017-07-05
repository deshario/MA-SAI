package com.deshario.agriculture.Fragments;

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
import android.widget.Toast;

import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.deshario.agriculture.R;

/**
 * Created by Deshario on 4/17/2017.
 */

public class Planning_Tab_Frag extends Fragment implements FullScreenDialogContent {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;
    public static int pageno;
    MyAdapter myAdapter;

    public static final String UNIQUE_NAME = "planning_key";
    private FullScreenDialogController dialogController;
    //declare key
    Boolean first = true;

    final int[] ICONS = new int[] {
            R.drawable.ic_trending_up_white_24dp,
            R.drawable.ic_trending_down_white_24dp,
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myview =  inflater.inflate(R.layout.categories_tab_view,null);

        //  Toast.makeText(getActivity(),"Inflate Done",Toast.LENGTH_SHORT).show();
        // ((MainActivity) getActivity()).setActionBarTitle("");
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        viewPager = (ViewPager) myview.findViewById(R.id.viewpager);
        adap();

        return myview;
    }

    public void adap(){

        //Set an Apater for the View Pager
        myAdapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(myAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (first && positionOffset == 0 && positionOffsetPixels == 0){
                    onPageSelected(0);
                    first = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(getActivity(),"Page : "+position,Toast.LENGTH_SHORT).show();
                pageno = position;
                if(position == 0){
                    //Categories1_Frag.resetdata();
                }

                if(position == 1){
                   // Categories2_Frag.resetdata();
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
            }
        });

    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialog) {
        String data = null;
        // Toast.makeText(getActivity(),"PageNo:"+pageno,Toast.LENGTH_SHORT).show();
        if(pageno == 0){
      //      data = Categories1_Frag.adapter.getSelectedItem();
        }
        if(pageno == 1){
         //   data = Categories2_Frag.adapter.getSelectedItem();
        }

        //Toast.makeText(getActivity(),"Page "+pageno+" : "+data,Toast.LENGTH_SHORT).show();
        Bundle result = new Bundle();
        if(data == null) {
            Toast.makeText(getActivity(),"กรุณาเลือกรายการจากหมวดหมู่",Toast.LENGTH_SHORT).show();
        }else{
            //result.putString(SELECTED_ANS, data);
           // result.putInt(SELECTED_CATG, pageno);
           // dialog.confirm(result);
        }
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController){
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
                case 0 : return new Planning1_Frag();
                case 1 : return new Planning2_Frag();

            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public int getItemPosition(Object object) { // Notify changed
            // Causes adapter to reload all Fragments when
            // notifyDataSetChanged is called
            return POSITION_NONE;
        }

        // Returns the title of the tab according to the position.
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :
                    return "รายได้คาดการณ์";
                case 1 :
                    return "ค่าใช้จ่ายคาดการณ์";
            }
            return null;
        }


    }

}


