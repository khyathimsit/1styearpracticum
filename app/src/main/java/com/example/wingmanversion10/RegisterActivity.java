package com.example.wingmanversion10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccount;
    private EditText username, phoneNumber, password, email;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.register_username);
        email = findViewById(R.id.email_id);
        phoneNumber = findViewById(R.id.register_phone_number);
        password = findViewById(R.id.register_password);
        createAccount = findViewById(R.id.register_btn);
        loadingBar = new ProgressDialog(RegisterActivity.this);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name = username.getText().toString();
        String phone = phoneNumber.getText().toString().trim();
        String pswd = password.getText().toString();
        String mail = email.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(mail)){
            Toast.makeText(this, "Please enter your mailId", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pswd)){
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
        }
        else{
//            loadingBar.setTitle("Creating Account");
//            loadingBar.setMessage("Please wait, the page is loading");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "After clicking button", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, VerifyOTP.class);
        String str = name + ","+ phone + "," + pswd + "," + mail;
        intent.putExtra("details", str);
        //intent.putExtra("mobile", phone);
        startActivity(intent);
        //finish();
    }
}
