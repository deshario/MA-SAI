package com.deshario.agriculture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserRegister extends AppCompatActivity {

    SharedPreference sharedPreference;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.material_primary));
        setContentView(linearLayout);
        sharedPreference = new SharedPreference(this);
        if (!sharedPreference.isInitialLaunch()) {
            login();
        }
        renderDialog();
    }

    public void renderDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.set_password, null);
        builder.setView(view);
        alertDialog = builder.create();

        final TextView headerTitle = (TextView) view.findViewById(R.id.header);
        final TextView firstTitle = (TextView) view.findViewById(R.id.title1);
        final TextView secondTitle = (TextView) view.findViewById(R.id.title2);
        final EditText password = (EditText) view.findViewById(R.id.oldPass);
        final EditText confirmPassword = (EditText) view.findViewById(R.id.newPass);
        final Button cancelBtn = (Button) view.findViewById(R.id.cancel);
        final Button submitBtn = (Button) view.findViewById(R.id.submit);

        headerTitle.setText("ตั้งรหัสผ่านสำหรับการใช้งาน");
        firstTitle.setText("รหัสผ่าน");
        secondTitle.setText("ยืนยันรหัสผ่าน");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                if(pass.equals("") || confirmPass.equals("")){
                    Toast.makeText(UserRegister.this,"กรุณากรอกทุกช่อง",Toast.LENGTH_SHORT).show();
                }else{
                    if(pass.length() == 5 && confirmPass.length() == 5){
                        if(pass.equals(confirmPass)){
                            setPassword(confirmPass);
                        }else{
                            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            assert vib != null;
                            vib.vibrate(400);
                            Toast.makeText(UserRegister.this,"รหัสผ่านไม่ตรงกัน",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(UserRegister.this,"ความยาวรหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alertDialog.show();
    }

    public void setPassword(String confirmPass){
        sharedPreference.setLoginPassword(confirmPass);
        sharedPreference.resetPassword(false);
        sharedPreference.setInitialLaunch(false);
        finish();
        startActivity(new Intent(this, UserLogin.class));
    }

    public void login(){
        sharedPreference.setInitialLaunch(false);
        sharedPreference.resetPassword(false);
        finish();
        startActivity(new Intent(this, UserLogin.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()){
            alertDialog.cancel();
        }
    }

}
