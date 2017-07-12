package com.deshario.agriculture.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.deshario.agriculture.Models.Category;
import com.deshario.agriculture.Models.Records;
import com.deshario.agriculture.R;
import com.deshario.agriculture.Adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by deshario on 10/06/17.
 */
public class Categories3_Frag extends Fragment {
    public static Context context;
    public static CategoryAdapter adapter;
    public static ArrayList<String> arrayList;
    public static ListView listView;
    public String new_item;
    public List<Category> category_items;

    public static Button btn_add,btn_del;

    public Categories3_Frag() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categories_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListView(view);
        btn_add = (Button)view.findViewById(R.id.show_button);
        btn_del = (Button)view.findViewById(R.id.delete_button);
        onClickEvent(view);
        new_item = null;
    }

    private void loadListView(View view) {
        listView = (ListView) view.findViewById(R.id.list_view);
        arrayList = new ArrayList<>();
        dowork();
    }

    public void dowork(){
        category_items = Category.getItembyTopic(3);
        for (int i = 0; i < category_items.size(); i++) {
            arrayList.add(category_items.get(i).getCat_item());
        }
        adapter = new CategoryAdapter(context, arrayList, true);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void onClickEvent(View view) {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected position
                //adapter.getSelectedItem();
                int pageno = Categories_Tab_Frag.pageno;
                final String title = Categories_Tab_Frag.getTitle(pageno);
                //Toast.makeText(getActivity(),"Add Data pageno "+pageno,Toast.LENGTH_SHORT).show();
                new MaterialDialog.Builder(getActivity())
                        .title("หมวดหมู่ : "+title)
                        .content("หัวข้อ")
                        .positiveText("เพิ่ม")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("กรุณาป้อนชือ", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                new_item = String.valueOf(input);
                                if(new_item == null || new_item.isEmpty()){
                                    Toasty.info(getActivity(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(new_item.length() <=5 ){
                                        Toasty.info(getActivity(),"ชื่อรายการสั้นเกินไป",Toast.LENGTH_SHORT).show();
                                    }else{
                                        boolean validate = Category.check_exists(new_item);
                                        if(validate == true){
                                            Toasty.info(context," กรุณาเลือกชื่ออื่น \n\n ชื่อนี้ถูกเลือกไว้แล้ว",Toast.LENGTH_SHORT).show();
                                        }else{
                                            //Toast.makeText(getActivity(),new_item+" : "+title,Toast.LENGTH_SHORT).show();
                                            Category category = new Category();
                                            category.setCat_topic(title);
                                            category.setCat_item(new_item);
                                            category.setCat_type(3);
                                            category.save();

                                            boolean status = Category.check_exists(new_item);
                                            if(status == true){
                                                Toasty.success(context,"รายการของคุณถูกบันทึกแล้ว",Toast.LENGTH_SHORT).show();
                                            }
                                            resetdata();
                                        }
                                    }
                                }

                            }
                        }).show();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = adapter.getCurrent();
                if(pos != -1){ // Valid Position
                    Object object = adapter.getItem(pos);
                    String cat = String.valueOf(object);
                    Category category = new Category();
                    category = Category.getSingleCategory(cat);
                    //System.out.println("ID : "+category.getId());

                    final Category inner_category = category;
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                            .title("รายการ : "+category.getCat_item())
                            .content("คุณแน่ใจหรือไม่ที่จะลบรายการนี้ ?")
                            .positiveText("ฉันแน่ใจ")
                            .negativeText("ฉันไม่แน่ใจ")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    delete(inner_category.getId());
                                    dialog.dismiss();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            });

                    MaterialDialog dialog = builder.build();
                    dialog.show();
                }

            }
        });

    }

    public void delete(long id){
        Category category = Category.load(Category.class, id);
        List<Records> records = Records.getSpecificRecordsByItem(category);
        int count = records.size();
        if(count >= 1){
            Toast.makeText(context,"รายการนี้ไม่สามารถลบได้",Toast.LENGTH_SHORT).show();
        }else{
            category.delete();
            resetdata();
            Toast.makeText(context,"รายการของคุณถูกลบเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
        }
    }

    public static void resetdata(){
        arrayList.clear();
        new Categories3_Frag().dowork();
    }
}
