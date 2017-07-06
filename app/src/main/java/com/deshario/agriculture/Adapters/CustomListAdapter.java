package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class CustomListAdapter extends SimpleAdapter

public class CustomListAdapter extends SimpleAdapter {
    public CustomListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=super.getView(position, convertView, parent);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView)view.findViewById(R.id.itemname);
        TextView amount = (TextView)view.findViewById(R.id.amount);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, arrayList.get(position).get("name"), Toast.LENGTH_SHORT).show();
//            }
//        });
        return super.getView(position, convertView, parent);

    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
