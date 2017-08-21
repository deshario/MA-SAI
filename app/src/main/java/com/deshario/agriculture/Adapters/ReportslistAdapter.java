package com.deshario.agriculture.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Models.ContactInfo;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.util.List;

public class ReportslistAdapter extends RecyclerView.Adapter<ReportslistAdapter.ContactViewHolder> {

    private List<ContactInfo> contactList;


    public ReportslistAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount(){
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.title.setText(ci.getTitle());
        contactViewHolder.description.setText(ci.getDesc());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.charts_list_layout, viewGroup, false);
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
            ContactInfo contactInfo = contactList.get(getPosition());
            String title = contactInfo.getTitle();
            Toast.makeText(v.getContext(),"Title :: "+title,Toast.LENGTH_SHORT).show();
        }
    }
}