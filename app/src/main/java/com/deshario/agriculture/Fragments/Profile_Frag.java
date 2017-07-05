package com.deshario.agriculture.Fragments;


import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.deshario.agriculture.R;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.layernet.thaidatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import me.riddhimanadib.formmaster.helper.FormBuildHelper;
//import me.riddhimanadib.formmaster.listener.OnFormElementValueChangedListener;
//import me.riddhimanadib.formmaster.model.FormElement;
//import me.riddhimanadib.formmaster.model.FormHeader;
//import me.riddhimanadib.formmaster.model.FormObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Frag extends Fragment implements FullScreenDialogContent,
        com.layernet.thaidatetimepicker.date.DatePickerDialog.OnDateSetListener{
    ImageView pic,dialog_pic;
    RecyclerView mRecyclerView;
    //FormBuildHelper mFormBuilder;
    private FullScreenDialogController dialogController;
    public static final String UNIQUE_NAME = "profile_key";
    private static final int TAG_EMAIL = 12;
    FloatingActionButton profile_btn;
    Spinner spinner;
    MaterialBetterSpinner materialDesignSpinner,prefix_spinner;
    ArrayAdapter<String> adapter_arr,adapter_prefix;
    AlertDialog image_dialog;
    EditText et_birth;
    Context mcontext;

    Calendar calendar;
    //List<FormObject> formItems;
    List<String> status_list,prefix_names;

    //FormHeader header1, header2;
    //FormElement name_prefix, name, surname, birthdate, status, partner_name,
      //      partner_surname, partner_child, family_members, address, salary;

    String u_name = null;
    String u_surname = null;
    String val;


    public Profile_Frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_layout, container, false);
        mcontext = getContext();

        profile_btn = (FloatingActionButton) view.findViewById(R.id.profile_edit);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Modify Profile",Toast.LENGTH_SHORT).show();
                modify_profile();
            }
        });

        pic = (ImageView)view.findViewById(R.id.profile_pic);

        pic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //Log.d("Touch", "Touch down");
                        imageviewer();
                        break;

                    case MotionEvent.ACTION_UP:
                        //Log.d("Touch", "Touch up");
                        close_imageviewer();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

//        pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageviewer();
//            }
//        });
//        pic.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                imageviewer();
//                return false;
//            }
//        });

