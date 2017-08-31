package com.deshario.agriculture.Fragments;

        import android.content.Context;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AlertDialog;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.deshario.agriculture.Models.Category;
        import com.deshario.agriculture.Models.Records;
        import com.deshario.agriculture.R;
        import com.deshario.agriculture.Adapters.CategoryAdapter;
        import com.franmontiel.fullscreendialog.FullScreenDialogContent;
        import com.franmontiel.fullscreendialog.FullScreenDialogController;
        import com.franmontiel.fullscreendialog.FullScreenDialogFragment;

        import java.util.ArrayList;
        import java.util.List;

        import es.dmoral.toasty.Toasty;

/**
 * Created by deshario on 10/06/17.
 */
public class Categories1_Frag extends Fragment implements FullScreenDialogContent{
    public static Context context;
    public static CategoryAdapter adapter;
    public static ArrayList<String> arrayList;
    public static ListView listView;
    public String new_item;

    public static Button btn_add,btn_del;

    private FullScreenDialogController dialogController;
    FullScreenDialogFragment dialogFragment;
    public List<Category> category_items;

    public Categories1_Frag() {
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
        category_items = Category.getItembyTopic(1);
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
            public void onClick(View myview) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.categories_input, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(view);
                alert.setCancelable(false);
                final AlertDialog dialog = alert.create();
                dialog.getWindow().setDimAmount(0.5f);
                dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                final TextView my_title = (TextView)view.findViewById(R.id.item_title);
                final EditText et_topic = (EditText)view.findViewById(R.id.input_title);

                int pageno = Categories_Tab_Frag.pageno;
                final String title = Categories_Tab_Frag.getTitle(pageno);
                my_title.setText("หมวดหมู่ : "+title);

                final Button btn_save = (Button)view.findViewById(R.id.save_btn);
                final ImageButton btn_close = (ImageButton)view.findViewById(R.id.close_modal);
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String new_item = et_topic.getText().toString();
                        if(new_item == null || new_item.isEmpty()){
                            Toasty.info(getActivity(),"กรุณากรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show();
                        }else{
                            if(new_item.length() <=5 ){
                                Toasty.info(getActivity(),"ชื่อรายการสั้นเกินไป",Toast.LENGTH_SHORT).show();
                            }else{
                                boolean validate = Category.check_exists(new_item);
                                if(validate == true){
                                    Toasty.info(context," กรุณาเลือกชื่ออื่น \n\n ชื่อนี้ถูกเลือกไว้แล้ว",Toast.LENGTH_SHORT).show();
                                }else{
                                    Category category = new Category();
                                    category.setCat_topic(title);
                                    category.setCat_item(new_item);
                                    category.setCat_type(Category.CATEGORY_DEBTS);
                                    category.save();

                                    boolean status = Category.check_exists(new_item);
                                    if(status == true){
                                        Toasty.success(context,"รายการของคุณถูกบันทึกแล้ว",Toast.LENGTH_SHORT).show();
                                    }
                                    resetdata();
                                    dialog.dismiss();
                                }
                            }
                        }
                    }
                });
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = adapter.getCurrent();
                if(pos != -1){ // Valid Position
                    Object object = adapter.getItem(pos);
                    String cat = String.valueOf(object); // This is Itemname
                    Category category = new Category();
                    category = Category.getSingleCategory(cat);
                    //System.out.println("ID : "+category.getId());

                    final Category inner_category = category;

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View myview = inflater.inflate(R.layout.categories_delete, null);
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setView(myview);
                    alert.setCancelable(false);
                    final AlertDialog dialog = alert.create();
                    dialog.getWindow().setDimAmount(0.5f);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDownAnimation;
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    final TextView et_title = (TextView)myview.findViewById(R.id.info_title);
                    final TextView et_message = (TextView)myview.findViewById(R.id.info_message);
                    final Button et_save = (Button)myview.findViewById(R.id.save_btn);
                    final Button et_delete = (Button)myview.findViewById(R.id.delete_btn);
                    final ImageButton et_close = (ImageButton)myview.findViewById(R.id.et_close);
                    et_title.setText("ลบรายการ ");
                    et_message.setText("รายการ : "+category.getCat_item());
                    et_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    et_save.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            delete(inner_category.getId());
                            dialog.dismiss();
                        }
                    });
                    et_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    public void delete(long id){
        Category category = Category.load(Category.class, id);
        List<Records> records = Records.getSpecificRecordsByItem(category);
        int count = records.size();
        if(count >= 1){
            Toasty.info(context,"รายการนี้ไม่สามารถลบได้",Toast.LENGTH_SHORT).show();
        }else{
            category.delete();
            resetdata();
            Toasty.success(context,"รายการของคุณถูกลบเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
        }
    }

    public static void resetdata(){
        arrayList.clear();
        new Categories1_Frag().dowork();
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        return false;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }
}
