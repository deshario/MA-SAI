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

import com.deshario.agriculture.Models.Category;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

import es.dmoral.toasty.Toasty;

/**
 * Created by Deshario on 4/17/2017.
 */

public class Category_Root extends Fragment implements FullScreenDialogContent {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public static int int_items = 3 ;
    public static int pageno;
    MyAdapter myAdapter;

    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String SELECTED_ANS = "SELECTED_ANS";
    public static final String SELECTED_CATG = "SELECTED_CATG";
    public static final String ITEM_ID = "ITEM_ID";
    private FullScreenDialogController dialogController;
    FullScreenDialogFragment dialogFragment;

    Boolean first = true;

    final int[] ICONS = new int[] {
            R.drawable.debt,
            R.drawable.decrease,
            R.drawable.income_icon
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview =  inflater.inflate(R.layout.categories_tab_view,null);
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        viewPager = (ViewPager) myview.findViewById(R.id.viewpager);
        adap();
        return myview;
    }

    public void adap(){
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
                    CategoryDebtLoan.resetdata();
                }

                if(position == 1){
                    CategoryExpense.resetdata();
                }

                if(position == 2){
                    CategoryIncome.resetdata();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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
            data = CategoryDebtLoan.adapter.getSelectedItem();
        }
        if(pageno == 1){
            data = CategoryExpense.adapter.getSelectedItem();
        }
        if(pageno == 2){
            data = CategoryIncome.adapter.getSelectedItem();
        }

        Bundle result = new Bundle();
        if(data == null) {
            Toasty.info(getActivity(), "กรุณาเลือกรายการจากหมวดหมู่", Toast.LENGTH_SHORT).show();
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

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new CategoryDebtLoan();
                case 1 : return new CategoryExpense();
                case 2 : return new CategoryIncome();

            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public int getItemPosition(Object object) {
            // Notify changed
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


