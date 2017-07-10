package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.deshario.agriculture.AddRecords;
import com.deshario.agriculture.CustomRangeInputFilter;
import com.deshario.agriculture.Fragments.AllRecords;
import com.deshario.agriculture.Fragments.Categories_Tab_Frag;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;

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
    public Records getItem(int position) {
        return listStorage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        ImageView img;
        TextView date;
        TextView note;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View row;
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            row  = layoutinflater.inflate(R.layout.cardview, parent, false);
            listViewHolder.img = (ImageView)row.findViewById(R.id.img_icon);
            listViewHolder.date = (TextView)row.findViewById(R.id.titler);
            listViewHolder.note = (TextView)row.findViewById(R.id.desc);

            row .setTag(listViewHolder);
        }else{
            row = convertView;
            listViewHolder = (ViewHolder)row.getTag();
        }

        row.setId(position);

        listViewHolder.img.setImageResource(R.drawable.refund);
        listViewHolder.date.setText(getThaiDate(listStorage.get(position).getData_recorded()));
        listViewHolder.note.setText(listStorage.get(position).getShortnote());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Records record = getItem(row.getId());
                view_data(record);
            }
        });

        return row;
    }

    public void view_data(final Records record){
        new MaterialDialog.Builder(context)
                .title("ตัวเลือก")
                .items(R.array.crud_options)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        int position = which;
                        switch (position){
                            case -1:
                                Toast.makeText(context,"select atleast one!",Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                do_view(record);
                                break;
                            case 1:
                                do_update(record);
                                break;
                            case 2:
                                do_delete(record);
                                break;
                            default:
                        }
                        return true;
                    }
                })
                .positiveText("เลือก")
                .show();
    }

    public void do_view(Records record){
        String amount = "จำนวนเงิน : "+thb+record.getData_amount();
        String rec_date = "วันที่ : "+getThaiDate(record.getData_recorded());
        String note = "บันทึกย่อ : "+record.getShortnote();
        String category_topic = "หมวดหมู่ : "+record.getCategory().getCat_topic();
        String category_item = "ชือรายการ : "+record.getCategory().getCat_item();
        boolean wrapInScrollView = true;
        MaterialDialog viewer_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.view_records,wrapInScrollView)
                .positiveText("ตกลง")
                .backgroundColorRes(R.color.default_bootstrap)
                .positiveColorRes(R.color.primary_bootstrap)
                .negativeColorRes(R.color.primary_bootstrap)
                .build();
        txt_cattopic = (TextView)viewer_dialog.getCustomView().findViewById(R.id.cat_topic);
        txt_amount = (TextView)viewer_dialog.getCustomView().findViewById(R.id.data_amount);
        txt_date = (TextView)viewer_dialog.getCustomView().findViewById(R.id.data_date);
        txt_note = (TextView)viewer_dialog.getCustomView().findViewById(R.id.note);
        txt_catitem = (TextView)viewer_dialog.getCustomView().findViewById(R.id.category);

        txt_cattopic.setText(category_topic);
        txt_amount.setText(amount);
        txt_date.setText(rec_date);
        txt_note.setText(note);
        txt_catitem.setText(category_item);
        viewer_dialog.show();
    }

    public void do_update(final Records record){
        boolean wrapInScrollView = true;
        MaterialDialog update_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.update_records,wrapInScrollView)
                .positiveText("ปรับปรุง")
                .negativeText("ยกเลิก")
                .backgroundColorRes(R.color.default_bootstrap)
                .positiveColorRes(R.color.primary_bootstrap)
                .negativeColorRes(R.color.primary_bootstrap)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which){
                        str_amount = et_amount.getText().toString();
                        str_note = et_note.getText().toString();
                        if(str_amount.isEmpty() || str_note.isEmpty()){
                            Toast.makeText(context,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                        }else{
                            double amount = Double.parseDouble(str_amount);
                            String note = str_note;
                            verify_update(record,amount,note);
                        }
                    }
                })
                .build();

        name_category = (TextView)update_dialog.getCustomView().findViewById(R.id.category_name);
        et_amount = (EditText)update_dialog.getCustomView().findViewById(R.id.update_amount);
        et_note = (EditText)update_dialog.getCustomView().findViewById(R.id.update_note);

        name_category.setText(record.getCategory().getCat_item());
        et_amount.setText(""+record.getData_amount());
        et_note.setText(record.getShortnote());

        update_dialog.show();
    }

    public void verify_update(Records record, double new_amount, String new_note){
        double amount = new_amount;
        String note = new_note;
        Date today = Calendar.getInstance().getTime();

        Records records = Records.load(Records.class,record.getId());
        records.setData_amount(amount);
        record.setShortnote(note);
        record.setData_updated(today);
        records.save();

        notifyDataSetChanged();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        long datetime = calendar.getTimeInMillis();
        boolean status = Records.check_updated(datetime);
        if(status == true){
            Toast.makeText(context,"รายการของคุณปรับปรุงสำเร็จ",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"ไม่สามารถปรับปรุงรายการของคุณได้",Toast.LENGTH_SHORT).show();
        }
    }


    public void do_delete(Records record){
        Toast.makeText(context,"do_delete : "+record.getShortnote(),Toast.LENGTH_SHORT).show();
        ColorDialog dialog = new ColorDialog(context);
        dialog.setTitle("ยืนยันการลบ");
        dialog.setContentText("จำนวนเงิน : "+record.getData_amount()+"\nบันทึกย่อ : "+record.getShortnote()+
        "\nหมวดหมู่ : "+record.getCategory().getCat_item());
        //dialog.setContentImage(context.getResources().getDrawable(R.drawable.hd));
        dialog.setPositiveListener("ลบ", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                Toast.makeText(context, dialog.getPositiveText().toString(), Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeListener("ยกเลิก", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        Toast.makeText(context, dialog.getNegativeText().toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).show();
//        MaterialDialog dialog = new MaterialDialog.Builder(context)
//                .title("รายการ : "+record.getShortnote())
//                .positiveText("ลบ")
//                .negativeText("ยกเลิก")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        Toast.makeText(context,"dialog positive",Toast.LENGTH_LONG).show();
//                    }
//                })
//                .show();
    }

    public String getThaiDate(Date date){
        //System.out.println("transform this : "+date); //Sat Jul 29 00:00:00 GMT+07:00 2017
        String thai_date = null;

        String DATE_FORMAT_NOW = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(date);
        String[] output = stringDate.split("-");
        //System.out.println("transformed date : "+stringDate);

        int _day = Integer.valueOf(output[0]);
        int _month = Integer.valueOf(output[1]);
        int _year = Integer.valueOf(output[2]);

        int day_ = _day;
        String month_ = Th_Months(_month-1);
        int year_ = Th_Year(_year);

        thai_date = day_+" "+month_+" "+year_;

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