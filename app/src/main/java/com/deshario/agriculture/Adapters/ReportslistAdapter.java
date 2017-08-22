package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Fragments.Categories2_Frag;
import com.deshario.agriculture.Fragments.ExpenseChart;
import com.deshario.agriculture.Fragments.IncomeChart;
import com.deshario.agriculture.Fragments.Reports_Tab_Frag;
import com.deshario.agriculture.Models.ChartsTypes;
import com.deshario.agriculture.R;

import java.util.List;


public class ReportslistAdapter extends RecyclerView.Adapter<ReportslistAdapter.ContactViewHolder> {

    private List<ChartsTypes> contactList;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Fragment frag;
    Context mContext;

    public ReportslistAdapter(List<ChartsTypes> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount(){
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ChartsTypes ci = contactList.get(i);
        contactViewHolder.title.setText(ci.getTitle());
        contactViewHolder.description.setText(ci.getDesc());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.charts_list_layout, viewGroup, false);
        mContext = viewGroup.getContext();
        return new ContactViewHolder(itemView);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView description;

        public ContactViewHolder(View view) {
            super(view);
            title =  (TextView)view.findViewById(R.id.title);
            description = (TextView)view.findViewById(R.id.desc);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ChartsTypes chartsTypes = contactList.get(getPosition());
            int id = chartsTypes.getId();
            String title = chartsTypes.getTitle();
            Toast.makeText(v.getContext(),"Page :: "+ Reports_Tab_Frag.page+"\nID :: "+id+"\nTitle :: "+title,Toast.LENGTH_SHORT).show();
            switch (Reports_Tab_Frag.page){
                case 0:
                    frag = new ExpenseChart();
                case 1:
                    frag = new Categories2_Frag();
                case 2:
                    frag = new IncomeChart();
                default:
            }
            change_frag(frag);
        }
        public void change_frag(Fragment fragment){
            mFragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            //FragmentTransaction mFragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.containerView,fragment);
            mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            mFragmentTransaction.addToBackStack("fragback");
            mFragmentTransaction.commit();
        }

    }
}