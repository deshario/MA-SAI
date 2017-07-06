package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Fragments.AllRecords;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;

import java.util.List;

public class RecordAdapter extends BaseAdapter {

    private LayoutInflater layoutinflater;
    private List<Records> listStorage;
    private Context context;

    public RecordAdapter(Context context, List<Records> customizedListView) {
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View row;
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            row  = layoutinflater.inflate(R.layout.cardview, parent, false);
//            listViewHolder.screenShot = (ImageView)convertView.findViewById(R.id.screen_shot);
            listViewHolder.date = (TextView)row.findViewById(R.id.titler);
            listViewHolder.desc = (TextView)row.findViewById(R.id.desc);

            row .setTag(listViewHolder);
        }else{
            row = convertView;
            listViewHolder = (ViewHolder)row.getTag();
        }
        row.setId(position);

        listViewHolder.date.setText(AllRecords.getThaiDate(listStorage.get(position).getData_recorded()));
        listViewHolder.desc.setText(listStorage.get(position).getShortnote());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked : "+row.getId(),Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }


    static class ViewHolder{
        ImageView screenShot;
        TextView date_no;
        TextView date;
        TextView desc;
    }
}