//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        mFormBuilder = new FormBuildHelper(getActivity(), mRecyclerView, new OnFormElementValueChangedListener() {
//            @Override
//            public void onValueChanged(FormElement formElement) {
//                check(formElement.getValue());
//            }
//        });
//
//        header1 = FormHeader.createInstance().setTitle("ข้อมูลส่วนตัว");
//
//        header2 = FormHeader.createInstance().setTitle("ข้อมูอืนๆ");
//
//        prefix_names = new ArrayList < > ();
//        prefix_names.add("นาย");
//        prefix_names.add("นาง");
//        prefix_names.add("น.ส");
//
//        name_prefix = FormElement.createInstance()
//                .setType(FormElement.TYPE_SPINNER_DROPDOWN)
//                .setTitle("คำนามหน้า")
//                .setOptions(prefix_names)
//                .setHint("กดเพื่อเลือก");
//
//        name = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE)
//                .setTitle("ชื่อ")
//                .setHint("ชื่อ")
//                .setValue(u_name);
//
//        surname = FormElement.createInstance().setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE)
//                .setTitle("นามสกุล")
//                .setHint("นามสกุล")
//                .setValue(u_surname);
//
//        birthdate = FormElement.createInstance()
//                .setType(FormElement.TYPE_PICKER_DATE)
//                .setTitle("วันเกิด")
//                .setHint("กรุณาเลือกวันที่");
//
//        status_list = new ArrayList < > ();
//        status_list.add("โสด");
//        status_list.add("สมรส");
//        status_list.add("หญาร่าง");
//        status = FormElement.createInstance().setTag(TAG_EMAIL)
//                .setType(FormElement.TYPE_SPINNER_DROPDOWN)
//                .setTitle("สถานะ").setOptions(status_list)
//                .setHint("กรุณาเลือกสถานะ").setRequired(true);
//
//        partner_name = FormElement.createInstance()
//                .setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE)
//                .setTitle("ชื่อคู่สมรส")
//                .setHint("ชื่อคู่สมรส");
//
//        partner_surname = FormElement.createInstance()
//                .setType(FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE)
//                .setTitle("นามสกุลคู่สมรส")
//                .setHint("นามสกุลคู่สมรส");
//
//        partner_child = FormElement.createInstance()
//                .setType(FormElement.TYPE_EDITTEXT_NUMBER)
//                .setTitle("จำนวนบุตร")
//                .setHint("0 คน");
//
//        family_members = FormElement.createInstance()
//                .setType(FormElement.TYPE_EDITTEXT_NUMBER)
//                .setTitle("จำนวนสมาชิกในบ้าน")
//                .setHint("0 คน");
//
//        salary = FormElement.createInstance()
//                .setType(FormElement.TYPE_EDITTEXT_NUMBER)
//                .setTitle("รายได้ต่อเดือน")
//                .setHint("0 บาท");
//
//        address = FormElement.createInstance()
//                .setType(FormElement.TYPE_EDITTEXT_TEXT_MULTILINE)
//                .setTitle("ที่อยู่")
//                .setHint("บ้านเลขที่/หมู่ที่ ... \nถนน ... ตำบล ... \nอำเภอ ... จังหวัด ... \nรหัสไปรษณีย์ ...");
//
//        // Add FormElement in List !
//        formItems = new ArrayList < > ();
//        formItems.add(header1);
//        formItems.add(name_prefix);
//        formItems.add(name);
//        formItems.add(surname);
//        formItems.add(birthdate);
//     //   formItems.add(header2);
//        formItems.add(status);
//        formItems.add(family_members);
//        formItems.add(salary);
//        formItems.add(address);
//
//
//        // Build and Display the form
//        mFormBuilder.addFormElements(formItems);
//        mFormBuilder.refreshView();

        return view;
    }

    public void imageviewer(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.img_viewer, null);
        dialogBuilder.setView(dialogView);


        dialog_pic = (ImageView)dialogView.findViewById(R.id.img);
        Integer img_src = Integer.valueOf(R.drawable.user_profile);
        //dialog_pic.setImageDrawable(R.drawable.user_profile);
        dialog_pic.setBackgroundResource(R.drawable.user_profile);
        image_dialog = dialogBuilder.create();
        image_dialog.show();
        image_dialog.setCancelable(true);

        WindowManager.LayoutParams lp = image_dialog.getWindow().getAttributes();
        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.dimAmount = 0.7f;
        image_dialog.getWindow().setAttributes(lp);
        image_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

    }

    public void close_imageviewer(){
       if(image_dialog.isShowing()){
           image_dialog.cancel();
       }
    }


    public void modify_profile(){
        //add_weekselect = null; // Very Important  to clear value when reload dialog
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("ข้อมูลส่วนตัว")
                .customView(R.layout.profile_form, true)
                .backgroundColorRes(R.color.custom_background_disabled)
                .titleColorRes(R.color.primary_bootstrap)
                .positiveText("ตกลง")
                .negativeText("ยกเลิก")
                .canceledOnTouchOutside(true)
                .cancelable(true)
                .build();

      //  spinner = (Spinner)dialog.getCustomView().findViewById(R.id.spinner);
        materialDesignSpinner = (MaterialBetterSpinner)dialog.getCustomView().findViewById(R.id.spin);
        prefix_spinner = (MaterialBetterSpinner)dialog.getCustomView().findViewById(R.id.prefix);
        et_birth = (EditText) dialog.getCustomView().findViewById(R.id.birthdate);

        String[] prefixs = getResources().getStringArray(R.array.prefix_names);
        String[] list = getResources().getStringArray(R.array.martial_status);

        adapter_prefix = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, prefixs);
        adapter_arr = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, list);

       // spinner.setAdapter(adapter);

        prefix_spinner.setAdapter(adapter_prefix);
        materialDesignSpinner.setAdapter(adapter_arr);

        et_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                FragmentManager manager = ((AppCompatActivity)mcontext).getFragmentManager();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Profile_Frag.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(manager, "Datepickerdialog");
                dpd.setCancelText("ยกเลิก");
                dpd.setOkText("เลือก");
            }
        });



        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        //super.onBackPressed();
    }


    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;
    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        Main_Frag.tabBarView.resetFocusOnAllTabs();
        return true; // true = dont do anything
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        Main_Frag.tabBarView.resetFocusOnAllTabs();
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, Planning1_Frag.Th_Year(year));
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat today = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("TH")); // DATE
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("TH"));
        Date date = new Date();
        et_birth.setText(dateFormat.format(calendar.getTime()));
        String _date_ = et_birth.getText().toString();
        et_birth.setText(" "+today.format(calendar.getTime()));
        //Toast.makeText(getActivity(),"Date : "+_date_,Toast.LENGTH_SHORT).show(); // use for insert
        //System.out.println("Date : "+dateFormat.format(date)); //2017-06-10 19:43:39
        //System.out.println("Date : "+date); //Sat Jun 10 19:43:39 GMT+07:00 2017
    }

//    public void check(String data) {
//        FormElement loginElement = mFormBuilder.getFormElement(TAG_EMAIL);
//        val = null;
//        val = loginElement.getValue();
//        if (formItems.size() == 9) {
//            if (val.equals("สมรส")) {
//                formItems.add(7, partner_name);
//                formItems.add(8, partner_surname);
//                formItems.add(9, partner_child);
//            }
//        } else {
//            if (!val.equals("สมรส")) {
//                formItems.remove(partner_name);
//                formItems.remove(partner_surname);
//                formItems.remove(partner_child);
//            }
//        }
//        //mFormBuilder.addFormElements(formItems); // here, formItems is the changed form items
//        //mFormBuilder.refreshView();
//    }
}