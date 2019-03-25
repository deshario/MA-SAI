package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Config.CustomRangeInputFilter;
import com.deshario.agriculture.Config.Deshario_Functions;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.PayDebt;
import com.deshario.agriculture.R;

import java.util.List;

public class PayDebtAdapter extends RecyclerView.Adapter<PayDebtAdapter.DataObjectHolder> {
    private Context context;
    private List<Records> mDataset;
    private String thb = "\u0E3F";


    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon_debt;
        TextView amount;
        TextView label;
        TextView dateTime;
        ImageButton pay_btn;

        public DataObjectHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            icon_debt = (ImageView) itemView.findViewById(R.id.debt_icon);
            amount = (TextView) itemView.findViewById(R.id.amount);
            label = (TextView) itemView.findViewById(R.id.itemname);
            dateTime = (TextView) itemView.findViewById(R.id.date);
            pay_btn = (ImageButton) itemView.findViewById(R.id.pay);
            pay_btn.setOnClickListener(this);
             label.setSelected(true);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Records record  = getItem(getPosition());
            paydebt(record);
        }
    }

    public PayDebtAdapter(List<Records> myDataset, Context context) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_cardview, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.icon_debt.setImageResource(R.drawable.refund);
        holder.amount.setText(thb+mDataset.get(position).getData_amount());
        holder.label.setText(mDataset.get(position).getCategory().getCat_item());
        holder.dateTime.setText(getThaiDate(mDataset.get(position).getData_created()));
    }

    public Records getItem(int position) {
        return mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public String getThaiDate(String date){
        String thai_date = date;
        String[] output = thai_date.split("-");
        int _day = Integer.valueOf(output[2]);
        int _month = Integer.valueOf(output[1]);
        int _year = Integer.valueOf(output[0]);
        String month_ = Deshario_Functions.Th_Months(_month,false);
        int year_ = Deshario_Functions.Th_Year(_year);
        thai_date = _day+" "+month_+" "+year_;
        return thai_date;
    }

    public void paydebt(final Records record){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pay_debt, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView item_name = (TextView)view.findViewById(R.id.itemname);
        TextView item_amount = (TextView)view.findViewById(R.id.debt_amount);
        TextView item_date = (TextView)view.findViewById(R.id.debt_date);

        item_name.setText(record.getCategory().getCat_item());
        item_amount.setText("จำนวนเงิน : "+thb+record.getData_amount());
        item_date.setText("วันที่กู้ : "+getThaiDate(record.getData_created()));

        final EditText amount2pay = (EditText)view.findViewById(R.id.pay_debt);
        amount2pay.setFilters(new InputFilter[]{new CustomRangeInputFilter(1, record.getData_amount())});

        Button btn_cancel = (Button)view.findViewById(R.id.cancel_btn);
        Button btn_pay = (Button)view.findViewById(R.id.pay_btn);
        ImageButton btn_close = (ImageButton)view.findViewById(R.id.close_btn);

        btn_close.setImageDrawable(Deshario_Functions.setTint(
                context.getResources().getDrawable(R.drawable.ic_close_white_36dp),
                context.getResources().getColor(R.color.my_gray)
        ));

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cash = amount2pay.getText().toString();
                if(cash.isEmpty()){
                    Toast.makeText(context,"กรุณาป้อนจำนวนเงิน", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    double amount = Double.parseDouble(cash);
                    update_debt(record,amount);
                }
            }
        });
    }


    public void update_debt(Records record, double amount){
        double remain_debt = record.getData_amount()-amount; // Ex : 500-200 = remain:400
        Records records = Records.load(Records.class,record.getId());
        records.setData_amount(remain_debt);
        records.save();

        new PayDebt().reload();

        String paid = "ชำระเงินจำนวน "+thb+amount+" สำเร็จ";
        String data;
        if(remain_debt <= 0){
            data = paid;
        }else{
            String remain  = "คุณมียอดเงินที่ค้างชำระ "+thb+remain_debt;
            data = paid+"\n\n"+remain;

        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debt_payed_inform, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageButton btn_close = (ImageButton)view.findViewById(R.id.close_btn);
        TextView inform = (TextView)view.findViewById(R.id.info_item);
        inform.setText(data);
        btn_close.setImageDrawable(Deshario_Functions.setTint(
                context.getResources().getDrawable(R.drawable.ic_close_white_36dp),
                context.getResources().getColor(R.color.my_gray)
        ));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}