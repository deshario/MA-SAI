package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class RecordAdapter extends BaseAdapter {

    private LayoutInflater layoutinflater;
    private List<Records> listStorage;
    private Context context;
    private String thb = "\u0E3F";
    private TextView txt_amount,txt_date,txt_note,txt_cattopic,txt_catitem;
    private EditText et_amount,et_note;
    private TextView name_category;
    private FullScreenDialogFragment dialogFragment;
    private String str_amount=null, str_note=null;
    int data_position;
    View positive_button;

    public RecordAdapter(Context context, List<Records> customizedListView) {
        this.context = context;
        layoutinflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Records getItem(int position) {
        return listStorage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        ImageView img;
        TextView title;
        TextView date;
        TextView note;
        View line;
        LinearLayout ln;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View row;
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            row  = layoutinflater.inflate(R.layout.cardview, parent, false);
            listViewHolder.ln = (LinearLayout) row.findViewById(R.id.linear_border);
            listViewHolder.line = (View) row.findViewById(R.id.line_view);
            listViewHolder.img = (ImageView)row.findViewById(R.id.img_icon);
            listViewHolder.title = (TextView)row.findViewById(R.id.item_title);
            listViewHolder.date = (TextView)row.findViewById(R.id.titler);
            listViewHolder.note = (TextView)row.findViewById(R.id.desc);

            row .setTag(listViewHolder);
        }else{
            row = convertView;
            listViewHolder = (ViewHolder)row.getTag();
        }

        row.setId(position);

        int type = listStorage.get(position).getCategory().getCat_type();
        switch (type){
            case 1: // debt
                listViewHolder.title.setText("หนี้สิน");
                listViewHolder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.danger_bootstrap));
                listViewHolder.img.setImageResource(R.drawable.debt);
                listViewHolder.ln.setBackground(ContextCompat.getDrawable(context,R.drawable.linear_danger_border));
                listViewHolder.line.setBackgroundColor(ContextCompat.getColor(context,R.color.danger_bootstrap));
                break;
            case 2: // expense
                listViewHolder.title.setText("ค่าใช้จ่าย");
                listViewHolder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.primary_deshario));
                listViewHolder.img.setImageResource(R.drawable.refund);
                listViewHolder.ln.setBackground(ContextCompat.getDrawable(context,R.drawable.linear_primary_border));
                listViewHolder.line.setBackgroundColor(ContextCompat.getColor(context,R.color.primary_deshario));
                break;
            case 3: // income
                listViewHolder.title.setText("รายได้");
                listViewHolder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.success_bootstrap));
                listViewHolder.img.setImageResource(R.drawable.wallet);
                listViewHolder.ln.setBackground(ContextCompat.getDrawable(context,R.drawable.linear_success_border));
                listViewHolder.line.setBackgroundColor(ContextCompat.getColor(context,R.color.success_bootstrap));
                break;
            default:
        }

        listViewHolder.date.setText(getThaiDate(listStorage.get(position).getData_created()));
        listViewHolder.note.setText(listStorage.get(position).getShortnote());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Records record = getItem(row.getId());
                data_position = row.getId();
                view_data(record);
            }
        });

        return row;
    }

    public void view_data(final Records record){
//        MaterialDialog dialog = new MaterialDialog.Builder(context)
//                .title("ตัวเลือก")
//                .items(R.array.crud_options)
//                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
//                    @Override
//                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                        int position = which;
//                        switch (position){
//                            case -1:
//                                Toasty.info(context,"select atleast one!",Toast.LENGTH_SHORT).show();
//                                break;
//                            case 0:
//                                do_view(record);
//                                break;
//                            case 1:
//                                do_update(record);
//                                break;
//                            case 2:
//                                do_delete(record);
//                                break;
//                            default:
//                        }
//                        return true;
//                    }
//                })
//                .positiveText("เลือก")
//                .build();
//
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.dimAmount=0.5f;
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//        dialog.show();
    }

    public void do_view(Records record){
//        String amount = "จำนวนเงิน : "+thb+record.getData_amount();
//        String rec_date = "วันที่ : "+getThaiDate(record.getData_created());
//        String note = "บันทึกย่อ : "+record.getShortnote();
//        String category_topic = "หมวดหมู่ : "+record.getCategory().getCat_topic();
//        String category_item = "ชือรายการ : "+record.getCategory().getCat_item();
//        boolean wrapInScrollView = true;
//        MaterialDialog viewer_dialog = new MaterialDialog.Builder(context)
//                .customView(R.layout.view_records,wrapInScrollView)
//                .positiveText("ตกลง")
//                .backgroundColorRes(R.color.default_bootstrap)
//                .positiveColorRes(R.color.primary_bootstrap)
//                .negativeColorRes(R.color.primary_bootstrap)
//                .build();
//        txt_cattopic = (TextView)viewer_dialog.getCustomView().findViewById(R.id.cat_topic);
//        txt_amount = (TextView)viewer_dialog.getCustomView().findViewById(R.id.data_amount);
//        txt_date = (TextView)viewer_dialog.getCustomView().findViewById(R.id.data_date);
//        txt_note = (TextView)viewer_dialog.getCustomView().findViewById(R.id.note);
//        txt_catitem = (TextView)viewer_dialog.getCustomView().findViewById(R.id.category);
//
//        txt_cattopic.setText(category_topic);
//        txt_amount.setText(amount);
//        txt_date.setText(rec_date);
//        txt_note.setText(note);
//        txt_catitem.setText(category_item);
//
//        WindowManager.LayoutParams lp = viewer_dialog.getWindow().getAttributes();
//        lp.dimAmount=1f;
//        viewer_dialog.getWindow().setAttributes(lp);
//        viewer_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//        viewer_dialog.show();
//        viewer_dialog.show();
    }

    public void do_update(final Records record){
//        boolean wrapInScrollView = true;
//        MaterialDialog update_dialog = new MaterialDialog.Builder(context)
//                .customView(R.layout.update_records,wrapInScrollView)
//                .positiveText("ปรับปรุง")
//                .negativeText("ยกเลิก")
//                .backgroundColorRes(R.color.default_bootstrap)
//                .positiveColorRes(R.color.primary_bootstrap)
//                .negativeColorRes(R.color.primary_bootstrap)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which){
//                        str_amount = et_amount.getText().toString();
//                        str_note = et_note.getText().toString();
//                        if(str_amount.isEmpty() || str_note.isEmpty()){
//                            Toasty.info(context,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
//                        }else{
//                            double amount = Double.parseDouble(str_amount);
//                            String note = str_note;
//                            verify_update(record,amount,note);
//                        }
//                    }
//                })
//                .build();
//
//        positive_button = update_dialog.getActionButton(DialogAction.POSITIVE);
//        //positive_button.setEnabled(false);
//
//        name_category = (TextView)update_dialog.getCustomView().findViewById(R.id.category_name);
//        et_amount = (EditText)update_dialog.getCustomView().findViewById(R.id.update_amount);
//        et_note = (EditText)update_dialog.getCustomView().findViewById(R.id.update_note);
//
//        name_category.setText(record.getCategory().getCat_item());
//        et_amount.setText(""+record.getData_amount());
//        et_note.setText(record.getShortnote());
//
//        update_dialog.show();
    }

    public void verify_update(Records record, double new_amount, String new_note){
        double amount = new_amount;
        String note = new_note;
        Date today = Calendar.getInstance().getTime();

        Records records = Records.load(Records.class,record.getId());
        records.setData_amount(amount);
        record.setShortnote(note);
       // record.setData_updated(today);
        records.save();
        notifyDataSetChanged();
        Toasty.success(context,"รายการของคุณปรับปรุงสำเร็จ",Toast.LENGTH_SHORT).show();
    }

    public void do_delete(final Records record){
//        String amount = "จำนวนเงิน : "+thb+record.getData_amount();
//        String category_item = "ชือรายการ : "+record.getCategory().getCat_item();
//        boolean wrapInScrollView = true;
//        MaterialDialog delete_dialog = new MaterialDialog.Builder(context)
//                .customView(R.layout.delete_information,wrapInScrollView)
//                .positiveText("ลบ")
//                .negativeText("ยกเลิก")
//                .backgroundColorRes(R.color.default_bootstrap)
//                .positiveColorRes(R.color.danger_bootstrap)
//                .negativeColorRes(R.color.primary_bootstrap)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which){
//                        verify_delete(record);
//                    }
//                })
//                .build();
//        txt_amount = (TextView)delete_dialog.getCustomView().findViewById(R.id.data_amount);
//        txt_catitem = (TextView)delete_dialog.getCustomView().findViewById(R.id.category);
//        txt_amount.setText(amount);
//        txt_catitem.setText(category_item);
//        delete_dialog.show();

    }

    public void verify_delete(Records record){
        Records records = Records.load(Records.class, record.getId());
        records.delete();
        listStorage.remove(data_position);
        notifyDataSetChanged();
        Toasty.success(context,"ลบรายการสำเร็จ",Toast.LENGTH_SHORT).show();
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