package com.deshario.agriculture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import in.arjsna.passcodeview.PassCodeView;

public class MainActivity extends AppCompatActivity {

    private final String Passcode = "3322";
    private PassCodeView passCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        passCodeView = (PassCodeView)findViewById(R.id.pass_code_view);
        TextView promptView = (TextView)findViewById(R.id.promptview);
       // Typeface typeFace = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Font-Bold.ttf");
       // passCodeView.setTypeFace(typeFace);
        passCodeView.setKeyTextColor(R.color.colorAccent);
        passCodeView.setEmptyDrawable(R.drawable.empty_dot);
        passCodeView.setFilledDrawable(R.drawable.filled_dot);
        //passCodeView.setPassCode("1234");
       passCodeView.reset();
       // promptView.setTypeface(typeFace);
        bindEvents();
    }

    private void bindEvents() {
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override public void onTextChanged(String text) {
                if (text.length() == 4) {
                    //if (text.equals(PASSCODE)) {
                       Intent intent = new Intent(MainActivity.this, Main_Tab.class);
                        //Intent intent = new Intent(MainActivity.this, AddRecords.class);
                        startActivity(intent);
                        //getActivity().
                        finish();
                    //} else {
                   //     passCodeView.setError(true);
                   // }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
