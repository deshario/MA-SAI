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
import android.view.WindowManager;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.deshario.agriculture.Adapters.GridListAdapter;
import com.deshario.agriculture.Models.Category;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

/**
 * Created by Deshario on 4/17/2017.
 */

public class Categories_Tab_Frag extends Fragment implements FullScreenDialogContent {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    public static int pageno;
    MyAdapter myAdapter;

    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String SELECTED_ANS = "SELECTED_ANS";
    public static final String SELECTED_CATG = "SELECTED_CATG";
    public static final String ITEM_ID = "ITEM_ID";
    private FullScreenDialogController dialogController;
    FullScreenDialogFragment dialogFragment;
    //declare key
    Boolean first = true;

    final int[] ICONS = new int[] {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myview =  inflater.inflate(R.layout.categories_tab_view,null);

//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
                    Categories1_Frag.resetdata();
                }

                if(position == 1){
                    Categories2_Frag.resetdata();
                }

                if(position == 2){
                    Categories3_Frag.resetdata();
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

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialog) {
        String data = null; // Itemname of Category
       // Toast.makeText(getActivity(),"PageNo:"+pageno,Toast.LENGTH_SHORT).show();
        if(pageno == 0){
            data = Categories1_Frag.adapter.getSelectedItem();
        }
        if(pageno == 1){
            data = Categories2_Frag.adapter.getSelectedItem();
        }
        if(pageno == 2){
            data = Categories3_Frag.adapter.getSelectedItem();
        }

        //Toast.makeText(getActivity(),"Page "+pageno+" : "+data,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(),"Data :: ->  "+data,Toast.LENGTH_SHORT).show();

//        Category category = new Category();
//        category = Category.getSingleCategory(data);
//        long main_id = category.getId();
//
//        Long long_id = new Long(main_id);
//        int mainid = long_id.intValue();


        Bundle result = new Bundle();
        if(data == null) {
            Toast.makeText(getActivity(),"กรุณาเลือกรายการจากหมวดหมู่",Toast.LENGTH_SHORT).show();
        }else{

            Category category = new Category();
            category = Category.getSingleCategory(data);
            long main_id = category.getId();

            Long long_id = new Long(main_id);
            int mainid = long_id.intValue();

            result.putString(SELECTED_ANS, data);
            result.putInt(SELECTED_CATG, pageno);
            result.putInt(ITEM_ID, mainid);
            dialog.confirm(result);
        }
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }

    public static String getTitle(int position){
        switch (position){
            case 0 :
                return "เงินกู้/หนี้สิ้น";
            case 1 :
                return "ค่าใช้จ่าย";
            case 2 :
                return "รายได้";
        }
        return "";
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
                case 0 : return new Categories1_Frag();
                case 1 : return new Categories2_Frag();
                case 2 : return new Categories3_Frag();

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
                    return "เงินกู้/หนี้สิ้น";
                case 1 :
                    return "ค่าใช้จ่าย";
                case 2 :
                    return "รายได้";
            }
            return null;
        }


    }

}


