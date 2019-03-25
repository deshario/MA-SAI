package com.deshario.agriculture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import in.arjsna.passcodeview.PassCodeView;

public class UserLogin extends AppCompatActivity {

    private PassCodeView passCodeView;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedPreference = new SharedPreference(this);
        sharedPreference.resetPassword(false);
        if (sharedPreference.isInitialLaunch()) {
            startActivity(new Intent(UserLogin.this,UserRegister.class));
        }

        passCodeView = (PassCodeView)findViewById(R.id.pass_code_view);
        passCodeView.setKeyTextColor(R.color.colorAccent);
        passCodeView.setEmptyDrawable(R.drawable.empty_dot);
        passCodeView.setFilledDrawable(R.drawable.filled_dot);
        passCodeView.reset();
        bindEvents();
    }

    private void bindEvents(){
        final String password = sharedPreference.getLoginPassword();
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override public void onTextChanged(String text) {
                if (text.length() == 5) {
                    if (text.equals(password)) {
                        Intent intent = new Intent(UserLogin.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        assert vib != null;
                        vib.vibrate(400);
                        passCodeView.setError(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
