package com.deshario.agriculture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deshario.agriculture.Config.AppPreferenceActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import es.dmoral.toasty.Toasty;
import lib.folderpicker.FolderPicker;

public class SettingsActivity extends AppPreferenceActivity {

    private static final int SDCARD_PERMISSION = 1, FOLDER_PICKER_CODE = 2, FILE_PICKER_CODE = 3;

    private String import_Export = null;

    public String getImport_Export() {
        return import_Export;
    }

    public void setImport_Export(String import_Export) {
        this.import_Export = import_Export;
    }

    private static SettingsActivity activity = null;

    private static final String TAG = SettingsActivity.class.getSimpleName();

    SharedPreference sharedPreference;
    private static AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("การตั้งค่า");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
        sharedPreference = new SharedPreference(this);
        activity = this;
    }

    public static SettingsActivity getInstance() {
        return activity;
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            Preference changePass = (Preference) findPreference("changePassword");
            Preference exportData = (Preference) findPreference("export");
            Preference importData = (Preference) findPreference("import");

            bindPreferenceSummaryToValue(changePass);
            bindPreferenceSummaryToValue(exportData);
            bindPreferenceSummaryToValue(importData);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference){
                String key = preference.getKey();
                switch (key){
                    case "changePassword":
                        changePassword();
                        break;
                    case "export":
                        exportDB();
                        break;
                    case "import":
                        importDB();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    public static void changePassword(){
        final Context context = SettingsActivity.getInstance();
        final SharedPreference tempSharedPreference = SettingsActivity.getInstance().sharedPreference;
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.getInstance());
        LayoutInflater inflater = SettingsActivity.getInstance().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_password, null);
        builder.setView(view);
        alertDialog = builder.create();

        final EditText oldPassword = (EditText) view.findViewById(R.id.oldPass);
        final EditText newPassword = (EditText) view.findViewById(R.id.newPass);
        final Button cancelBtn = (Button) view.findViewById(R.id.cancel);
        final Button submitBtn = (Button) view.findViewById(R.id.submit);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passOld = oldPassword.getText().toString();
                String passNew = newPassword.getText().toString();
                if(passOld.equals("") || passNew.equals("")){
                    Toasty.warning(context,"กรุณากรอกทุกช่อง",Toast.LENGTH_SHORT,true).show();
                }else{
                    if(passOld.length() == 5 && passNew.length() == 5){
                        if(passOld.equals(tempSharedPreference.getLoginPassword())){
                            tempSharedPreference.setLoginPassword(passNew);
                            tempSharedPreference.setInitialLaunch(false);
                            tempSharedPreference.resetPassword(true);
                            Toasty.success(context,"อัปเดตรหัสผ่านสำเร็จ \nกรุณาเข้าสู่ระบบอีกครั้ง",Toast.LENGTH_LONG,true).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    SettingsActivity.activity.finish();
                                }
                            }, 3000);

                        }else{
                            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            assert vib != null;
                            vib.vibrate(400);
                            Toasty.error(context,"รหัสผ่านเก่าไม่ถูกต้อง",Toast.LENGTH_SHORT,true).show();
                        }
                    }else{
                        Toasty.error(context,"ความยาวรหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT,true).show();
                    }
                }
            }
        });

        alertDialog.show();
    }
    public void setPassword(String confirmPass){
        sharedPreference.setLoginPassword(confirmPass);
        sharedPreference.setInitialLaunch(false);
        startActivity(new Intent(this, UserLogin.class));
    }


    public static void importDB(){
        SettingsActivity.getInstance().setImport_Export("import");
        if(checkPermission()){
            Intent intent = new Intent(SettingsActivity.getInstance(), FolderPicker.class);
            intent.putExtra("title", "เลือกไฟล์ที่จะนำเข้า");
            intent.putExtra("pickFiles", true);
            SettingsActivity.getInstance().startActivityForResult(intent,FILE_PICKER_CODE);
        }else{
            ActivityCompat.requestPermissions(SettingsActivity.getInstance(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},SDCARD_PERMISSION);
        };
    }

    public static void exportDB(){
        if(checkPermission()){
            SettingsActivity.getInstance().setImport_Export("export");
            Intent intent = new Intent(SettingsActivity.getInstance(), FolderPicker.class);
            intent.putExtra("title", "เลือกโฟลเดอร์เพื่อบันทึกไฟล์");
            SettingsActivity.getInstance().startActivityForResult(intent, FOLDER_PICKER_CODE);
        }else{
            ActivityCompat.requestPermissions(SettingsActivity.getInstance(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SDCARD_PERMISSION);
        }
    }

    public static boolean checkPermission(){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion >= Build.VERSION_CODES.M){
            int permissionCheck = ContextCompat.checkSelfPermission(SettingsActivity.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return permissionCheck == PackageManager.PERMISSION_GRANTED;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case SDCARD_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if(SettingsActivity.getInstance().getImport_Export() == "import"){
                        importDB();
                    }else if(SettingsActivity.getInstance().getImport_Export() == "export"){
                        exportDB();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == FOLDER_PICKER_CODE) {
            if (resultCode == Activity.RESULT_OK && intent.hasExtra("data")) {
                String folderLocate = intent.getExtras().getString("data");
                System.out.println("folderLocation :: "+ folderLocate);
                exportDBNow(folderLocate);
            }
        } else if (requestCode == FILE_PICKER_CODE) {
            if (resultCode == Activity.RESULT_OK && intent.hasExtra("data")) {
                String fileLocate = intent.getExtras().getString("data");
                System.out.println("fileLocation :: "+ fileLocate);
                importDBNow(fileLocate);
            }
        }
    }

    private void exportDBNow(String foldertoSave){
        File sd = new File(foldertoSave);
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {
            File data = Environment.getDataDirectory();
            FileChannel source=null;
            FileChannel destination=null;
            String currentDBPath = "/data/"+ SettingsActivity.getInstance().getPackageName() +"/databases/"+ "agriculture.db";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, "agriculture.db");
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toasty.success(this, "Export Success", Toast.LENGTH_SHORT,true).show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void importDBNow(String fileLocate){
        File sd = new File(fileLocate);
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
//        String backupDBPath = "/data/"+ SettingsActivity.getInstance().getPackageName() +"/databases/"+"agriculture.db";
        String backupDBPath = "/data/"+ SettingsActivity.getInstance().getPackageName() +"/databases/";
        String currentDBPath = "agriculture.db";
        File currentDB = new File(backupDBPath, currentDBPath);
        File backupDB = new File(data, fileLocate);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toasty.success(this, "Please wait", Toast.LENGTH_SHORT,true).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()){
            alertDialog.cancel();
        }
        //android.os.Process.killProcess(android.os.Process.myPid());
        //super.onDestroy();
    }
}
