package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.deshario.agriculture.CustomRangeInputFilter;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.PayDebt;
import com.deshario.agriculture.R;

import java.util.List;

import es.dmoral.toasty.Toasty;


public class PayDebtAdapter extends RecyclerView.Adapter<PayDebtAdapter.DataObjectHolder> {
    private Context context;
    private List<Records> mDataset;
    private String thb = "\u0E3F";

    private TextView item_name;
    private TextView item_amount;
    private TextView item_date;
    EditText amount2pay;
    View positiveAction;

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
        String month_ = Th_Months(_month-1);
        int year_ = Th_Year(_year);
        thai_date = _day+" "+month_+" "+year_;
        return thai_date;
    }

    public void paydebt(final Records record){
        boolean wrapInScrollView = true;
        MaterialDialog debt_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.pay_debt,wrapInScrollView)
                .positiveText("ชำระ")
                .negativeText("ยกเลิก")
                .backgroundColorRes(R.color.default_bootstrap)
                .positiveColorRes(R.color.primary_bootstrap)
                .negativeColorRes(R.color.primary_bootstrap)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String cash = amount2pay.getText().toString();
                        if(cash.isEmpty()){
                            Toasty.info(context,"การชำระหนี้ล้มเหลว",Toast.LENGTH_SHORT).show();
                        }else{
                            double amount = Double.parseDouble(cash);
                            update_debt(record,amount);
                        }
                    }
                })
                .build();

        positiveAction = debt_dialog.getActionButton(DialogAction.POSITIVE);
        item_name = (TextView)debt_dialog.getCustomView().findViewById(R.id.itemname);
        item_amount = (TextView)debt_dialog.getCustomView().findViewById(R.id.debt_amount);
        item_date = (TextView)debt_dialog.getCustomView().findViewById(R.id.debt_date);

        item_name.setText(record.getCategory().getCat_item());
        item_amount.setText("จำนวนเงิน : "+thb+record.getData_amount());
        item_date.setText("วันที่กู้ : "+getThaiDate(record.getData_created()));

        amount2pay = (EditText) debt_dialog.getCustomView().findViewById(R.id.pay_debt);
        amount2pay.setFilters(new InputFilter[]{new CustomRangeInputFilter(1, record.getData_amount())});

        WindowManager.LayoutParams lp = debt_dialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        debt_dialog.getWindow().setAttributes(lp);
        debt_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        debt_dialog.show();
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

        MaterialDialog info_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.debt_payed_inform,true)
                .positiveText("ตกลง")
                .backgroundColorRes(R.color.default_bootstrap)
                .positiveColorRes(R.color.primary_bootstrap)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();
        TextView inform = (TextView)info_dialog.getCustomView().findViewById(R.id.info_item);
        inform.setText(data);
        info_dialog.show();

    }

    public String Th_Months(int month){
        String[] th_months = new String[] {
                "ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."
        };
        return th_months[month];
    }

    public int Th_Year(int en_year){
        return en_year+543;
    }

}