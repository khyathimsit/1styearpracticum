package com.example.wingman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button loginbutton;
    private EditText phoneNumber, password;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumber = (EditText) findViewById(R.id.login_phone_number);
        password = (EditText) findViewById(R.id.login_password);
        loginbutton = (Button) findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String phone = phoneNumber.getText().toString();
        String pswd = password.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pswd)){
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Logging in..");
            loadingBar.setMessage("Please wait, while the page is loading");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            AllowAccessToAccount(phone, pswd);
        }
    }

    private void AllowAccessToAccount(final String phone, final String pswd) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(dataSnapshot.child("Users").child(uid).exists()){
                    Users usersData = dataSnapshot.child("Users").child(uid).getValue(Users.class);
                    //Log.d("hello", phone);
                    //Log.d("hello", pswd);
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(pswd)){
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            intent.putExtra("Number", phone);
                            startActivity(intent);
                        }
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Password entered is incorrect. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + "number does not exists!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}