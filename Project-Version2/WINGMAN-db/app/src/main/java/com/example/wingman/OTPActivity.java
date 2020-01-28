package com.example.wingman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class OTPActivity extends AppCompatActivity {
    private EditText num;
    private Button otp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        num = findViewById(R.id.editTextMobile);
        otp_btn = findViewById(R.id.buttonOTP);

        otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = num.getText().toString().trim();
                if(number.isEmpty() || number.length() < 10){
                    num.setError("Enter a valid mobile");
                    num.requestFocus();
                    return;
                }

                Intent intent = new Intent(OTPActivity.this, VerifyOTP.class);
                intent.putExtra("mobile", number);
                startActivity(intent);

            }
        });
    }
}
