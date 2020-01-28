package com.example.wingmanversion10;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyOTP extends AppCompatActivity {

    String mVerificationId;
    String code;
    private EditText editTextCode;
    FirebaseAuth mAuth;

    String Name = "";
    String Phone = "";
    String Password = "";
    String Mail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        Intent intent = getIntent();
        String details = intent.getStringExtra("details");
        String[] tokens = details.split(",");
        Name = tokens[0];
        Phone = tokens[1];
        Password =tokens[2];
        Mail = tokens[3];
        //Log.d(" printing mobile ",mobile);
        sendVerificationCode(Phone);

        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
            }
        });

    }

    void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        Log.d("In Send OTP ", "Sent Code Successfullty");
    }


    //the callback to detect the verification status
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            code = phoneAuthCredential.getSmsCode();
            Log.d("In OnVerificatiStates ", "Entered Here");
//            Log.d(" printing code ",code);
            if (code != null) {
                editTextCode.setText(code);
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
            Log.d("In OnCodeSent ", "Entered Here");
            mVerificationId = s;
        }
    };


    void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        Log.d("In verifyotp ", "Entered Here");
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Validate(Name,Phone,Mail,Password);
                            Intent intent = new Intent(VerifyOTP.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    private void Validate(final String name, final String phone, final String mail, final String pswd) {
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
                                //loadingBar.dismiss();

                                Intent intent = new Intent(VerifyOTP.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                //loadingBar.dismiss();
                                Toast.makeText(VerifyOTP.this, "Network error: Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(VerifyOTP.this, "This phone number: " + phone + " already exists", Toast.LENGTH_SHORT).show();
                    //loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
