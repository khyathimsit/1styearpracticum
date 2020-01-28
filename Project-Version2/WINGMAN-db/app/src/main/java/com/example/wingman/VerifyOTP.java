package com.example.wingman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    private EditText editTextCode;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    String Name = "";
    String Phone = "";
    String Password = "";
    String Mail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        editTextCode = findViewById(R.id.editTextCode);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(VerifyOTP.this);
        String details = getIntent().getStringExtra("details");
        String[] tokens = details.split(",");
        Name = tokens[0];
        Phone = tokens[1];
        Password = tokens[2];
        Mail = tokens[3];
        //Log.d("helo", Phone + "  Phone");
        sendVerificationCode(Phone);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

        Toast.makeText(VerifyOTP.this, " OTP generated Successfully!", Toast.LENGTH_LONG).show();

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                Log.d("hello", " before onVerificationCompleted");
                editTextCode.setText(code);
                Log.d("hello", " after onVerificationCompleted");
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ValidatephoneNumber(Name, Phone, Mail, Password);
                            Intent intent = new Intent(VerifyOTP.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(VerifyOTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void ValidatephoneNumber(final String name, final String phone, final String mail, final String pswd) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", name);
                    userdataMap.put("phone", phone);
                    userdataMap.put("mail", mail);
                    userdataMap.put("password", pswd);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(VerifyOTP.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(VerifyOTP.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(VerifyOTP.this, "Network error: Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(VerifyOTP.this, "This phone number: " + phone + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}