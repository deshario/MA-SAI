package com.deshario.agriculture.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deshario.agriculture.Deshario_Functions;
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
    private FullScreenDialogFragment dialogFragment;
    private String str_amount=null, str_note=null;
    int data_position;
    View positive_button;
    private static int sel_position = -1;

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
                listViewHolder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.material_danger));
                listViewHolder.img.setImageResource(R.drawable.debt);
                listViewHolder.ln.setBackground(ContextCompat.getDrawable(context,R.drawable.cardview_border_material_danger));
                listViewHolder.line.setBackgroundColor(ContextCompat.getColor(context,R.color.material_danger));
                break;
            case 2: // expense
                listViewHolder.title.setText("ค่าใช้จ่าย");
                listViewHolder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.material_primary));
                listViewHolder.img.setImageResource(R.drawable.refund);
                listViewHolder.ln.setBackground(ContextCompat.getDrawable(context,R.drawable.cardview_border_primary));
                listViewHolder.line.setBackgroundColor(ContextCompat.getColor(context,R.color.material_primary));
                break;
            case 3: // income
                listViewHolder.title.setText("รายได้");
                listViewHolder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.material_success));
                listViewHolder.img.setImageResource(R.drawable.wallet);
                listViewHolder.ln.setBackground(ContextCompat.getDrawable(context,R.drawable.cardview_border_material_sucess));
                listViewHolder.line.setBackgroundColor(ContextCompat.getColor(context,R.color.material_success));
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
                view_options(record);
            }
        });

        return row;
    }

    public void view_options(final Records record){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("ตัวเลือก");
        alert.setCancelable(false);
        alert.setSingleChoiceItems(R.array.crud_options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sel_position = which;
            }
        });
        alert.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                select(sel_position,record);
                sel_position = -1; // Reset
            }
        });
        alert.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sel_position = -1; // Reset
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = alert.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.5f;
        dialog.getWindow().setDimAmount(0.5f);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.FadeAnimation;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

    public void select(int position, Records record){
        switch (position){
            case -1:
                Toasty.info(context,"กรุณาเลือกรายการใดรายการหนึ่ง",Toast.LENGTH_SHORT).show();
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
    }

    public void do_view(Records record){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_records, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String amount = "จำนวนเงิน : "+thb+record.getData_amount();
        String rec_date = "วันที่ : "+getThaiDate(record.getData_created());
        String note = "บันทึกย่อ : "+record.getShortnote();
        String category_topic = "หมวดหมู่ : "+record.getCategory().getCat_topic();
        String category_item = "ชือรายการ : "+record.getCategory().getCat_item();

        ImageButton close_btn = (ImageButton)view.findViewById(R.id.close_btn);
        TextView txt_amount = (TextView)view.findViewById(R.id.data_amount);
        TextView txt_date = (TextView)view.findViewById(R.id.data_date);
        TextView txt_note = (TextView)view.findViewById(R.id.data_note);
        TextView txt_category_topic = (TextView)view.findViewById(R.id.cat_topic);
        TextView txt_category_item = (TextView)view.findViewById(R.id.data_category);

        txt_amount.setText(amount);
        txt_date.setText(rec_date);
        txt_note.setText(note);
        txt_category_topic.setText(category_topic);
        txt_category_item.setText(category_item);

        close_btn.setImageDrawable(Deshario_Functions.setTint(
                context.getResources().getDrawable(R.drawable.ic_close_white_36dp),
                context.getResources().getColor(R.color.my_gray)
        ));
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void do_update(final Records record){
        LayoutInflater inflater = LayoutInflater.from(context);
        View myview = inflater.inflate(R.layout.update_records, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(myview);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageButton close_btn = (ImageButton)myview.findViewById(R.id.close_btn);
        TextView name_category = (TextView)myview.findViewById(R.id.category_name);
        final EditText et_amount = (EditText)myview.findViewById(R.id.update_amount);
        final EditText et_note = (EditText)myview.findViewById(R.id.update_note);
        Button update_btn = (Button)myview.findViewById(R.id.update_btn);

        name_category.setSelected(true);

        name_category.setText(record.getCategory().getCat_item());
        et_amount.setText(""+record.getData_amount());
        et_note.setText(record.getShortnote());

        close_btn.setImageDrawable(Deshario_Functions.setTint(
                context.getResources().getDrawable(R.drawable.ic_close_white_36dp),
                context.getResources().getColor(R.color.my_gray)
        ));
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_amount = et_amount.getText().toString();
                str_note = et_note.getText().toString();
                if(str_amount.isEmpty() || str_note.isEmpty()){
                    Toasty.info(context,"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    double amount = Double.parseDouble(str_amount);
                    String note = str_note;
                    verify_update(record,amount,note);
                }
            }
        });
    }

    public void verify_update(Records record, double new_amount, String new_note){
        double amount = new_amount;
        String note = new_note;
        Date today = Calendar.getInstance().getTime();
        Records records = Records.load(Records.class,record.getId());
        records.setData_amount(amount);
        record.setShortnote(note);
        records.save();
        notifyDataSetChanged();
        Toasty.success(context,"รายการของคุณปรับปรุงสำเร็จ",Toast.LENGTH_SHORT).show();
    }

    public void do_delete(final Records record){
        LayoutInflater inflater = LayoutInflater.from(context);
        View my_view = inflater.inflate(R.layout.delete_information, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(my_view);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String category_item = "ชือรายการ : "+record.getCategory().getCat_item();
        String amount = "จำนวนเงิน : "+thb+record.getData_amount();
        TextView txt_item = (TextView)my_view.findViewById(R.id.item1);
        TextView txt_amount = (TextView)my_view.findViewById(R.id.item2);
        Button cancel_btn = (Button)my_view.findViewById(R.id.btn_cancel);
        Button delete_btn = (Button)my_view.findViewById(R.id.btn_delete);
        ImageButton dismiss_btn = (ImageButton)my_view.findViewById(R.id.close_btn);
        txt_item.setText(category_item);
        txt_amount.setText(amount);
        dismiss_btn.setImageDrawable(Deshario_Functions.setTint(
                context.getResources().getDrawable(R.drawable.ic_close_white_36dp),
                context.getResources().getColor(R.color.my_gray)
        ));
        dismiss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                verify_delete(record);
            }
        });
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