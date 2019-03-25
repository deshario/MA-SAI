package com.deshario.agriculture.Fragments;


import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deshario.agriculture.Config.Deshario_Functions;
import com.deshario.agriculture.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Charts_Root extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;
    Charts_Root.MyAdapter myAdapter;
    Context context;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();


    public Charts_Root(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports_tab, container, false);
        context = view.getContext();
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adap();
        return view;
    }


    public void adap(){
        Drawable incomeIcon = getResources().getDrawable(R.drawable.income_icon);
        Drawable expenseIcon = getResources().getDrawable(R.drawable.decrease);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) { // Cloning by not reference
            incomeIcon = Objects.requireNonNull(getResources().getDrawable(R.drawable.income_icon).getConstantState()).newDrawable().mutate();
            expenseIcon = Objects.requireNonNull(getResources().getDrawable(R.drawable.decrease).getConstantState()).newDrawable().mutate();
        }
        final Drawable income_icon = Deshario_Functions.setTint(incomeIcon,Color.WHITE);
        final Drawable decrease_icon = Deshario_Functions.setTint(incomeIcon,Color.WHITE);
        final Drawable other_icon = Deshario_Functions.setTint(expenseIcon,Color.WHITE);
        //Set an Apater for the View Pager
        myAdapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(myAdapter);
        setUpColors();
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        //viewPager.setCurrentItem(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //if (first && positionOffset == 0 && positionOffsetPixels == 0){
                //   onPageSelected(1); // default must be 0 || lefttab is 0 so...
                //   first = false;
                //}
                if(position < (myAdapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                    tabLayout.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
                    toolbar.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

                }else{
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position){
//                Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
//                switch (position){
//                    case 0:
//                        tabLayout.setBackgroundColor(getResources().getColor(R.color.primary_deshario));
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.primary_deshario));
////                        Charts_Income.changecolor(getResources().getColor(R.color.primary_deshario));
//                        break;
//                    case 1:
//                        tabLayout.setBackgroundColor(getResources().getColor(R.color.material_red));
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_red));
////                        Charts_Expense.changecolor(getResources().getColor(R.color.material_red));
//                        break;
//                    case 2:
//                        tabLayout.setBackgroundColor(getResources().getColor(R.color.success_bootstrap));
//                        toolbar.setBackgroundColor(getResources().getColor(R.color.success_bootstrap));
////                        Charts_Compensation.changecolor(getResources().getColor(R.color.success_bootstrap));
//                        break;
//                    default:
//                }
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
                tabLayout.getTabAt(0).setIcon(income_icon);
                tabLayout.getTabAt(1).setIcon(decrease_icon);
                tabLayout.getTabAt(2).setIcon(other_icon);
            }
        });

    }
    public static int getPage(){
        return viewPager.getCurrentItem();
    }

    private void setUpColors(){
        Integer color1 = getResources().getColor(R.color.material_primary);
        Integer color2 = getResources().getColor(R.color.deep_orange);
        Integer color3 = getResources().getColor(R.color.light_success);
        Integer[] colors_temp = {color1, color2, color3};
        colors = colors_temp;
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
                case 0 : return new Charts_Income();
                case 1 : return new Charts_Expense();
                case 2 : return new Charts_Compensation();

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
